=== scripts/deploy-image.sh ===
#!/bin/bash
set -e
set -o pipefail

echo "======================================"
echo "  AWS ECS Fargate Deployment Script"
echo "======================================"
echo ""

# Configuration
PROJECT_NAME="comptestcrm"
TASK_FAMILY="${PROJECT_NAME}-task"
SERVICE_NAME="${PROJECT_NAME}-service"

# Prompt for AWS configuration
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -p "Enter ECS Cluster Name: " CLUSTER_NAME
read -p "Enter VPC ID: " VPC_ID
read -p "Enter Subnet IDs (comma-separated, at least 2): " SUBNET_INPUT
read -p "Enter Security Group ID: " SECURITY_GROUP
read -p "Enter Docker Image URI: " IMAGE_URI

# Database configuration
echo ""
echo "--- Database Configuration ---"
read -p "Enter Database Host: " DB_HOST
read -p "Enter Database Port (default: 3306): " DB_PORT
DB_PORT=${DB_PORT:-3306}
read -p "Enter Database Name (default: crm): " DB_NAME
DB_NAME=${DB_NAME:-crm}
read -p "Enter Database User: " DB_USER
read -sp "Enter Database Password: " DB_PASSWORD
echo ""

# Parse subnets
IFS=',' read -ra SUBNETS <<< "$SUBNET_INPUT"
SUBNET_1=${SUBNETS[0]}
SUBNET_2=${SUBNETS[1]:-$SUBNET_1}

# Get AWS Account ID
echo ""
echo "Retrieving AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text --region "$AWS_REGION")
echo "Account ID: $ACCOUNT_ID"

# Check/Create ECS Cluster
echo ""
echo "Checking ECS cluster..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating ECS cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
}

# Check/Create CloudWatch Log Group
echo ""
echo "Checking CloudWatch log group..."
LOG_GROUP="/ecs/${PROJECT_NAME}"
aws logs describe-log-groups --log-group-name-prefix "$LOG_GROUP" --region "$AWS_REGION" --query "logGroups[?logGroupName=='$LOG_GROUP']" --output text | grep -q "$LOG_GROUP" || {
    echo "Creating CloudWatch log group: $LOG_GROUP"
    aws logs create-log-group --log-group-name "$LOG_GROUP" --region "$AWS_REGION"
    aws logs put-retention-policy --log-group-name "$LOG_GROUP" --retention-in-days 7 --region "$AWS_REGION"
}

# Load Balancer Configuration
echo ""
read -p "Do you need a load balancer for this service? (y/n): " NEED_LB

if [[ "$NEED_LB" =~ ^[Yy]$ ]]; then
    echo ""
    echo "Creating Application Load Balancer and Target Group..."
    
    # Create ALB
    ALB_NAME="${PROJECT_NAME}-alb"
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name "$ALB_NAME" \
        --subnets "${SUBNET_1}" "${SUBNET_2}" \
        --security-groups "$SECURITY_GROUP" \
        --scheme internet-facing \
        --type application \
        --ip-address-type ipv4 \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || aws elbv2 describe-load-balancers --names "$ALB_NAME" --region "$AWS_REGION" --query 'LoadBalancers[0].LoadBalancerArn' --output text)
    
    echo "Load Balancer ARN: $ALB_ARN"
    
    # Create Target Group with target-type ip (required for Fargate)
    TG_NAME="${PROJECT_NAME}-tg"
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name "$TG_NAME" \
        --protocol HTTP \
        --port 8080 \
        --vpc-id "$VPC_ID" \
        --target-type ip \
        --health-check-enabled \
        --health-check-protocol HTTP \
        --health-check-path "/appinfo/health" \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region "$AWS_REGION" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || aws elbv2 describe-target-groups --names "$TG_NAME" --region "$AWS_REGION" --query 'TargetGroups[0].TargetGroupArn' --output text)
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    
    # Create Listener
    aws elbv2 create-listener \
        --load-balancer-arn "$ALB_ARN" \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn="$TARGET_GROUP_ARN" \
        --region "$AWS_REGION" >/dev/null 2>&1 || echo "Listener already exists"
    
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers --load-balancer-arns "$ALB_ARN" --region "$AWS_REGION" --query 'LoadBalancers[0].DNSName' --output text)
    echo "Load Balancer DNS: $ALB_DNS"
else
    TARGET_GROUP_ARN=""
    echo "Skipping load balancer configuration"
fi

# Prepare task definition JSON
echo ""
echo "Preparing task definition..."
TASK_DEF_FILE="ecs/task-definition.json"
cp "$TASK_DEF_FILE" "${TASK_DEF_FILE}.tmp"

sed -i "s|{{IMAGE_URI}}|${IMAGE_URI}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{AWS_REGION}}|${AWS_REGION}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{ACCOUNT_ID}}|${ACCOUNT_ID}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{DB_HOST}}|${DB_HOST}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{DB_PORT}}|${DB_PORT}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{DB_NAME}}|${DB_NAME}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{DB_USER}}|${DB_USER}|g" "${TASK_DEF_FILE}.tmp"
sed -i "s|{{DB_PASSWORD}}|${DB_PASSWORD}|g" "${TASK_DEF_FILE}.tmp"

# Register task definition
echo "Registering task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://"${TASK_DEF_FILE}.tmp" \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "Task Definition ARN: $TASK_DEF_ARN"

# Clean up temporary file
rm "${TASK_DEF_FILE}.tmp"

# Prepare service definition JSON
echo ""
echo "Preparing service definition..."
SERVICE_DEF_FILE="ecs/service-definition.json"
cp "$SERVICE_DEF_FILE" "${SERVICE_DEF_FILE}.tmp"

sed -i "s|{{CLUSTER_NAME}}|${CLUSTER_NAME}|g" "${SERVICE_DEF_FILE}.tmp"
sed -i "s|{{SUBNET_1}}|${SUBNET_1}|g" "${SERVICE_DEF_FILE}.tmp"
sed -i "s|{{SUBNET_2}}|${SUBNET_2}|g" "${SERVICE_DEF_FILE}.tmp"
sed -i "s|{{SECURITY_GROUP}}|${SECURITY_GROUP}|g" "${SERVICE_DEF_FILE}.tmp"

if [[ -n "$TARGET_GROUP_ARN" ]]; then
    sed -i "s|{{TARGET_GROUP_ARN}}|${TARGET_GROUP_ARN}|g" "${SERVICE_DEF_FILE}.tmp"
else
    # Remove loadBalancers section if no load balancer
    sed -i '/"loadBalancers":/,/],/d' "${SERVICE_DEF_FILE}.tmp"
    sed -i '/"healthCheckGracePeriodSeconds":/d' "${SERVICE_DEF_FILE}.tmp"
fi

# Check if service exists
echo ""
echo "Checking if service exists..."
SERVICE_EXISTS=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[0].serviceName' \
    --output text 2>/dev/null)

if [[ "$SERVICE_EXISTS" == "$SERVICE_NAME" ]]; then
    echo "Service exists. Updating service..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service "$SERVICE_NAME" \
        --task-definition "$TASK_DEF_ARN" \
        --force-new-deployment \
        --region "$AWS_REGION" >/dev/null
else
    echo "Service does not exist. Creating service..."
    aws ecs create-service \
        --cli-input-json file://"${SERVICE_DEF_FILE}.tmp" \
        --region "$AWS_REGION" >/dev/null
fi

# Clean up temporary file
rm "${SERVICE_DEF_FILE}.tmp"

# Wait for service stability
echo ""
echo "Waiting for service to become stable..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION"

# Verify deployment
echo ""
echo "======================================"
echo "Deployment Completed Successfully"
echo "======================================"
echo ""
echo "Service Details:"
aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[0].{Status:status,DesiredCount:desiredCount,RunningCount:runningCount,PendingCount:pendingCount}' \
    --output table

echo ""
echo "CloudWatch Logs: $LOG_GROUP"

if [[ -n "$ALB_DNS" ]]; then
    echo "Application URL: http://$ALB_DNS"
fi

echo ""
echo "To view logs:"
echo "  aws logs tail $LOG_GROUP --follow --region $AWS_REGION"
echo ""

#!/bin/bash
set -e
set -o pipefail

echo "=========================================="
echo "CompCRM AWS ECS Fargate Deployment Script"
echo "=========================================="
echo ""

# Project configuration
PROJECT_NAME="compcrm"
TASK_FAMILY="${PROJECT_NAME}-task"
SERVICE_NAME="${PROJECT_NAME}-service"

# Prompt for deployment configuration
echo "=== AWS Configuration ==="
read -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -p "Enter ECS Cluster Name: " CLUSTER_NAME
echo ""

echo "=== Network Configuration ==="
read -p "Enter VPC ID: " VPC_ID
read -p "Enter Subnet IDs (comma-separated, at least 2): " SUBNETS_INPUT
read -p "Enter Security Group ID: " SECURITY_GROUP
echo ""

# Convert comma-separated subnets to JSON array format
IFS=',' read -ra SUBNET_ARRAY <<< "$SUBNETS_INPUT"
SUBNET_1="${SUBNET_ARRAY[0]}"
SUBNET_2="${SUBNET_ARRAY[1]:-$SUBNET_1}"

echo "=== Container Configuration ==="
read -p "Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/compcrm:latest): " IMAGE_URI
echo ""

# Get AWS Account ID
echo "Retrieving AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo "Account ID: $ACCOUNT_ID"
echo ""

# Check/Create ECS Cluster
echo "Checking if ECS cluster exists..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating ECS cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to create ECS cluster"
        exit 1
    fi
}
echo "Cluster ready: $CLUSTER_NAME"
echo ""

# Load Balancer Configuration
echo "=== Load Balancer Configuration ==="
read -p "Do you need an Application Load Balancer for this service? (y/n): " NEED_ALB
echo ""

if [[ "$NEED_ALB" =~ ^[Yy]$ ]]; then
    echo "Creating Application Load Balancer and Target Group..."
    
    ALB_NAME="${PROJECT_NAME}-alb"
    TG_NAME="${PROJECT_NAME}-tg"
    
    # Create ALB
    echo "Creating ALB: $ALB_NAME"
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name "$ALB_NAME" \
        --subnets $SUBNET_1 $SUBNET_2 \
        --security-groups "$SECURITY_GROUP" \
        --scheme internet-facing \
        --type application \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || echo "")
    
    if [ -z "$ALB_ARN" ]; then
        # ALB might already exist, try to describe it
        ALB_ARN=$(aws elbv2 describe-load-balancers \
            --names "$ALB_NAME" \
            --region "$AWS_REGION" \
            --query 'LoadBalancers[0].LoadBalancerArn' \
            --output text 2>/dev/null || echo "")
    fi
    
    if [ -z "$ALB_ARN" ]; then
        echo "ERROR: Failed to create or find ALB"
        exit 1
    fi
    
    echo "ALB ARN: $ALB_ARN"
    
    # Create Target Group with target-type ip (required for Fargate)
    echo "Creating Target Group: $TG_NAME"
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name "$TG_NAME" \
        --protocol HTTP \
        --port 8080 \
        --vpc-id "$VPC_ID" \
        --target-type ip \
        --health-check-path "/appinfo/health" \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region "$AWS_REGION" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || echo "")
    
    if [ -z "$TARGET_GROUP_ARN" ]; then
        # TG might already exist, try to describe it
        TARGET_GROUP_ARN=$(aws elbv2 describe-target-groups \
            --names "$TG_NAME" \
            --region "$AWS_REGION" \
            --query 'TargetGroups[0].TargetGroupArn' \
            --output text 2>/dev/null || echo "")
    fi
    
    if [ -z "$TARGET_GROUP_ARN" ]; then
        echo "ERROR: Failed to create or find Target Group"
        exit 1
    fi
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    
    # Create Listener
    echo "Creating ALB Listener..."
    aws elbv2 create-listener \
        --load-balancer-arn "$ALB_ARN" \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn="$TARGET_GROUP_ARN" \
        --region "$AWS_REGION" >/dev/null 2>&1 || echo "Listener may already exist"
    
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers \
        --load-balancer-arns "$ALB_ARN" \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].DNSName' \
        --output text)
    
    echo "Load Balancer DNS: $ALB_DNS"
    echo ""
else
    TARGET_GROUP_ARN=""
fi

# Replace placeholders in task definition
echo "Preparing ECS task definition..."
cp ecs/task-definition.json ecs/task-definition-processed.json

sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" ecs/task-definition-processed.json
sed -i "s|{{AWS_REGION}}|$AWS_REGION|g" ecs/task-definition-processed.json
sed -i "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" ecs/task-definition-processed.json

# Register task definition
echo "Registering ECS task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://ecs/task-definition-processed.json \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

if [ $? -ne 0 ] || [ -z "$TASK_DEF_ARN" ]; then
    echo "ERROR: Failed to register task definition"
    exit 1
fi

echo "Task definition registered: $TASK_DEF_ARN"
echo ""

# Prepare service definition
echo "Preparing ECS service definition..."
cp ecs/service-definition.json ecs/service-definition-processed.json

sed -i "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" ecs/service-definition-processed.json
sed -i "s|{{SUBNET_1}}|$SUBNET_1|g" ecs/service-definition-processed.json
sed -i "s|{{SUBNET_2}}|$SUBNET_2|g" ecs/service-definition-processed.json
sed -i "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" ecs/service-definition-processed.json

if [ -n "$TARGET_GROUP_ARN" ]; then
    sed -i "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" ecs/service-definition-processed.json
else
    # Remove loadBalancers section if no ALB
    sed -i '/"loadBalancers":/,/],/d' ecs/service-definition-processed.json
    sed -i '/"healthCheckGracePeriodSeconds":/d' ecs/service-definition-processed.json
fi

# Check if service exists
echo "Checking if ECS service exists..."
EXISTING_SERVICE=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[0].serviceName' \
    --output text 2>/dev/null || echo "None")

if [ "$EXISTING_SERVICE" = "None" ] || [ "$EXISTING_SERVICE" = "" ]; then
    echo "Service does not exist. Creating new ECS service..."
    aws ecs create-service \
        --cluster "$CLUSTER_NAME" \
        --cli-input-json file://ecs/service-definition-processed.json \
        --region "$AWS_REGION" >/dev/null
    
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to create ECS service"
        exit 1
    fi
    echo "Service created: $SERVICE_NAME"
else
    echo "Service exists. Updating ECS service with new task definition..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service "$SERVICE_NAME" \
        --task-definition "$TASK_DEF_ARN" \
        --force-new-deployment \
        --region "$AWS_REGION" >/dev/null
    
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to update ECS service"
        exit 1
    fi
    echo "Service updated: $SERVICE_NAME"
fi

echo ""
echo "Waiting for service to stabilize..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION"

if [ $? -ne 0 ]; then
    echo "WARNING: Service did not stabilize within timeout period"
    echo "Check AWS console for service status"
else
    echo "Service is stable"
fi

echo ""
echo "=========================================="
echo "DEPLOYMENT SUCCESSFUL!"
echo "=========================================="
echo "Cluster: $CLUSTER_NAME"
echo "Service: $SERVICE_NAME"
echo "Task Definition: $TASK_DEF_ARN"
if [ -n "$ALB_DNS" ]; then
    echo "Application URL: http://$ALB_DNS"
fi
echo "CloudWatch Logs: /ecs/$PROJECT_NAME"
echo ""
echo "Verify deployment:"
echo "  aws ecs describe-services --cluster $CLUSTER_NAME --services $SERVICE_NAME --region $AWS_REGION"
echo "=========================================="

# Cleanup
rm -f ecs/task-definition-processed.json ecs/service-definition-processed.json

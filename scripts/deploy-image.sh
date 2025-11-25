#!/bin/bash
set -e
set -o pipefail

echo "========================================"
echo "AWS ECS Fargate Deployment Script"
echo "========================================"
echo ""

# Project configuration
PROJECT_NAME="crmcomptest"
TASK_FAMILY="${PROJECT_NAME}-task"
SERVICE_NAME="${PROJECT_NAME}-service"

# Prompt for configuration
read -r -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -r -p "Enter ECS Cluster Name: " CLUSTER_NAME
read -r -p "Enter VPC ID: " VPC_ID
read -r -p "Enter Subnet IDs (comma-separated, at least 2): " SUBNETS_INPUT
read -r -p "Enter Security Group ID: " SECURITY_GROUP
read -r -p "Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/app:latest): " IMAGE_URI

# Convert comma-separated subnets to array
IFS=',' read -ra SUBNETS <<< "$SUBNETS_INPUT"
SUBNET_1=${SUBNETS[0]}
SUBNET_2=${SUBNETS[1]:-$SUBNET_1}

# Get AWS Account ID
echo ""
echo "Retrieving AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo "Account ID: $ACCOUNT_ID"

# Check/create ECS cluster
echo ""
echo "Checking ECS cluster..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
}

# Create CloudWatch log group
echo ""
echo "Creating CloudWatch log group..."
LOG_GROUP="/ecs/${PROJECT_NAME}"
aws logs create-log-group --log-group-name "$LOG_GROUP" --region "$AWS_REGION" 2>/dev/null || echo "Log group already exists"

# Load balancer configuration
read -r -p "Do you need a load balancer for this service? (y/n): " USE_LB

if [[ "$USE_LB" =~ ^[Yy]$ ]]; then
    echo ""
    echo "Creating Application Load Balancer..."
    
    ALB_NAME="${PROJECT_NAME}-alb"
    TG_NAME="${PROJECT_NAME}-tg"
    
    # Create ALB
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name "$ALB_NAME" \
        --subnets ${SUBNETS[@]} \
        --security-groups "$SECURITY_GROUP" \
        --scheme internet-facing \
        --type application \
        --ip-address-type ipv4 \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text 2>/dev/null || aws elbv2 describe-load-balancers --names "$ALB_NAME" --region "$AWS_REGION" --query 'LoadBalancers[0].LoadBalancerArn' --output text)
    
    echo "ALB ARN: $ALB_ARN"
    
    # Create Target Group with target-type ip (required for Fargate)
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name "$TG_NAME" \
        --protocol HTTP \
        --port 8080 \
        --vpc-id "$VPC_ID" \
        --target-type ip \
        --health-check-enabled \
        --health-check-path "/health" \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region "$AWS_REGION" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || aws elbv2 describe-target-groups --names "$TG_NAME" --region "$AWS_REGION" --query 'TargetGroups[0].TargetGroupArn' --output text)
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    
    # Create listener
    aws elbv2 create-listener \
        --load-balancer-arn "$ALB_ARN" \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn="$TARGET_GROUP_ARN" \
        --region "$AWS_REGION" 2>/dev/null || echo "Listener already exists"
    
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers --load-balancer-arns "$ALB_ARN" --region "$AWS_REGION" --query 'LoadBalancers[0].DNSName' --output text)
    
    LOAD_BALANCER_CONFIG=",\"loadBalancers\":[{\"targetGroupArn\":\"$TARGET_GROUP_ARN\",\"containerName\":\"${PROJECT_NAME}\",\"containerPort\":8080}],\"healthCheckGracePeriodSeconds\":300"
else
    LOAD_BALANCER_CONFIG=""
fi

# Replace placeholders in task definition
echo ""
echo "Preparing task definition..."
cp ecs/task-definition.json ecs/task-definition-resolved.json
sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" ecs/task-definition-resolved.json
sed -i "s|{{AWS_REGION}}|$AWS_REGION|g" ecs/task-definition-resolved.json
sed -i "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" ecs/task-definition-resolved.json

# Register task definition
echo ""
echo "Registering task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://ecs/task-definition-resolved.json \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "Task Definition ARN: $TASK_DEF_ARN"

# Prepare service definition
echo ""
echo "Preparing service definition..."
cp ecs/service-definition.json ecs/service-definition-resolved.json
sed -i "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" ecs/service-definition-resolved.json
sed -i "s|{{SUBNET_1}}|$SUBNET_1|g" ecs/service-definition-resolved.json
sed -i "s|{{SUBNET_2}}|$SUBNET_2|g" ecs/service-definition-resolved.json
sed -i "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" ecs/service-definition-resolved.json

# Check if service exists
echo ""
echo "Checking if service exists..."
EXISTING_SERVICE=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[?status==`ACTIVE`].serviceName' \
    --output text)

if [ -z "$EXISTING_SERVICE" ] || [ "$EXISTING_SERVICE" = "None" ]; then
    echo "Creating new service..."
    
    # Add load balancer config if needed
    if [[ "$USE_LB" =~ ^[Yy]$ ]]; then
        # Inject load balancer configuration into service definition
        python3 -c "
import json
import sys
with open('ecs/service-definition-resolved.json', 'r') as f:
    svc = json.load(f)
svc['loadBalancers'] = [{'targetGroupArn': '$TARGET_GROUP_ARN', 'containerName': '${PROJECT_NAME}', 'containerPort': 8080}]
svc['healthCheckGracePeriodSeconds'] = 300
with open('ecs/service-definition-resolved.json', 'w') as f:
    json.dump(svc, f, indent=2)
" || echo "Python not available, using manual injection"
    fi
    
    aws ecs create-service \
        --cli-input-json file://ecs/service-definition-resolved.json \
        --region "$AWS_REGION"
else
    echo "Service exists. Updating service with new task definition..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service "$SERVICE_NAME" \
        --task-definition "$TASK_DEF_ARN" \
        --region "$AWS_REGION"
fi

# Wait for service stability
echo ""
echo "Waiting for service to become stable..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION"

# Verify deployment
echo ""
echo "========================================"
echo "Deployment Complete!"
echo "========================================"
echo ""
echo "Service Details:"
aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[0].[serviceName,status,runningCount,desiredCount]' \
    --output table

if [[ "$USE_LB" =~ ^[Yy]$ ]]; then
    echo ""
    echo "Application URL: http://$ALB_DNS"
fi

echo ""
echo "CloudWatch Logs: $LOG_GROUP"
echo ""
echo "To view logs, run:"
echo "aws logs tail $LOG_GROUP --follow --region $AWS_REGION"
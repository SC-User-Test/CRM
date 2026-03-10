#!/bin/bash
# === deploy-image.sh ===
# Deploy CompappCRM to AWS ECS Fargate

set -e
set -o pipefail

echo "==========================================="
echo "CompappCRM AWS ECS Fargate Deployment"
echo "==========================================="
echo ""

# Configuration
SERVICE_NAME="compappcrm-service"
TASK_FAMILY="compappcrm-task"
CONTAINER_NAME="compappcrm"
APP_PORT=8080

# Prompt for deployment configuration
echo "AWS ECS Configuration"
echo "---------------------"
read -r -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -r -p "Enter ECS Cluster Name: " CLUSTER_NAME
read -r -p "Enter Docker Image URI: " IMAGE_URI

echo ""
echo "Network Configuration"
echo "---------------------"
read -r -p "Enter VPC ID: " VPC_ID
read -r -p "Enter Subnet IDs (comma-separated, min 2): " SUBNETS_INPUT
read -r -p "Enter Security Group ID: " SECURITY_GROUP

# Parse subnets
IFS=',' read -ra SUBNET_ARRAY <<< "$SUBNETS_INPUT"
SUBNET_1="${SUBNET_ARRAY[0]}"
SUBNET_2="${SUBNET_ARRAY[1]:-$SUBNET_1}"

# Trim whitespace
SUBNET_1=$(echo "$SUBNET_1" | xargs)
SUBNET_2=$(echo "$SUBNET_2" | xargs)

echo ""
echo "Application Configuration"
echo "-------------------------"
read -r -p "Do you need a load balancer for this service? (y/n): " NEEDS_LB

if [[ "$NEEDS_LB" =~ ^[Yy]$ ]]; then
    echo ""
    echo "Creating Application Load Balancer and Target Group..."
    
    # Create target group with target-type ip (required for Fargate awsvpc)
    TARGET_GROUP_NAME="${TASK_FAMILY}-tg"
    
    echo "Creating target group: $TARGET_GROUP_NAME"
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name "$TARGET_GROUP_NAME" \
        --protocol HTTP \
        --port "$APP_PORT" \
        --vpc-id "$VPC_ID" \
        --target-type ip \
        --health-check-enabled \
        --health-check-path "/appinfo/health" \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region "$AWS_REGION" \
        --query 'TargetGroups[0].TargetGroupArn' \
        --output text 2>/dev/null || \
        aws elbv2 describe-target-groups \
            --names "$TARGET_GROUP_NAME" \
            --region "$AWS_REGION" \
            --query 'TargetGroups[0].TargetGroupArn' \
            --output text)
    
    echo "Target Group ARN: $TARGET_GROUP_ARN"
    USE_LOAD_BALANCER="true"
else
    USE_LOAD_BALANCER="false"
    echo "Skipping load balancer configuration."
fi

echo ""
echo "Getting AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo "Account ID: $ACCOUNT_ID"

echo ""
echo "Checking if ECS cluster exists..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Cluster does not exist. Creating cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
}

echo ""
echo "Preparing task definition..."
cd "$(dirname "$0")/.."

# Replace placeholders in task definition
sed -e "s|{{IMAGE_URI}}|$IMAGE_URI|g" \
    -e "s|{{AWS_REGION}}|$AWS_REGION|g" \
    -e "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" \
    ecs/task-definition.json > ecs/task-definition-resolved.json

echo "Registering task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://ecs/task-definition-resolved.json \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

echo "Task Definition ARN: $TASK_DEF_ARN"

echo ""
echo "Preparing service definition..."

# Prepare service definition with conditional load balancer
if [ "$USE_LOAD_BALANCER" = "true" ]; then
    # Include load balancer configuration
    sed -e "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" \
        -e "s|{{SUBNET_1}}|$SUBNET_1|g" \
        -e "s|{{SUBNET_2}}|$SUBNET_2|g" \
        -e "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" \
        -e "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" \
        ecs/service-definition.json > ecs/service-definition-resolved.json
else
    # Remove load balancer section
    sed -e "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" \
        -e "s|{{SUBNET_1}}|$SUBNET_1|g" \
        -e "s|{{SUBNET_2}}|$SUBNET_2|g" \
        -e "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" \
        ecs/service-definition.json | \
        jq 'del(.loadBalancers) | del(.healthCheckGracePeriodSeconds)' > ecs/service-definition-resolved.json
fi

echo "Checking if service exists..."
EXISTING_SERVICE=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION" \
    --query 'services[?status==`ACTIVE`].serviceName' \
    --output text 2>/dev/null || echo "")

if [ -n "$EXISTING_SERVICE" ] && [ "$EXISTING_SERVICE" != "None" ]; then
    echo "Service exists. Updating service..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service "$SERVICE_NAME" \
        --task-definition "$TASK_DEF_ARN" \
        --force-new-deployment \
        --region "$AWS_REGION"
else
    echo "Service does not exist. Creating service..."
    aws ecs create-service \
        --cli-input-json file://ecs/service-definition-resolved.json \
        --region "$AWS_REGION"
fi

echo ""
echo "Waiting for service to stabilize..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services "$SERVICE_NAME" \
    --region "$AWS_REGION"

echo ""
echo "==========================================="
echo "DEPLOYMENT SUCCESSFUL!"
echo "==========================================="
echo "Cluster: $CLUSTER_NAME"
echo "Service: $SERVICE_NAME"
echo "Task Definition: $TASK_DEF_ARN"
echo ""

if [ "$USE_LOAD_BALANCER" = "true" ]; then
    echo "Load Balancer Target Group: $TARGET_GROUP_ARN"
    echo "Note: Configure your ALB listener to forward traffic to this target group."
    echo ""
fi

echo "View logs in CloudWatch:"
echo "Log Group: /ecs/compappcrm"
echo "Region: $AWS_REGION"
echo ""
echo "Verify deployment:"
echo "aws ecs describe-services --cluster $CLUSTER_NAME --services $SERVICE_NAME --region $AWS_REGION"
echo "==========================================="

# Cleanup temporary files
rm -f ecs/task-definition-resolved.json ecs/service-definition-resolved.json
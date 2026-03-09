#!/bin/bash
set -e
set -o pipefail

# ECS Fargate Deployment Script for CRM Application
echo "=========================================="
echo "CRM Application - ECS Fargate Deployment"
echo "=========================================="
echo ""

# Check AWS CLI installation
if ! command -v aws &> /dev/null; then
    echo "ERROR: AWS CLI is not installed. Please install it first."
    exit 1
fi

# Prompt for AWS configuration
echo "=== AWS Configuration ==="
read -r -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
read -r -p "Enter ECS Cluster Name: " CLUSTER_NAME
echo ""

# Get AWS Account ID
echo "Retrieving AWS Account ID..."
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text --region "$AWS_REGION")
if [ -z "$ACCOUNT_ID" ]; then
    echo "ERROR: Failed to retrieve AWS Account ID. Check your AWS credentials."
    exit 1
fi
echo "AWS Account ID: $ACCOUNT_ID"
echo ""

# Check/Create ECS Cluster
echo "Checking ECS cluster..."
aws ecs describe-clusters --clusters "$CLUSTER_NAME" --region "$AWS_REGION" >/dev/null 2>&1 || {
    echo "Creating ECS cluster: $CLUSTER_NAME"
    aws ecs create-cluster --cluster-name "$CLUSTER_NAME" --region "$AWS_REGION"
    echo "Cluster created successfully."
}
echo ""

# Network Configuration
echo "=== Network Configuration ==="
read -r -p "Enter VPC ID: " VPC_ID
read -r -p "Enter Subnet IDs (comma-separated, e.g., subnet-xxx,subnet-yyy): " SUBNETS_INPUT
read -r -p "Enter Security Group ID: " SECURITY_GROUP
echo ""

# Convert comma-separated subnets to array
IFS=',' read -ra SUBNETS <<< "$SUBNETS_INPUT"
SUBNET_1="${SUBNETS[0]}"
SUBNET_2="${SUBNETS[1]:-$SUBNET_1}"

# Image Configuration
echo "=== Docker Image Configuration ==="
read -r -p "Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/crm:latest): " IMAGE_URI
echo ""

# Database Configuration
echo "=== Database Configuration ==="
read -r -p "Enter Database Host: " DB_HOST
read -r -p "Enter Database Port (default: 3306): " DB_PORT
DB_PORT=${DB_PORT:-3306}
read -r -p "Enter Database Name (default: crm): " DB_NAME
DB_NAME=${DB_NAME:-crm}
read -r -p "Enter Database Username: " DB_USERNAME
read -r -s -p "Enter Database Password: " DB_PASSWORD
echo ""
echo ""

# Load Balancer Configuration
read -r -p "Do you need a load balancer for this service? (y/n): " NEEDS_LB
echo ""

if [[ "$NEEDS_LB" =~ ^[Yy]$ ]]; then
    echo "=== Creating Application Load Balancer ==="
    
    # Create ALB
    ALB_NAME="crm-alb-$(date +%s)"
    echo "Creating Application Load Balancer: $ALB_NAME"
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name "$ALB_NAME" \
        --subnets "${SUBNETS[@]}" \
        --security-groups "$SECURITY_GROUP" \
        --scheme internet-facing \
        --type application \
        --ip-address-type ipv4 \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].LoadBalancerArn' \
        --output text)
    
    echo "ALB created: $ALB_ARN"
    
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers \
        --load-balancer-arns "$ALB_ARN" \
        --region "$AWS_REGION" \
        --query 'LoadBalancers[0].DNSName' \
        --output text)
    
    # Create Target Group with target-type ip (required for Fargate awsvpc)
    TG_NAME="crm-tg-$(date +%s)"
    echo "Creating Target Group: $TG_NAME"
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
        --output text)
    
    echo "Target Group created: $TARGET_GROUP_ARN"
    
    # Create Listener
    echo "Creating ALB Listener..."
    aws elbv2 create-listener \
        --load-balancer-arn "$ALB_ARN" \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn="$TARGET_GROUP_ARN" \
        --region "$AWS_REGION" >/dev/null
    
    echo "Load Balancer setup completed."
    echo ""
else
    echo "Skipping load balancer creation."
    TARGET_GROUP_ARN=""
    echo ""
fi

# Create CloudWatch Log Group
echo "Creating CloudWatch Log Group..."
aws logs create-log-group --log-group-name "/ecs/crm-task" --region "$AWS_REGION" 2>/dev/null || echo "Log group already exists."
echo ""

# Update task definition with placeholders
echo "Updating task definition..."
cp ecs/task-definition.json ecs/task-definition-deploy.json
sed -i.bak "s|{{IMAGE_URI}}|$IMAGE_URI|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{AWS_REGION}}|$AWS_REGION|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{ACCOUNT_ID}}|$ACCOUNT_ID|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{DB_HOST}}|$DB_HOST|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{DB_PORT}}|$DB_PORT|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{DB_NAME}}|$DB_NAME|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{DB_USERNAME}}|$DB_USERNAME|g" ecs/task-definition-deploy.json
sed -i.bak "s|{{DB_PASSWORD}}|$DB_PASSWORD|g" ecs/task-definition-deploy.json

# Register task definition
echo "Registering ECS task definition..."
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file://ecs/task-definition-deploy.json \
    --region "$AWS_REGION" \
    --query 'taskDefinition.taskDefinitionArn' \
    --output text)

if [ -z "$TASK_DEF_ARN" ]; then
    echo "ERROR: Failed to register task definition."
    exit 1
fi
echo "Task definition registered: $TASK_DEF_ARN"
echo ""

# Update service definition
echo "Updating service definition..."
cp ecs/service-definition.json ecs/service-definition-deploy.json
sed -i.bak "s|{{CLUSTER_NAME}}|$CLUSTER_NAME|g" ecs/service-definition-deploy.json
sed -i.bak "s|{{SUBNET_1}}|$SUBNET_1|g" ecs/service-definition-deploy.json
sed -i.bak "s|{{SUBNET_2}}|$SUBNET_2|g" ecs/service-definition-deploy.json
sed -i.bak "s|{{SECURITY_GROUP}}|$SECURITY_GROUP|g" ecs/service-definition-deploy.json

if [ -n "$TARGET_GROUP_ARN" ]; then
    sed -i.bak "s|{{TARGET_GROUP_ARN}}|$TARGET_GROUP_ARN|g" ecs/service-definition-deploy.json
else
    # Remove loadBalancers section if no LB
    sed -i.bak '/"loadBalancers"/,/],/d' ecs/service-definition-deploy.json
    sed -i.bak '/"healthCheckGracePeriodSeconds"/d' ecs/service-definition-deploy.json
fi

# Check if service exists
echo "Checking if service exists..."
SERVICE_EXISTS=$(aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services crm-service \
    --region "$AWS_REGION" \
    --query 'services[0].serviceName' \
    --output text 2>/dev/null)

if [ "$SERVICE_EXISTS" = "crm-service" ]; then
    echo "Service exists. Updating service..."
    aws ecs update-service \
        --cluster "$CLUSTER_NAME" \
        --service crm-service \
        --task-definition "$TASK_DEF_ARN" \
        --desired-count 2 \
        --region "$AWS_REGION" >/dev/null
    echo "Service updated successfully."
else
    echo "Service does not exist. Creating new service..."
    aws ecs create-service \
        --cli-input-json file://ecs/service-definition-deploy.json \
        --region "$AWS_REGION" >/dev/null
    echo "Service created successfully."
fi
echo ""

# Wait for service stability
echo "Waiting for service to become stable (this may take several minutes)..."
aws ecs wait services-stable \
    --cluster "$CLUSTER_NAME" \
    --services crm-service \
    --region "$AWS_REGION"

echo "Service is stable."
echo ""

# Verify deployment
echo "=== Deployment Summary ==="
aws ecs describe-services \
    --cluster "$CLUSTER_NAME" \
    --services crm-service \
    --region "$AWS_REGION" \
    --query 'services[0].{Status:status,Running:runningCount,Desired:desiredCount}' \
    --output table

if [ -n "$ALB_DNS" ]; then
    echo ""
    echo "Application URL: http://$ALB_DNS"
fi

echo ""
echo "CloudWatch Logs: /ecs/crm-task"
echo ""
echo "=========================================="
echo "Deployment completed successfully!"
echo "=========================================="

# Cleanup temporary files
rm -f ecs/task-definition-deploy.json ecs/task-definition-deploy.json.bak
rm -f ecs/service-definition-deploy.json ecs/service-definition-deploy.json.bak
#!/bin/bash
set -e
set -o pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}CRM Application - AWS ECS Fargate Deployment${NC}"
echo "============================================="
echo ""

# Check if AWS CLI is installed and configured
if ! command -v aws &> /dev/null; then
    echo -e "${RED}AWS CLI is not installed. Please install it first.${NC}"
    exit 1
fi

# Test AWS credentials
aws sts get-caller-identity >/dev/null 2>&1 || {
    echo -e "${RED}AWS credentials not configured. Please run 'aws configure' first.${NC}"
    exit 1
}

# Get AWS Account ID
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
echo -e "${YELLOW}AWS Account ID: $ACCOUNT_ID${NC}"

# Prompt for deployment configuration
read -p "Enter AWS region (default: us-east-1): " AWS_REGION
if [ -z "$AWS_REGION" ]; then
    AWS_REGION="us-east-1"
fi

read -p "Enter ECS cluster name (default: crm-cluster): " CLUSTER_NAME
if [ -z "$CLUSTER_NAME" ]; then
    CLUSTER_NAME="crm-cluster"
fi

read -p "Enter VPC ID: " VPC_ID
if [ -z "$VPC_ID" ]; then
    echo -e "${RED}VPC ID is required for Fargate deployment${NC}"
    exit 1
fi

read -p "Enter subnet IDs (comma-separated, at least 2): " SUBNETS_INPUT
if [ -z "$SUBNETS_INPUT" ]; then
    echo -e "${RED}At least 2 subnet IDs are required for high availability${NC}"
    exit 1
fi

# Parse subnets
IFS=',' read -ra SUBNETS_ARRAY <<< "$SUBNETS_INPUT"
SUBNET_1=$(echo "${SUBNETS_ARRAY[0]}" | xargs)
SUBNET_2=$(echo "${SUBNETS_ARRAY[1]}" | xargs)

if [ -z "$SUBNET_1" ] || [ -z "$SUBNET_2" ]; then
    echo -e "${RED}At least 2 valid subnet IDs are required${NC}"
    exit 1
fi

read -p "Enter security group ID: " SECURITY_GROUP
if [ -z "$SECURITY_GROUP" ]; then
    echo -e "${RED}Security group ID is required${NC}"
    exit 1
fi

read -p "Enter ECR image URI: " IMAGE_URI
if [ -z "$IMAGE_URI" ]; then
    echo -e "${RED}ECR image URI is required${NC}"
    exit 1
fi

# Database configuration
read -p "Enter database host: " DB_HOST
read -p "Enter database port (default: 3306): " DB_PORT
if [ -z "$DB_PORT" ]; then
    DB_PORT="3306"
fi

read -p "Enter database name (default: crm): " DB_NAME
if [ -z "$DB_NAME" ]; then
    DB_NAME="crm"
fi

read -p "Enter database username: " DB_USER
read -s -p "Enter database password: " DB_PASSWORD
echo ""

if [ -z "$DB_HOST" ] || [ -z "$DB_USER" ] || [ -z "$DB_PASSWORD" ]; then
    echo -e "${RED}Database configuration is required${NC}"
    exit 1
fi

echo ""
echo -e "${YELLOW}Deployment Configuration:${NC}"
echo "Region: $AWS_REGION"
echo "Cluster: $CLUSTER_NAME"
echo "VPC: $VPC_ID"
echo "Subnets: $SUBNET_1, $SUBNET_2"
echo "Security Group: $SECURITY_GROUP"
echo "Image: $IMAGE_URI"
echo "Database: $DB_HOST:$DB_PORT/$DB_NAME"
echo ""

# Check/create ECS cluster
echo -e "${YELLOW}Checking ECS cluster...${NC}"
aws ecs describe-clusters --clusters $CLUSTER_NAME --region $AWS_REGION >/dev/null 2>&1 || {
    echo -e "${YELLOW}Creating ECS cluster: $CLUSTER_NAME${NC}"
    aws ecs create-cluster --cluster-name $CLUSTER_NAME --region $AWS_REGION
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to create ECS cluster${NC}"
        exit 1
    fi
}

# Create CloudWatch log group
echo -e "${YELLOW}Creating CloudWatch log group...${NC}"
aws logs create-log-group --log-group-name "/ecs/crm" --region $AWS_REGION >/dev/null 2>&1 || true

# Load balancer configuration
read -p "Do you need a load balancer for this service? (y/n): " NEED_LB
if [[ $NEED_LB =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}Creating Application Load Balancer...${NC}"
    
    # Create ALB
    ALB_ARN=$(aws elbv2 create-load-balancer \
        --name crm-alb \
        --subnets $SUBNET_1 $SUBNET_2 \
        --security-groups $SECURITY_GROUP \
        --region $AWS_REGION \
        --query 'LoadBalancers[0].LoadBalancerArn' --output text)
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to create load balancer${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}Created ALB: $ALB_ARN${NC}"
    
    # Create target group
    TARGET_GROUP_ARN=$(aws elbv2 create-target-group \
        --name crm-tg \
        --protocol HTTP \
        --port 8080 \
        --vpc-id $VPC_ID \
        --target-type ip \
        --health-check-path /appinfo/health \
        --health-check-interval-seconds 30 \
        --health-check-timeout-seconds 5 \
        --healthy-threshold-count 2 \
        --unhealthy-threshold-count 3 \
        --region $AWS_REGION \
        --query 'TargetGroups[0].TargetGroupArn' --output text)
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to create target group${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}Created Target Group: $TARGET_GROUP_ARN${NC}"
    
    # Create listener
    aws elbv2 create-listener \
        --load-balancer-arn $ALB_ARN \
        --protocol HTTP \
        --port 80 \
        --default-actions Type=forward,TargetGroupArn=$TARGET_GROUP_ARN \
        --region $AWS_REGION >/dev/null
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to create listener${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}Created ALB Listener${NC}"
else
    TARGET_GROUP_ARN=""
fi

# Create temporary directory for JSON files
mkdir -p /tmp/ecs-deploy

# Update task definition JSON
cat > /tmp/ecs-deploy/task-definition.json << EOF
{
  "family": "crm-task",
  "requiresCompatibilities": ["FARGATE"],
  "networkMode": "awsvpc",
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::$ACCOUNT_ID:role/ecsTaskExecutionRole",
  "taskRoleArn": "arn:aws:iam::$ACCOUNT_ID:role/ecsTaskRole",
  "containerDefinitions": [
    {
      "name": "crm",
      "image": "$IMAGE_URI",
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "JAVA_OPTS",
          "value": "-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
        },
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "docker"
        },
        {
          "name": "TZ",
          "value": "UTC"
        },
        {
          "name": "DB_HOST",
          "value": "$DB_HOST"
        },
        {
          "name": "DB_PORT",
          "value": "$DB_PORT"
        },
        {
          "name": "DB_NAME",
          "value": "$DB_NAME"
        },
        {
          "name": "DB_USER",
          "value": "$DB_USER"
        },
        {
          "name": "DB_PASSWORD",
          "value": "$DB_PASSWORD"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/crm",
          "awslogs-region": "$AWS_REGION",
          "awslogs-stream-prefix": "ecs"
        }
      },
      "healthCheck": {
        "command": ["CMD-SHELL", "curl -f http://localhost:8080/appinfo/health || exit 1"],
        "interval": 30,
        "timeout": 5,
        "retries": 3,
        "startPeriod": 60
      }
    }
  ]
}
EOF

# Create service definition JSON
if [ -n "$TARGET_GROUP_ARN" ]; then
    # With load balancer
    cat > /tmp/ecs-deploy/service-definition.json << EOF
{
  "serviceName": "crm-service",
  "cluster": "$CLUSTER_NAME",
  "taskDefinition": "crm-task",
  "desiredCount": 2,
  "launchType": "FARGATE",
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": ["$SUBNET_1", "$SUBNET_2"],
      "securityGroups": ["$SECURITY_GROUP"],
      "assignPublicIp": "ENABLED"
    }
  },
  "deploymentConfiguration": {
    "maximumPercent": 200,
    "minimumHealthyPercent": 50
  },
  "loadBalancers": [
    {
      "targetGroupArn": "$TARGET_GROUP_ARN",
      "containerName": "crm",
      "containerPort": 8080
    }
  ],
  "healthCheckGracePeriodSeconds": 300,
  "enableECSManagedTags": true,
  "propagateTags": "SERVICE",
  "tags": [
    {
      "key": "Environment",
      "value": "production"
    },
    {
      "key": "Application",
      "value": "crm"
    }
  ]
}
EOF
else
    # Without load balancer
    cat > /tmp/ecs-deploy/service-definition.json << EOF
{
  "serviceName": "crm-service",
  "cluster": "$CLUSTER_NAME",
  "taskDefinition": "crm-task",
  "desiredCount": 2,
  "launchType": "FARGATE",
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": ["$SUBNET_1", "$SUBNET_2"],
      "securityGroups": ["$SECURITY_GROUP"],
      "assignPublicIp": "ENABLED"
    }
  },
  "deploymentConfiguration": {
    "maximumPercent": 200,
    "minimumHealthyPercent": 50
  },
  "enableECSManagedTags": true,
  "propagateTags": "SERVICE",
  "tags": [
    {
      "key": "Environment",
      "value": "production"
    },
    {
      "key": "Application",
      "value": "crm"
    }
  ]
}
EOF
fi

# Register task definition
echo -e "${YELLOW}Registering task definition...${NC}"
TASK_DEF_ARN=$(aws ecs register-task-definition \
    --cli-input-json file:///tmp/ecs-deploy/task-definition.json \
    --region $AWS_REGION \
    --query 'taskDefinition.taskDefinitionArn' --output text)

if [ $? -ne 0 ]; then
    echo -e "${RED}Failed to register task definition${NC}"
    exit 1
fi

echo -e "${GREEN}Registered task definition: $TASK_DEF_ARN${NC}"

# Check if service exists
echo -e "${YELLOW}Checking if service exists...${NC}"
SERVICE_EXISTS=$(aws ecs describe-services \
    --cluster $CLUSTER_NAME \
    --services crm-service \
    --region $AWS_REGION \
    --query 'length(services[?serviceName==`crm-service` && status!=`INACTIVE`])' --output text)

if [ "$SERVICE_EXISTS" = "0" ]; then
    # Create service
    echo -e "${YELLOW}Creating ECS service...${NC}"
    aws ecs create-service \
        --cli-input-json file:///tmp/ecs-deploy/service-definition.json \
        --region $AWS_REGION
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to create service${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}Created ECS service: crm-service${NC}"
else
    # Update service
    echo -e "${YELLOW}Updating existing ECS service...${NC}"
    aws ecs update-service \
        --cluster $CLUSTER_NAME \
        --service crm-service \
        --task-definition $TASK_DEF_ARN \
        --region $AWS_REGION
    
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to update service${NC}"
        exit 1
    fi
    
    echo -e "${GREEN}Updated ECS service: crm-service${NC}"
fi

# Wait for service to stabilize
echo -e "${YELLOW}Waiting for service to stabilize (this may take a few minutes)...${NC}"
aws ecs wait services-stable --cluster $CLUSTER_NAME --services crm-service --region $AWS_REGION

if [ $? -ne 0 ]; then
    echo -e "${RED}Service deployment timed out or failed${NC}"
    echo "Check the ECS console for more details."
    exit 1
fi

# Verify deployment
echo -e "${YELLOW}Verifying deployment...${NC}"
RUNNING_TASKS=$(aws ecs describe-services \
    --cluster $CLUSTER_NAME \
    --services crm-service \
    --region $AWS_REGION \
    --query 'services[0].runningCount' --output text)

echo -e "${GREEN}Deployment completed successfully!${NC}"
echo ""
echo "Deployment Summary:"
echo "=================="
echo "Cluster: $CLUSTER_NAME"
echo "Service: crm-service"
echo "Running Tasks: $RUNNING_TASKS"
echo "Task Definition: $TASK_DEF_ARN"
echo "CloudWatch Logs: /ecs/crm"

if [ -n "$TARGET_GROUP_ARN" ]; then
    # Get ALB DNS name
    ALB_DNS=$(aws elbv2 describe-load-balancers \
        --load-balancer-arns $ALB_ARN \
        --region $AWS_REGION \
        --query 'LoadBalancers[0].DNSName' --output text)
    
    echo "Load Balancer: http://$ALB_DNS"
    echo "Health Check: http://$ALB_DNS/appinfo/health"
fi

echo ""
echo "Useful commands:"
echo "- View service: aws ecs describe-services --cluster $CLUSTER_NAME --services crm-service --region $AWS_REGION"
echo "- View logs: aws logs tail /ecs/crm --follow --region $AWS_REGION"
echo "- Scale service: aws ecs update-service --cluster $CLUSTER_NAME --service crm-service --desired-count 3 --region $AWS_REGION"

# Cleanup temp files
rm -rf /tmp/ecs-deploy

echo -e "${GREEN}Deployment script completed!${NC}"
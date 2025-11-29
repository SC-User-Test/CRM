@echo off
setlocal enabledelayedexpansion

echo CRM Application - AWS ECS Fargate Deployment
echo =============================================
echo.

:: Check if AWS CLI is installed
aws --version >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo AWS CLI is not installed. Please install it first.
    exit /b 1
)

:: Test AWS credentials
aws sts get-caller-identity >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo AWS credentials not configured. Please run 'aws configure' first.
    exit /b 1
)

:: Get AWS Account ID
for /f "tokens=*" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
echo AWS Account ID: !ACCOUNT_ID!

:: Prompt for deployment configuration
set /p AWS_REGION="Enter AWS region (default: us-east-1): "
if "!AWS_REGION!"=="" set AWS_REGION=us-east-1

set /p CLUSTER_NAME="Enter ECS cluster name (default: crm-cluster): "
if "!CLUSTER_NAME!"=="" set CLUSTER_NAME=crm-cluster

set /p VPC_ID="Enter VPC ID: "
if "!VPC_ID!"=="" (
    echo VPC ID is required for Fargate deployment
    exit /b 1
)

set /p SUBNETS_INPUT="Enter subnet IDs (comma-separated, at least 2): "
if "!SUBNETS_INPUT!"=="" (
    echo At least 2 subnet IDs are required for high availability
    exit /b 1
)

:: Parse first two subnets (basic parsing for batch)
for /f "tokens=1,2 delims=," %%a in ("!SUBNETS_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)

:: Trim whitespace
for /f "tokens=* delims= " %%a in ("!SUBNET_1!") do set SUBNET_1=%%a
for /f "tokens=* delims= " %%a in ("!SUBNET_2!") do set SUBNET_2=%%a

if "!SUBNET_1!"=="" (
    echo At least 2 valid subnet IDs are required
    exit /b 1
)
if "!SUBNET_2!"=="" (
    echo At least 2 valid subnet IDs are required
    exit /b 1
)

set /p SECURITY_GROUP="Enter security group ID: "
if "!SECURITY_GROUP!"=="" (
    echo Security group ID is required
    exit /b 1
)

set /p IMAGE_URI="Enter ECR image URI: "
if "!IMAGE_URI!"=="" (
    echo ECR image URI is required
    exit /b 1
)

:: Database configuration
set /p DB_HOST="Enter database host: "
set /p DB_PORT="Enter database port (default: 3306): "
if "!DB_PORT!"=="" set DB_PORT=3306

set /p DB_NAME="Enter database name (default: crm): "
if "!DB_NAME!"=="" set DB_NAME=crm

set /p DB_USER="Enter database username: "
set /p DB_PASSWORD="Enter database password: "

if "!DB_HOST!"=="" (
    echo Database configuration is required
    exit /b 1
)
if "!DB_USER!"=="" (
    echo Database configuration is required
    exit /b 1
)
if "!DB_PASSWORD!"=="" (
    echo Database configuration is required
    exit /b 1
)

echo.
echo Deployment Configuration:
echo Region: !AWS_REGION!
echo Cluster: !CLUSTER_NAME!
echo VPC: !VPC_ID!
echo Subnets: !SUBNET_1!, !SUBNET_2!
echo Security Group: !SECURITY_GROUP!
echo Image: !IMAGE_URI!
echo Database: !DB_HOST!:!DB_PORT!/!DB_NAME!
echo.

:: Check/create ECS cluster
echo Checking ECS cluster...
aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Creating ECS cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
    if !ERRORLEVEL! neq 0 (
        echo Failed to create ECS cluster
        exit /b 1
    )
)

:: Create CloudWatch log group
echo Creating CloudWatch log group...
aws logs create-log-group --log-group-name "/ecs/crm" --region !AWS_REGION! >nul 2>&1

:: Load balancer configuration
set /p NEED_LB="Do you need a load balancer for this service? (y/n): "
if /i "!NEED_LB!"=="y" (
    echo Creating Application Load Balancer...
    
    :: Create ALB
    for /f "tokens=*" %%i in ('aws elbv2 create-load-balancer --name crm-alb --subnets !SUBNET_1! !SUBNET_2! --security-groups !SECURITY_GROUP! --region !AWS_REGION! --query "LoadBalancers[0].LoadBalancerArn" --output text') do set ALB_ARN=%%i
    
    if !ERRORLEVEL! neq 0 (
        echo Failed to create load balancer
        exit /b 1
    )
    
    echo Created ALB: !ALB_ARN!
    
    :: Create target group
    for /f "tokens=*" %%i in ('aws elbv2 create-target-group --name crm-tg --protocol HTTP --port 8080 --vpc-id !VPC_ID! --target-type ip --health-check-path /appinfo/health --health-check-interval-seconds 30 --health-check-timeout-seconds 5 --healthy-threshold-count 2 --unhealthy-threshold-count 3 --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%i
    
    if !ERRORLEVEL! neq 0 (
        echo Failed to create target group
        exit /b 1
    )
    
    echo Created Target Group: !TARGET_GROUP_ARN!
    
    :: Create listener
    aws elbv2 create-listener --load-balancer-arn !ALB_ARN! --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn=!TARGET_GROUP_ARN! --region !AWS_REGION! >nul
    
    if !ERRORLEVEL! neq 0 (
        echo Failed to create listener
        exit /b 1
    )
    
    echo Created ALB Listener
) else (
    set TARGET_GROUP_ARN=
)

:: Create temporary directory for JSON files
if not exist temp mkdir temp

:: Create task definition JSON
(
echo {
echo   "family": "crm-task",
echo   "requiresCompatibilities": ["FARGATE"],
echo   "networkMode": "awsvpc",
echo   "cpu": "512",
echo   "memory": "1024",
echo   "executionRoleArn": "arn:aws:iam::!ACCOUNT_ID!:role/ecsTaskExecutionRole",
echo   "taskRoleArn": "arn:aws:iam::!ACCOUNT_ID!:role/ecsTaskRole",
echo   "containerDefinitions": [
echo     {
echo       "name": "crm",
echo       "image": "!IMAGE_URI!",
echo       "essential": true,
echo       "portMappings": [
echo         {
echo           "containerPort": 8080,
echo           "protocol": "tcp"
echo         }
echo       ],
echo       "environment": [
echo         {
echo           "name": "JAVA_OPTS",
echo           "value": "-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"
echo         },
echo         {
echo           "name": "SPRING_PROFILES_ACTIVE",
echo           "value": "docker"
echo         },
echo         {
echo           "name": "TZ",
echo           "value": "UTC"
echo         },
echo         {
echo           "name": "DB_HOST",
echo           "value": "!DB_HOST!"
echo         },
echo         {
echo           "name": "DB_PORT",
echo           "value": "!DB_PORT!"
echo         },
echo         {
echo           "name": "DB_NAME",
echo           "value": "!DB_NAME!"
echo         },
echo         {
echo           "name": "DB_USER",
echo           "value": "!DB_USER!"
echo         },
echo         {
echo           "name": "DB_PASSWORD",
echo           "value": "!DB_PASSWORD!"
echo         }
echo       ],
echo       "logConfiguration": {
echo         "logDriver": "awslogs",
echo         "options": {
echo           "awslogs-group": "/ecs/crm",
echo           "awslogs-region": "!AWS_REGION!",
echo           "awslogs-stream-prefix": "ecs"
echo         }
echo       },
echo       "healthCheck": {
echo         "command": ["CMD-SHELL", "curl -f http://localhost:8080/appinfo/health \|\| exit 1"],
echo         "interval": 30,
echo         "timeout": 5,
echo         "retries": 3,
echo         "startPeriod": 60
echo       }
echo     }
echo   ]
echo }
) > temp\task-definition.json

:: Create service definition JSON
if "!TARGET_GROUP_ARN!" neq "" (
    :: With load balancer
    (
    echo {
    echo   "serviceName": "crm-service",
    echo   "cluster": "!CLUSTER_NAME!",
    echo   "taskDefinition": "crm-task",
    echo   "desiredCount": 2,
    echo   "launchType": "FARGATE",
    echo   "networkConfiguration": {
    echo     "awsvpcConfiguration": {
    echo       "subnets": ["!SUBNET_1!", "!SUBNET_2!"],
    echo       "securityGroups": ["!SECURITY_GROUP!"],
    echo       "assignPublicIp": "ENABLED"
    echo     }
    echo   },
    echo   "deploymentConfiguration": {
    echo     "maximumPercent": 200,
    echo     "minimumHealthyPercent": 50
    echo   },
    echo   "loadBalancers": [
    echo     {
    echo       "targetGroupArn": "!TARGET_GROUP_ARN!",
    echo       "containerName": "crm",
    echo       "containerPort": 8080
    echo     }
    echo   ],
    echo   "healthCheckGracePeriodSeconds": 300,
    echo   "enableECSManagedTags": true,
    echo   "propagateTags": "SERVICE",
    echo   "tags": [
    echo     {
    echo       "key": "Environment",
    echo       "value": "production"
    echo     },
    echo     {
    echo       "key": "Application",
    echo       "value": "crm"
    echo     }
    echo   ]
    echo }
    ) > temp\service-definition.json
) else (
    :: Without load balancer
    (
    echo {
    echo   "serviceName": "crm-service",
    echo   "cluster": "!CLUSTER_NAME!",
    echo   "taskDefinition": "crm-task",
    echo   "desiredCount": 2,
    echo   "launchType": "FARGATE",
    echo   "networkConfiguration": {
    echo     "awsvpcConfiguration": {
    echo       "subnets": ["!SUBNET_1!", "!SUBNET_2!"],
    echo       "securityGroups": ["!SECURITY_GROUP!"],
    echo       "assignPublicIp": "ENABLED"
    echo     }
    echo   },
    echo   "deploymentConfiguration": {
    echo     "maximumPercent": 200,
    echo     "minimumHealthyPercent": 50
    echo   },
    echo   "enableECSManagedTags": true,
    echo   "propagateTags": "SERVICE",
    echo   "tags": [
    echo     {
    echo       "key": "Environment",
    echo       "value": "production"
    echo     },
    echo     {
    echo       "key": "Application",
    echo       "value": "crm"
    echo     }
    echo   ]
    echo }
    ) > temp\service-definition.json
)

:: Register task definition
echo Registering task definition...
for /f "tokens=*" %%i in ('aws ecs register-task-definition --cli-input-json file://temp/task-definition.json --region !AWS_REGION! --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

if !ERRORLEVEL! neq 0 (
    echo Failed to register task definition
    exit /b 1
)

echo Registered task definition: !TASK_DEF_ARN!

:: Check if service exists
echo Checking if service exists...
for /f "tokens=*" %%i in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION! --query "length(services[?serviceName==`crm-service` && status!=`INACTIVE`])" --output text') do set SERVICE_EXISTS=%%i

if "!SERVICE_EXISTS!"=="0" (
    :: Create service
    echo Creating ECS service...
    aws ecs create-service --cli-input-json file://temp/service-definition.json --region !AWS_REGION!
    
    if !ERRORLEVEL! neq 0 (
        echo Failed to create service
        exit /b 1
    )
    
    echo Created ECS service: crm-service
) else (
    :: Update service
    echo Updating existing ECS service...
    aws ecs update-service --cluster !CLUSTER_NAME! --service crm-service --task-definition !TASK_DEF_ARN! --region !AWS_REGION!
    
    if !ERRORLEVEL! neq 0 (
        echo Failed to update service
        exit /b 1
    )
    
    echo Updated ECS service: crm-service
)

:: Wait for service to stabilize
echo Waiting for service to stabilize (this may take a few minutes)...
aws ecs wait services-stable --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION!

if !ERRORLEVEL! neq 0 (
    echo Service deployment timed out or failed
    echo Check the ECS console for more details.
    exit /b 1
)

:: Verify deployment
echo Verifying deployment...
for /f "tokens=*" %%i in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION! --query "services[0].runningCount" --output text') do set RUNNING_TASKS=%%i

echo Deployment completed successfully!
echo.
echo Deployment Summary:
echo ==================
echo Cluster: !CLUSTER_NAME!
echo Service: crm-service
echo Running Tasks: !RUNNING_TASKS!
echo Task Definition: !TASK_DEF_ARN!
echo CloudWatch Logs: /ecs/crm

if "!TARGET_GROUP_ARN!" neq "" (
    :: Get ALB DNS name
    for /f "tokens=*" %%i in ('aws elbv2 describe-load-balancers --load-balancer-arns !ALB_ARN! --region !AWS_REGION! --query "LoadBalancers[0].DNSName" --output text') do set ALB_DNS=%%i
    
    echo Load Balancer: http://!ALB_DNS!
    echo Health Check: http://!ALB_DNS!/appinfo/health
)

echo.
echo Useful commands:
echo - View service: aws ecs describe-services --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION!
echo - View logs: aws logs tail /ecs/crm --follow --region !AWS_REGION!
echo - Scale service: aws ecs update-service --cluster !CLUSTER_NAME! --service crm-service --desired-count 3 --region !AWS_REGION!

:: Cleanup temp files
rmdir /s /q temp

echo Deployment script completed!
pause
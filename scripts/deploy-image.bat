@echo off
setlocal enabledelayedexpansion

REM ECS Fargate Deployment Script for CRM Application
echo ==========================================
echo CRM Application - ECS Fargate Deployment
echo ==========================================
echo.

REM Check AWS CLI installation
where aws >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo ERROR: AWS CLI is not installed. Please install it first.
    exit /b 1
)

REM Prompt for AWS configuration
echo === AWS Configuration ===
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
echo.

REM Get AWS Account ID
echo Retrieving AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text --region !AWS_REGION!') do set ACCOUNT_ID=%%i
if "!ACCOUNT_ID!"=="" (
    echo ERROR: Failed to retrieve AWS Account ID. Check your AWS credentials.
    exit /b 1
)
echo AWS Account ID: !ACCOUNT_ID!
echo.

REM Check/Create ECS Cluster
echo Checking ECS cluster...
aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Creating ECS cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
    echo Cluster created successfully.
)
echo.

REM Network Configuration
echo === Network Configuration ===
set /p VPC_ID="Enter VPC ID: "
set /p SUBNETS_INPUT="Enter Subnet IDs (comma-separated, e.g., subnet-xxx,subnet-yyy): "
set /p SECURITY_GROUP="Enter Security Group ID: "
echo.

REM Parse subnets
for /f "tokens=1,2 delims=," %%a in ("!SUBNETS_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

REM Image Configuration
echo === Docker Image Configuration ===
set /p IMAGE_URI="Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/crm:latest): "
echo.

REM Database Configuration
echo === Database Configuration ===
set /p DB_HOST="Enter Database Host: "
set /p DB_PORT="Enter Database Port (default: 3306): "
if "!DB_PORT!"=="" set DB_PORT=3306
set /p DB_NAME="Enter Database Name (default: crm): "
if "!DB_NAME!"=="" set DB_NAME=crm
set /p DB_USERNAME="Enter Database Username: "
set /p DB_PASSWORD="Enter Database Password: "
echo.

REM Load Balancer Configuration
set /p NEEDS_LB="Do you need a load balancer for this service? (y/n): "
echo.

set TARGET_GROUP_ARN=
set ALB_DNS=

if /i "!NEEDS_LB!"=="y" (
    echo === Creating Application Load Balancer ===
    
    REM Create ALB name with timestamp
    set ALB_NAME=crm-alb-%RANDOM%
    echo Creating Application Load Balancer: !ALB_NAME!
    
    for /f "delims=" %%i in ('aws elbv2 create-load-balancer --name !ALB_NAME! --subnets !SUBNET_1! !SUBNET_2! --security-groups !SECURITY_GROUP! --scheme internet-facing --type application --ip-address-type ipv4 --region !AWS_REGION! --query "LoadBalancers[0].LoadBalancerArn" --output text') do set ALB_ARN=%%i
    echo ALB created: !ALB_ARN!
    
    REM Get ALB DNS
    for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --load-balancer-arns !ALB_ARN! --region !AWS_REGION! --query "LoadBalancers[0].DNSName" --output text') do set ALB_DNS=%%i
    
    REM Create Target Group
    set TG_NAME=crm-tg-%RANDOM%
    echo Creating Target Group: !TG_NAME!
    
    for /f "delims=" %%i in ('aws elbv2 create-target-group --name !TG_NAME! --protocol HTTP --port 8080 --vpc-id !VPC_ID! --target-type ip --health-check-enabled --health-check-protocol HTTP --health-check-path "/appinfo/health" --health-check-interval-seconds 30 --health-check-timeout-seconds 5 --healthy-threshold-count 2 --unhealthy-threshold-count 3 --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%i
    echo Target Group created: !TARGET_GROUP_ARN!
    
    REM Create Listener
    echo Creating ALB Listener...
    aws elbv2 create-listener --load-balancer-arn !ALB_ARN! --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn=!TARGET_GROUP_ARN! --region !AWS_REGION! >nul
    echo Load Balancer setup completed.
    echo.
) else (
    echo Skipping load balancer creation.
    echo.
)

REM Create CloudWatch Log Group
echo Creating CloudWatch Log Group...
aws logs create-log-group --log-group-name "/ecs/crm-task" --region !AWS_REGION! 2>nul
if !ERRORLEVEL! neq 0 (
    echo Log group already exists.
)
echo.

REM Update task definition
echo Updating task definition...
copy ecs\task-definition.json ecs\task-definition-deploy.json >nul

powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{AWS_REGION}}', '!AWS_REGION!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{ACCOUNT_ID}}', '!ACCOUNT_ID!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{DB_HOST}}', '!DB_HOST!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{DB_PORT}}', '!DB_PORT!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{DB_NAME}}', '!DB_NAME!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{DB_USERNAME}}', '!DB_USERNAME!' | Set-Content ecs\task-definition-deploy.json"
powershell -Command "(Get-Content ecs\task-definition-deploy.json) -replace '{{DB_PASSWORD}}', '!DB_PASSWORD!' | Set-Content ecs\task-definition-deploy.json"

REM Register task definition
echo Registering ECS task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://ecs/task-definition-deploy.json --region !AWS_REGION! --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

if "!TASK_DEF_ARN!"=="" (
    echo ERROR: Failed to register task definition.
    exit /b 1
)
echo Task definition registered: !TASK_DEF_ARN!
echo.

REM Update service definition
echo Updating service definition...
copy ecs\service-definition.json ecs\service-definition-deploy.json >nul

powershell -Command "(Get-Content ecs\service-definition-deploy.json) -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' | Set-Content ecs\service-definition-deploy.json"
powershell -Command "(Get-Content ecs\service-definition-deploy.json) -replace '{{SUBNET_1}}', '!SUBNET_1!' | Set-Content ecs\service-definition-deploy.json"
powershell -Command "(Get-Content ecs\service-definition-deploy.json) -replace '{{SUBNET_2}}', '!SUBNET_2!' | Set-Content ecs\service-definition-deploy.json"
powershell -Command "(Get-Content ecs\service-definition-deploy.json) -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!' | Set-Content ecs\service-definition-deploy.json"

if not "!TARGET_GROUP_ARN!"=="" (
    powershell -Command "(Get-Content ecs\service-definition-deploy.json) -replace '{{TARGET_GROUP_ARN}}', '!TARGET_GROUP_ARN!' | Set-Content ecs\service-definition-deploy.json"
) else (
    REM Remove loadBalancers section if no LB
    powershell -Command "$content = Get-Content ecs\service-definition-deploy.json | Out-String; $content = $content -replace ',\s*\"loadBalancers\":\s*\[[^\]]*\]', ''; $content = $content -replace ',\s*\"healthCheckGracePeriodSeconds\":\s*\d+', ''; $content | Set-Content ecs\service-definition-deploy.json"
)

REM Check if service exists
echo Checking if service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION! --query "services[0].serviceName" --output text 2^>nul') do set SERVICE_EXISTS=%%i

if "!SERVICE_EXISTS!"=="crm-service" (
    echo Service exists. Updating service...
    aws ecs update-service --cluster !CLUSTER_NAME! --service crm-service --task-definition !TASK_DEF_ARN! --desired-count 2 --region !AWS_REGION! >nul
    echo Service updated successfully.
) else (
    echo Service does not exist. Creating new service...
    aws ecs create-service --cli-input-json file://ecs/service-definition-deploy.json --region !AWS_REGION! >nul
    echo Service created successfully.
)
echo.

REM Wait for service stability
echo Waiting for service to become stable (this may take several minutes)...
aws ecs wait services-stable --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION!
echo Service is stable.
echo.

REM Verify deployment
echo === Deployment Summary ===
aws ecs describe-services --cluster !CLUSTER_NAME! --services crm-service --region !AWS_REGION! --query "services[0].{Status:status,Running:runningCount,Desired:desiredCount}" --output table

if not "!ALB_DNS!"=="" (
    echo.
    echo Application URL: http://!ALB_DNS!
)

echo.
echo CloudWatch Logs: /ecs/crm-task
echo.
echo ==========================================
echo Deployment completed successfully!
echo ==========================================

REM Cleanup
del ecs\task-definition-deploy.json 2>nul
del ecs\service-definition-deploy.json 2>nul

endlocal
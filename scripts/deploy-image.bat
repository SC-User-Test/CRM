@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo CompCRM AWS ECS Fargate Deployment Script
echo ==========================================
echo.

REM Project configuration
set PROJECT_NAME=compcrm
set TASK_FAMILY=!PROJECT_NAME!-task
set SERVICE_NAME=!PROJECT_NAME!-service

REM Prompt for deployment configuration
echo === AWS Configuration ===
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
echo.

echo === Network Configuration ===
set /p VPC_ID="Enter VPC ID: "
set /p SUBNETS_INPUT="Enter Subnet IDs (comma-separated, at least 2): "
set /p SECURITY_GROUP="Enter Security Group ID: "
echo.

REM Parse subnets
for /f "tokens=1,2 delims=," %%a in ("!SUBNETS_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

echo === Container Configuration ===
set /p IMAGE_URI="Enter ECR Image URI (e.g., 123456789.dkr.ecr.us-east-1.amazonaws.com/compcrm:latest): "
echo.

REM Get AWS Account ID
echo Retrieving AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
echo Account ID: !ACCOUNT_ID!
echo.

REM Check/Create ECS Cluster
echo Checking if ECS cluster exists...
aws ecs describe-clusters --clusters "!CLUSTER_NAME!" --region "!AWS_REGION!" >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Cluster does not exist. Creating ECS cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name "!CLUSTER_NAME!" --region "!AWS_REGION!"
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Failed to create ECS cluster
        exit /b 1
    )
)
echo Cluster ready: !CLUSTER_NAME!
echo.

REM Load Balancer Configuration
echo === Load Balancer Configuration ===
set /p NEED_ALB="Do you need an Application Load Balancer for this service? (y/n): "
echo.

set TARGET_GROUP_ARN=
set ALB_DNS=

if /i "!NEED_ALB!"=="y" (
    echo Creating Application Load Balancer and Target Group...
    
    set ALB_NAME=!PROJECT_NAME!-alb
    set TG_NAME=!PROJECT_NAME!-tg
    
    REM Create ALB
    echo Creating ALB: !ALB_NAME!
    for /f "delims=" %%i in ('aws elbv2 create-load-balancer --name "!ALB_NAME!" --subnets !SUBNET_1! !SUBNET_2! --security-groups "!SECURITY_GROUP!" --scheme internet-facing --type application --region "!AWS_REGION!" --query "LoadBalancers[0].LoadBalancerArn" --output text 2^>nul') do set ALB_ARN=%%i
    
    if "!ALB_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --names "!ALB_NAME!" --region "!AWS_REGION!" --query "LoadBalancers[0].LoadBalancerArn" --output text 2^>nul') do set ALB_ARN=%%i
    )
    
    if "!ALB_ARN!"=="" (
        echo ERROR: Failed to create or find ALB
        exit /b 1
    )
    
    echo ALB ARN: !ALB_ARN!
    
    REM Create Target Group
    echo Creating Target Group: !TG_NAME!
    for /f "delims=" %%i in ('aws elbv2 create-target-group --name "!TG_NAME!" --protocol HTTP --port 8080 --vpc-id "!VPC_ID!" --target-type ip --health-check-path "/appinfo/health" --health-check-interval-seconds 30 --health-check-timeout-seconds 5 --healthy-threshold-count 2 --unhealthy-threshold-count 3 --region "!AWS_REGION!" --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%i
    
    if "!TARGET_GROUP_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-target-groups --names "!TG_NAME!" --region "!AWS_REGION!" --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%i
    )
    
    if "!TARGET_GROUP_ARN!"=="" (
        echo ERROR: Failed to create or find Target Group
        exit /b 1
    )
    
    echo Target Group ARN: !TARGET_GROUP_ARN!
    
    REM Create Listener
    echo Creating ALB Listener...
    aws elbv2 create-listener --load-balancer-arn "!ALB_ARN!" --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn="!TARGET_GROUP_ARN!" --region "!AWS_REGION!" >nul 2>&1
    
    REM Get ALB DNS
    for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --load-balancer-arns "!ALB_ARN!" --region "!AWS_REGION!" --query "LoadBalancers[0].DNSName" --output text') do set ALB_DNS=%%i
    
    echo Load Balancer DNS: !ALB_DNS!
    echo.
)

REM Replace placeholders in task definition
echo Preparing ECS task definition...
copy ecs\task-definition.json ecs\task-definition-processed.json >nul

powershell -Command "(Get-Content ecs\task-definition-processed.json) -replace '{{IMAGE_URI}}','!IMAGE_URI!' | Set-Content ecs\task-definition-processed.json"
powershell -Command "(Get-Content ecs\task-definition-processed.json) -replace '{{AWS_REGION}}','!AWS_REGION!' | Set-Content ecs\task-definition-processed.json"
powershell -Command "(Get-Content ecs\task-definition-processed.json) -replace '{{ACCOUNT_ID}}','!ACCOUNT_ID!' | Set-Content ecs\task-definition-processed.json"

REM Register task definition
echo Registering ECS task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://ecs/task-definition-processed.json --region "!AWS_REGION!" --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

if !ERRORLEVEL! neq 0 (
    echo ERROR: Failed to register task definition
    exit /b 1
)

if "!TASK_DEF_ARN!"=="" (
    echo ERROR: Failed to register task definition
    exit /b 1
)

echo Task definition registered: !TASK_DEF_ARN!
echo.

REM Prepare service definition
echo Preparing ECS service definition...
copy ecs\service-definition.json ecs\service-definition-processed.json >nul

powershell -Command "(Get-Content ecs\service-definition-processed.json) -replace '{{CLUSTER_NAME}}','!CLUSTER_NAME!' | Set-Content ecs\service-definition-processed.json"
powershell -Command "(Get-Content ecs\service-definition-processed.json) -replace '{{SUBNET_1}}','!SUBNET_1!' | Set-Content ecs\service-definition-processed.json"
powershell -Command "(Get-Content ecs\service-definition-processed.json) -replace '{{SUBNET_2}}','!SUBNET_2!' | Set-Content ecs\service-definition-processed.json"
powershell -Command "(Get-Content ecs\service-definition-processed.json) -replace '{{SECURITY_GROUP}}','!SECURITY_GROUP!' | Set-Content ecs\service-definition-processed.json"

if not "!TARGET_GROUP_ARN!"=="" (
    powershell -Command "(Get-Content ecs\service-definition-processed.json) -replace '{{TARGET_GROUP_ARN}}','!TARGET_GROUP_ARN!' | Set-Content ecs\service-definition-processed.json"
) else (
    powershell -Command "$content = Get-Content ecs\service-definition-processed.json -Raw; $content = $content -replace ',?\s*\"loadBalancers\":\s*\[[^\]]*\]', ''; $content = $content -replace ',?\s*\"healthCheckGracePeriodSeconds\":\s*\d+', ''; $content | Set-Content ecs\service-definition-processed.json"
)

REM Check if service exists
echo Checking if ECS service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!" --query "services[0].serviceName" --output text 2^>nul') do set EXISTING_SERVICE=%%i

if "!EXISTING_SERVICE!"=="None" (
    echo Service does not exist. Creating new ECS service...
    aws ecs create-service --cluster "!CLUSTER_NAME!" --cli-input-json file://ecs/service-definition-processed.json --region "!AWS_REGION!" >nul
    
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Failed to create ECS service
        exit /b 1
    )
    echo Service created: !SERVICE_NAME!
) else (
    echo Service exists. Updating ECS service with new task definition...
    aws ecs update-service --cluster "!CLUSTER_NAME!" --service "!SERVICE_NAME!" --task-definition "!TASK_DEF_ARN!" --force-new-deployment --region "!AWS_REGION!" >nul
    
    if !ERRORLEVEL! neq 0 (
        echo ERROR: Failed to update ECS service
        exit /b 1
    )
    echo Service updated: !SERVICE_NAME!
)

echo.
echo Waiting for service to stabilize...
aws ecs wait services-stable --cluster "!CLUSTER_NAME!" --services "!SERVICE_NAME!" --region "!AWS_REGION!"

if !ERRORLEVEL! neq 0 (
    echo WARNING: Service did not stabilize within timeout period
    echo Check AWS console for service status
) else (
    echo Service is stable
)

echo.
echo ==========================================
echo DEPLOYMENT SUCCESSFUL!
echo ==========================================
echo Cluster: !CLUSTER_NAME!
echo Service: !SERVICE_NAME!
echo Task Definition: !TASK_DEF_ARN!
if not "!ALB_DNS!"=="" (
    echo Application URL: http://!ALB_DNS!
)
echo CloudWatch Logs: /ecs/!PROJECT_NAME!
echo.
echo Verify deployment:
echo   aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION!
echo ==========================================

REM Cleanup
del /f /q ecs\task-definition-processed.json ecs\service-definition-processed.json 2>nul

endlocal

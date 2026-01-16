=== scripts/deploy-image.bat ===
@echo off
setlocal enabledelayedexpansion

echo ======================================
echo   AWS ECS Fargate Deployment Script
echo ======================================
echo.

set PROJECT_NAME=comptestcrm
set TASK_FAMILY=!PROJECT_NAME!-task
set SERVICE_NAME=!PROJECT_NAME!-service

REM Prompt for AWS configuration
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
set /p VPC_ID="Enter VPC ID: "
set /p SUBNET_INPUT="Enter Subnet IDs (comma-separated, at least 2): "
set /p SECURITY_GROUP="Enter Security Group ID: "
set /p IMAGE_URI="Enter Docker Image URI: "

REM Database configuration
echo.
echo --- Database Configuration ---
set /p DB_HOST="Enter Database Host: "
set /p DB_PORT="Enter Database Port (default: 3306): "
if "!DB_PORT!"=="" set DB_PORT=3306
set /p DB_NAME="Enter Database Name (default: crm): "
if "!DB_NAME!"=="" set DB_NAME=crm
set /p DB_USER="Enter Database User: "
set /p DB_PASSWORD="Enter Database Password: "

REM Parse subnets (simple approach for two subnets)
for /f "tokens=1,2 delims=," %%a in ("!SUBNET_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

REM Get AWS Account ID
echo.
echo Retrieving AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text --region !AWS_REGION!') do set ACCOUNT_ID=%%i
echo Account ID: !ACCOUNT_ID!

REM Check/Create ECS Cluster
echo.
echo Checking ECS cluster...
aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Cluster does not exist. Creating ECS cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
)

REM Check/Create CloudWatch Log Group
echo.
echo Checking CloudWatch log group...
set LOG_GROUP=/ecs/!PROJECT_NAME!
aws logs describe-log-groups --log-group-name-prefix "!LOG_GROUP!" --region !AWS_REGION! | findstr "!LOG_GROUP!" >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Creating CloudWatch log group: !LOG_GROUP!
    aws logs create-log-group --log-group-name "!LOG_GROUP!" --region !AWS_REGION!
    aws logs put-retention-policy --log-group-name "!LOG_GROUP!" --retention-in-days 7 --region !AWS_REGION!
)

REM Load Balancer Configuration
echo.
set /p NEED_LB="Do you need a load balancer for this service? (y/n): "

if /i "!NEED_LB!"=="y" (
    echo.
    echo Creating Application Load Balancer and Target Group...
    
    set ALB_NAME=!PROJECT_NAME!-alb
    
    REM Create ALB
    for /f "delims=" %%i in ('aws elbv2 create-load-balancer --name !ALB_NAME! --subnets !SUBNET_1! !SUBNET_2! --security-groups !SECURITY_GROUP! --scheme internet-facing --type application --ip-address-type ipv4 --region !AWS_REGION! --query "LoadBalancers[0].LoadBalancerArn" --output text 2^>nul') do set ALB_ARN=%%i
    
    if "!ALB_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --names !ALB_NAME! --region !AWS_REGION! --query "LoadBalancers[0].LoadBalancerArn" --output text') do set ALB_ARN=%%i
    )
    
    echo Load Balancer ARN: !ALB_ARN!
    
    REM Create Target Group with target-type ip
    set TG_NAME=!PROJECT_NAME!-tg
    for /f "delims=" %%i in ('aws elbv2 create-target-group --name !TG_NAME! --protocol HTTP --port 8080 --vpc-id !VPC_ID! --target-type ip --health-check-enabled --health-check-protocol HTTP --health-check-path "/appinfo/health" --health-check-interval-seconds 30 --health-check-timeout-seconds 5 --healthy-threshold-count 2 --unhealthy-threshold-count 3 --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%i
    
    if "!TARGET_GROUP_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-target-groups --names !TG_NAME! --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%i
    )
    
    echo Target Group ARN: !TARGET_GROUP_ARN!
    
    REM Create Listener
    aws elbv2 create-listener --load-balancer-arn !ALB_ARN! --protocol HTTP --port 80 --default-actions Type=forward,TargetGroupArn=!TARGET_GROUP_ARN! --region !AWS_REGION! >nul 2>&1
    
    REM Get ALB DNS name
    for /f "delims=" %%i in ('aws elbv2 describe-load-balancers --load-balancer-arns !ALB_ARN! --region !AWS_REGION! --query "LoadBalancers[0].DNSName" --output text') do set ALB_DNS=%%i
    echo Load Balancer DNS: !ALB_DNS!
) else (
    set TARGET_GROUP_ARN=
    echo Skipping load balancer configuration
)

REM Prepare task definition JSON
echo.
echo Preparing task definition...
set TASK_DEF_FILE=ecs\task-definition.json
copy "!TASK_DEF_FILE!" "!TASK_DEF_FILE!.tmp" >nul

powershell -Command "(Get-Content '!TASK_DEF_FILE!.tmp') -replace '{{IMAGE_URI}}', '!IMAGE_URI!' -replace '{{AWS_REGION}}', '!AWS_REGION!' -replace '{{ACCOUNT_ID}}', '!ACCOUNT_ID!' -replace '{{DB_HOST}}', '!DB_HOST!' -replace '{{DB_PORT}}', '!DB_PORT!' -replace '{{DB_NAME}}', '!DB_NAME!' -replace '{{DB_USER}}', '!DB_USER!' -replace '{{DB_PASSWORD}}', '!DB_PASSWORD!' | Set-Content '!TASK_DEF_FILE!.tmp'"

REM Register task definition
echo Registering task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://!TASK_DEF_FILE!.tmp --region !AWS_REGION! --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

echo Task Definition ARN: !TASK_DEF_ARN!

REM Clean up temporary file
del "!TASK_DEF_FILE!.tmp"

REM Prepare service definition JSON
echo.
echo Preparing service definition...
set SERVICE_DEF_FILE=ecs\service-definition.json
copy "!SERVICE_DEF_FILE!" "!SERVICE_DEF_FILE!.tmp" >nul

if "!TARGET_GROUP_ARN!"=="" (
    powershell -Command "$content = Get-Content '!SERVICE_DEF_FILE!.tmp' | Out-String; $content = $content -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' -replace '{{SUBNET_1}}', '!SUBNET_1!' -replace '{{SUBNET_2}}', '!SUBNET_2!' -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!'; $json = $content | ConvertFrom-Json; $json.PSObject.Properties.Remove('loadBalancers'); $json.PSObject.Properties.Remove('healthCheckGracePeriodSeconds'); $json | ConvertTo-Json -Depth 10 | Set-Content '!SERVICE_DEF_FILE!.tmp'"
) else (
    powershell -Command "(Get-Content '!SERVICE_DEF_FILE!.tmp') -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' -replace '{{SUBNET_1}}', '!SUBNET_1!' -replace '{{SUBNET_2}}', '!SUBNET_2!' -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!' -replace '{{TARGET_GROUP_ARN}}', '!TARGET_GROUP_ARN!' | Set-Content '!SERVICE_DEF_FILE!.tmp'"
)

REM Check if service exists
echo.
echo Checking if service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[0].serviceName" --output text 2^>nul') do set SERVICE_EXISTS=%%i

if "!SERVICE_EXISTS!"=="!SERVICE_NAME!" (
    echo Service exists. Updating service...
    aws ecs update-service --cluster !CLUSTER_NAME! --service !SERVICE_NAME! --task-definition !TASK_DEF_ARN! --force-new-deployment --region !AWS_REGION! >nul
) else (
    echo Service does not exist. Creating service...
    aws ecs create-service --cli-input-json file://!SERVICE_DEF_FILE!.tmp --region !AWS_REGION! >nul
)

REM Clean up temporary file
del "!SERVICE_DEF_FILE!.tmp"

REM Wait for service stability
echo.
echo Waiting for service to become stable...
aws ecs wait services-stable --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION!

REM Verify deployment
echo.
echo ======================================
echo Deployment Completed Successfully
echo ======================================
echo.
echo Service Details:
aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[0].{Status:status,DesiredCount:desiredCount,RunningCount:runningCount,PendingCount:pendingCount}" --output table

echo.
echo CloudWatch Logs: !LOG_GROUP!

if not "!ALB_DNS!"=="" (
    echo Application URL: http://!ALB_DNS!
)

echo.
echo To view logs:
echo   aws logs tail !LOG_GROUP! --follow --region !AWS_REGION!
echo.

endlocal

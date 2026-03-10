@echo off
REM === deploy-image.bat ===
REM Deploy CompappCRM to AWS ECS Fargate

setlocal enabledelayedexpansion

echo ===========================================
echo CompappCRM AWS ECS Fargate Deployment
echo ===========================================
echo.

REM Configuration
set SERVICE_NAME=compappcrm-service
set TASK_FAMILY=compappcrm-task
set CONTAINER_NAME=compappcrm
set APP_PORT=8080

REM Prompt for deployment configuration
echo AWS ECS Configuration
echo ---------------------
set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
set /p IMAGE_URI="Enter Docker Image URI: "

echo.
echo Network Configuration
echo ---------------------
set /p VPC_ID="Enter VPC ID: "
set /p SUBNETS_INPUT="Enter Subnet IDs (comma-separated, min 2): "
set /p SECURITY_GROUP="Enter Security Group ID: "

REM Parse subnets
for /f "tokens=1,2 delims=," %%a in ("!SUBNETS_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)

REM Trim whitespace
for /f "tokens=* delims= " %%a in ("!SUBNET_1!") do set SUBNET_1=%%a
for /f "tokens=* delims= " %%a in ("!SUBNET_2!") do set SUBNET_2=%%a

if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

echo.
echo Application Configuration
echo -------------------------
set /p NEEDS_LB="Do you need a load balancer for this service? (y/n): "

if /i "!NEEDS_LB!"=="y" (
    echo.
    echo Creating Application Load Balancer and Target Group...
    
    set TARGET_GROUP_NAME=!TASK_FAMILY!-tg
    
    echo Creating target group: !TARGET_GROUP_NAME!
    
    REM Try to create target group, or get existing
    for /f "delims=" %%i in ('aws elbv2 create-target-group --name !TARGET_GROUP_NAME! --protocol HTTP --port !APP_PORT! --vpc-id !VPC_ID! --target-type ip --health-check-enabled --health-check-path "/appinfo/health" --health-check-interval-seconds 30 --health-check-timeout-seconds 5 --healthy-threshold-count 2 --unhealthy-threshold-count 3 --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text 2^>nul') do set TARGET_GROUP_ARN=%%i
    
    if "!TARGET_GROUP_ARN!"=="" (
        for /f "delims=" %%i in ('aws elbv2 describe-target-groups --names !TARGET_GROUP_NAME! --region !AWS_REGION! --query "TargetGroups[0].TargetGroupArn" --output text') do set TARGET_GROUP_ARN=%%i
    )
    
    echo Target Group ARN: !TARGET_GROUP_ARN!
    set USE_LOAD_BALANCER=true
) else (
    set USE_LOAD_BALANCER=false
    echo Skipping load balancer configuration.
)

echo.
echo Getting AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
echo Account ID: !ACCOUNT_ID!

echo.
echo Checking if ECS cluster exists...
aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Cluster does not exist. Creating cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
)

echo.
echo Preparing task definition...
cd /d "%~dp0.."

REM Create resolved task definition
powershell -Command "(Get-Content ecs\task-definition.json) -replace '{{IMAGE_URI}}', '%IMAGE_URI%' -replace '{{AWS_REGION}}', '%AWS_REGION%' -replace '{{ACCOUNT_ID}}', '%ACCOUNT_ID%' | Set-Content ecs\task-definition-resolved.json"

echo Registering task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://ecs/task-definition-resolved.json --region !AWS_REGION! --query "taskDefinition.taskDefinitionArn" --output text') do set TASK_DEF_ARN=%%i

echo Task Definition ARN: !TASK_DEF_ARN!

echo.
echo Preparing service definition...

if "!USE_LOAD_BALANCER!"=="true" (
    REM Include load balancer configuration
    powershell -Command "(Get-Content ecs\service-definition.json) -replace '{{CLUSTER_NAME}}', '%CLUSTER_NAME%' -replace '{{SUBNET_1}}', '%SUBNET_1%' -replace '{{SUBNET_2}}', '%SUBNET_2%' -replace '{{SECURITY_GROUP}}', '%SECURITY_GROUP%' -replace '{{TARGET_GROUP_ARN}}', '%TARGET_GROUP_ARN%' | Set-Content ecs\service-definition-resolved.json"
) else (
    REM Remove load balancer section
    powershell -Command "$json = Get-Content ecs\service-definition.json | ConvertFrom-Json; $json.PSObject.Properties.Remove('loadBalancers'); $json.PSObject.Properties.Remove('healthCheckGracePeriodSeconds'); $content = ($json | ConvertTo-Json -Depth 10) -replace '{{CLUSTER_NAME}}', '%CLUSTER_NAME%' -replace '{{SUBNET_1}}', '%SUBNET_1%' -replace '{{SUBNET_2}}', '%SUBNET_2%' -replace '{{SECURITY_GROUP}}', '%SECURITY_GROUP%'; $content | Set-Content ecs\service-definition-resolved.json"
)

echo Checking if service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[?status==`ACTIVE`].serviceName" --output text 2^>nul') do set EXISTING_SERVICE=%%i

if not "!EXISTING_SERVICE!"=="" if not "!EXISTING_SERVICE!"=="None" (
    echo Service exists. Updating service...
    aws ecs update-service --cluster !CLUSTER_NAME! --service !SERVICE_NAME! --task-definition !TASK_DEF_ARN! --force-new-deployment --region !AWS_REGION!
) else (
    echo Service does not exist. Creating service...
    aws ecs create-service --cli-input-json file://ecs/service-definition-resolved.json --region !AWS_REGION!
)

echo.
echo Waiting for service to stabilize...
aws ecs wait services-stable --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION!

echo.
echo ===========================================
echo DEPLOYMENT SUCCESSFUL!
echo ===========================================
echo Cluster: !CLUSTER_NAME!
echo Service: !SERVICE_NAME!
echo Task Definition: !TASK_DEF_ARN!
echo.

if "!USE_LOAD_BALANCER!"=="true" (
    echo Load Balancer Target Group: !TARGET_GROUP_ARN!
    echo Note: Configure your ALB listener to forward traffic to this target group.
    echo.
)

echo View logs in CloudWatch:
echo Log Group: /ecs/compappcrm
echo Region: !AWS_REGION!
echo.
echo Verify deployment:
echo aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION!
echo ===========================================

REM Cleanup temporary files
del /f /q ecs\task-definition-resolved.json ecs\service-definition-resolved.json 2>nul

endlocal
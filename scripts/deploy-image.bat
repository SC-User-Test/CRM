@echo off
setlocal enabledelayedexpansion

echo ========================================
echo AWS ECS Fargate Deployment Script
echo ========================================
echo.

set PROJECT_NAME=crmcomptest
set TASK_FAMILY=!PROJECT_NAME!-task
set SERVICE_NAME=!PROJECT_NAME!-service

set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
set /p CLUSTER_NAME="Enter ECS Cluster Name: "
set /p VPC_ID="Enter VPC ID: "
set /p SUBNETS_INPUT="Enter Subnet IDs (comma-separated, at least 2): "
set /p SECURITY_GROUP="Enter Security Group ID: "
set /p IMAGE_URI="Enter ECR Image URI: "

for /f "tokens=1,2 delims=," %%a in ("!SUBNETS_INPUT!") do (
    set SUBNET_1=%%a
    set SUBNET_2=%%b
)
if "!SUBNET_2!"=="" set SUBNET_2=!SUBNET_1!

echo.
echo Retrieving AWS Account ID...
for /f "delims=" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
echo Account ID: !ACCOUNT_ID!

echo.
echo Checking ECS cluster...
aws ecs describe-clusters --clusters !CLUSTER_NAME! --region !AWS_REGION! >nul 2>&1
if !ERRORLEVEL! neq 0 (
    echo Cluster does not exist. Creating cluster: !CLUSTER_NAME!
    aws ecs create-cluster --cluster-name !CLUSTER_NAME! --region !AWS_REGION!
)

echo.
echo Creating CloudWatch log group...
set LOG_GROUP=/ecs/!PROJECT_NAME!
aws logs create-log-group --log-group-name !LOG_GROUP! --region !AWS_REGION! 2>nul

set /p USE_LB="Do you need a load balancer for this service? (y/n): "

set LOAD_BALANCER_CONFIG=
if /i "!USE_LB!"=="y" (
    echo.
    echo Load balancer configuration will be handled manually after service creation.
    echo Please configure your ALB/NLB and target group separately.
)

echo.
echo Preparing task definition...
copy ecs\task-definition.json ecs\task-definition-resolved.json >nul
powershell -Command "(Get-Content ecs\task-definition-resolved.json) -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content ecs\task-definition-resolved.json"
powershell -Command "(Get-Content ecs\task-definition-resolved.json) -replace '{{AWS_REGION}}', '!AWS_REGION!' | Set-Content ecs\task-definition-resolved.json"
powershell -Command "(Get-Content ecs\task-definition-resolved.json) -replace '{{ACCOUNT_ID}}', '!ACCOUNT_ID!' | Set-Content ecs\task-definition-resolved.json"

echo.
echo Registering task definition...
for /f "delims=" %%i in ('aws ecs register-task-definition --cli-input-json file://ecs/task-definition-resolved.json --region !AWS_REGION! --query taskDefinition.taskDefinitionArn --output text') do set TASK_DEF_ARN=%%i
echo Task Definition ARN: !TASK_DEF_ARN!

echo.
echo Preparing service definition...
copy ecs\service-definition.json ecs\service-definition-resolved.json >nul
powershell -Command "(Get-Content ecs\service-definition-resolved.json) -replace '{{CLUSTER_NAME}}', '!CLUSTER_NAME!' | Set-Content ecs\service-definition-resolved.json"
powershell -Command "(Get-Content ecs\service-definition-resolved.json) -replace '{{SUBNET_1}}', '!SUBNET_1!' | Set-Content ecs\service-definition-resolved.json"
powershell -Command "(Get-Content ecs\service-definition-resolved.json) -replace '{{SUBNET_2}}', '!SUBNET_2!' | Set-Content ecs\service-definition-resolved.json"
powershell -Command "(Get-Content ecs\service-definition-resolved.json) -replace '{{SECURITY_GROUP}}', '!SECURITY_GROUP!' | Set-Content ecs\service-definition-resolved.json"

echo.
echo Checking if service exists...
for /f "delims=" %%i in ('aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[?status==`ACTIVE`].serviceName" --output text') do set EXISTING_SERVICE=%%i

if "!EXISTING_SERVICE!"=="" (
    echo Creating new service...
    aws ecs create-service --cli-input-json file://ecs/service-definition-resolved.json --region !AWS_REGION!
) else (
    echo Service exists. Updating service with new task definition...
    aws ecs update-service --cluster !CLUSTER_NAME! --service !SERVICE_NAME! --task-definition !TASK_DEF_ARN! --region !AWS_REGION!
)

echo.
echo Waiting for service to become stable...
aws ecs wait services-stable --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION!

echo.
echo ========================================
echo Deployment Complete!
echo ========================================
echo.
echo Service Details:
aws ecs describe-services --cluster !CLUSTER_NAME! --services !SERVICE_NAME! --region !AWS_REGION! --query "services[0].[serviceName,status,runningCount,desiredCount]" --output table

echo.
echo CloudWatch Logs: !LOG_GROUP!
echo.
echo To view logs run:
echo aws logs tail !LOG_GROUP! --follow --region !AWS_REGION!

endlocal
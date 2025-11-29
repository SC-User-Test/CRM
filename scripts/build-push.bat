@echo off
setlocal enabledelayedexpansion

echo CRM Application - Build and Push Script
echo ==========================================
echo.

:: Get project name from directory or use default
for %%i in (.) do set PROJECT_NAME=%%~nxi
if "!PROJECT_NAME!"=="" set PROJECT_NAME=crm

:: Sanitize project name for Docker (basic version for batch)
set IMAGE_NAME=!PROJECT_NAME!
set IMAGE_NAME=!IMAGE_NAME: =-!
for /f "delims=" %%i in ('echo !IMAGE_NAME! ^| powershell -Command "$input.ToLower() -replace '[^a-z0-9-]', '-' -replace '^-+', '' -replace '-+$', ''"') do set IMAGE_NAME=%%i

echo Project: !PROJECT_NAME!
echo Image name: !IMAGE_NAME!
echo.

:: Prompt for image tag
set /p IMAGE_TAG="Enter image tag (default: latest): "
if "!IMAGE_TAG!"=="" set IMAGE_TAG=latest

:: Sanitize tag
for /f "delims=" %%i in ('echo !IMAGE_TAG! ^| powershell -Command "$input.ToLower() -replace '[^a-z0-9.-]', '-' -replace '^-+', '' -replace '-+$', ''"') do set IMAGE_TAG=%%i
if "!IMAGE_TAG!"=="" set IMAGE_TAG=latest

echo Using tag: !IMAGE_TAG!
echo.

:: Registry selection
echo Select container registry:
echo 1. AWS ECR
echo 2. Docker Hub
set /p REGISTRY_CHOICE="Enter choice (1-2): "

if "!REGISTRY_CHOICE!"=="1" (
    echo Selected: AWS ECR
    set /p AWS_REGION="Enter AWS region (default: us-east-1): "
    if "!AWS_REGION!"=="" set AWS_REGION=us-east-1
    
    set /p ECR_REPO="Enter ECR repository name (default: !IMAGE_NAME!): "
    if "!ECR_REPO!"=="" set ECR_REPO=!IMAGE_NAME!
    
    :: Get AWS Account ID
    for /f "tokens=*" %%i in ('aws sts get-caller-identity --query Account --output text') do set ACCOUNT_ID=%%i
    if !ERRORLEVEL! neq 0 (
        echo Failed to get AWS Account ID. Please check AWS CLI configuration.
        exit /b 1
    )
    
    set REGISTRY_URL=!ACCOUNT_ID!.dkr.ecr.!AWS_REGION!.amazonaws.com
    set FULL_IMAGE_NAME=!REGISTRY_URL!/!ECR_REPO!:!IMAGE_TAG!
    
    echo Registry URL: !REGISTRY_URL!
    echo Full image name: !FULL_IMAGE_NAME!
    echo.
    
    :: ECR Login
    echo Logging into ECR...
    aws ecr get-login-password --region !AWS_REGION! | docker login --username AWS --password-stdin !REGISTRY_URL!
    if !ERRORLEVEL! neq 0 (
        echo ECR login failed
        exit /b 1
    )
    
    :: Check if repository exists, create if it doesn't
    echo Checking ECR repository...
    aws ecr describe-repositories --repository-names !ECR_REPO! --region !AWS_REGION! >nul 2>&1
    if !ERRORLEVEL! neq 0 (
        echo Creating ECR repository: !ECR_REPO!
        aws ecr create-repository --repository-name !ECR_REPO! --region !AWS_REGION!
        if !ERRORLEVEL! neq 0 (
            echo Failed to create ECR repository
            exit /b 1
        )
    )
) else if "!REGISTRY_CHOICE!"=="2" (
    echo Selected: Docker Hub
    set /p DOCKER_USERNAME="Enter Docker Hub username: "
    set /p DOCKER_PASSWORD="Enter Docker Hub password: "
    
    if "!DOCKER_USERNAME!"=="" (
        echo Docker Hub credentials are required
        exit /b 1
    )
    if "!DOCKER_PASSWORD!"=="" (
        echo Docker Hub credentials are required
        exit /b 1
    )
    
    set FULL_IMAGE_NAME=!DOCKER_USERNAME!/!IMAGE_NAME!:!IMAGE_TAG!
    
    echo Full image name: !FULL_IMAGE_NAME!
    echo.
    
    :: Docker Hub Login
    echo Logging into Docker Hub...
    echo !DOCKER_PASSWORD! | docker login --username !DOCKER_USERNAME! --password-stdin
    if !ERRORLEVEL! neq 0 (
        echo Docker Hub login failed
        exit /b 1
    )
) else (
    echo Invalid choice
    exit /b 1
)

:: Build Docker image
echo Building Docker image...
docker build -t !FULL_IMAGE_NAME! .
if !ERRORLEVEL! neq 0 (
    echo Docker build failed
    exit /b 1
)

:: Push Docker image
echo Pushing Docker image...
docker push !FULL_IMAGE_NAME!
if !ERRORLEVEL! neq 0 (
    echo Docker push failed
    exit /b 1
)

echo Successfully built and pushed: !FULL_IMAGE_NAME!
echo.
echo Next steps:
echo 1. Update your deployment configuration with the image URI
echo 2. Deploy to your target platform (ECS, Kubernetes, etc.)

pause
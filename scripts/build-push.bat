@echo off
setlocal enabledelayedexpansion

echo === CRMTestComp Docker Build and Push Script ===
echo.

REM Project configuration
set PROJECT_NAME=CRMTestComp

REM Sanitize image name (convert to lowercase, replace invalid chars with hyphens)
for /f "delims=" %%i in ('powershell -command "'!PROJECT_NAME!' -replace '[^a-zA-Z0-9]', '-' -replace '^-+', '' -replace '-+$', '' | %% {$_.ToLower()}"') do set IMAGE_NAME=%%i
echo Sanitized image name: !IMAGE_NAME!
echo.

REM Prompt for image tag
set /p IMAGE_TAG="Enter image tag (default: latest): "
if "!IMAGE_TAG!"=="" set IMAGE_TAG=latest

REM Sanitize tag
for /f "delims=" %%i in ('powershell -command "'!IMAGE_TAG!' -replace '[^a-zA-Z0-9.-]', '-' -replace '^-+', '' -replace '-+$', '' | %% {$_.ToLower()}"') do set IMAGE_TAG=%%i
echo Using tag: !IMAGE_TAG!
echo.

REM Registry selection
echo Select Docker registry:
echo 1. AWS ECR
echo 2. Docker Hub
set /p REGISTRY_CHOICE="Enter choice (1-2): "

if "!REGISTRY_CHOICE!"=="1" (
    echo === AWS ECR Configuration ===
    set /p AWS_REGION="Enter AWS region: "
    set /p AWS_ACCOUNT_ID="Enter AWS Account ID: "
    
    set ECR_REPO=!IMAGE_NAME!
    set REGISTRY_URL=!AWS_ACCOUNT_ID!.dkr.ecr.!AWS_REGION!.amazonaws.com
    set FULL_IMAGE_NAME=!REGISTRY_URL!/!ECR_REPO!:!IMAGE_TAG!
    
    echo Full image name: !FULL_IMAGE_NAME!
    echo.
    
    echo Authenticating with AWS ECR...
    aws ecr get-login-password --region !AWS_REGION! | docker login --username AWS --password-stdin !REGISTRY_URL!
    if !ERRORLEVEL! neq 0 (
        echo ECR login failed
        exit /b 1
    )
    
    echo Checking if ECR repository exists...
    aws ecr describe-repositories --repository-names !ECR_REPO! --region !AWS_REGION! >nul 2>&1
    if !ERRORLEVEL! neq 0 (
        echo Creating ECR repository: !ECR_REPO!
        aws ecr create-repository --repository-name !ECR_REPO! --region !AWS_REGION!
    )
) else if "!REGISTRY_CHOICE!"=="2" (
    echo === Docker Hub Configuration ===
    set /p DOCKER_USERNAME="Enter Docker Hub username: "
    set /p DOCKER_PASSWORD="Enter Docker Hub password: "
    
    set FULL_IMAGE_NAME=!DOCKER_USERNAME!/!IMAGE_NAME!:!IMAGE_TAG!
    
    echo Full image name: !FULL_IMAGE_NAME!
    echo.
    
    echo Authenticating with Docker Hub...
    echo !DOCKER_PASSWORD! | docker login --username !DOCKER_USERNAME! --password-stdin
    if !ERRORLEVEL! neq 0 (
        echo Docker Hub login failed
        exit /b 1
    )
) else (
    echo Invalid choice. Exiting.
    exit /b 1
)

echo.
echo Building Docker image...
docker build -t "!FULL_IMAGE_NAME!" .
if !ERRORLEVEL! neq 0 (
    echo Docker build failed
    exit /b 1
)

echo.
echo Pushing Docker image...
docker push "!FULL_IMAGE_NAME!"
if !ERRORLEVEL! neq 0 (
    echo Docker push failed
    exit /b 1
)

echo.
echo === Build and Push Complete ===
echo Image: !FULL_IMAGE_NAME!
echo You can now use this image URI in your deployment scripts.
echo.

pause
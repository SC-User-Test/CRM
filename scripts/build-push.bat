@echo off
REM === build-push.bat ===
REM Build and push Docker image for CompappCRM application

setlocal enabledelayedexpansion

echo =====================================
echo CompappCRM Docker Build ^& Push Script
echo =====================================
echo.

REM Project configuration
set PROJECT_NAME=CompappCRM

REM Sanitize project name for Docker tag
set IMAGE_NAME=%PROJECT_NAME%
for %%a in (A B C D E F G H I J K L M N O P Q R S T U V W X Y Z) do (
    set IMAGE_NAME=!IMAGE_NAME:%%a=%%a!
)
set IMAGE_NAME=%IMAGE_NAME: =-%
set IMAGE_NAME=%IMAGE_NAME%

REM Simple lowercase conversion
for %%i in (a b c d e f g h i j k l m n o p q r s t u v w x y z) do (
    call set IMAGE_NAME=%%IMAGE_NAME:%%i=%%i%%
)
set IMAGE_NAME=%IMAGE_NAME: =-%
set IMAGE_NAME=compappcrm

echo Image name: !IMAGE_NAME!
echo.

REM Prompt for image tag
set /p IMAGE_TAG="Enter image tag (default: latest): "
if "!IMAGE_TAG!"==" " set IMAGE_TAG=latest
if "!IMAGE_TAG!"=="" set IMAGE_TAG=latest

echo Using tag: !IMAGE_TAG!
echo.

REM Select registry
echo Select container registry:
echo 1. AWS ECR (Elastic Container Registry)
echo 2. Docker Hub
set /p REGISTRY_CHOICE="Enter choice (1 or 2): "

if "!REGISTRY_CHOICE!"=="1" (
    REM AWS ECR
    echo.
    echo AWS ECR Configuration
    echo ---------------------
    
    set /p AWS_REGION="Enter AWS Region (e.g., us-east-1): "
    set /p AWS_ACCOUNT_ID="Enter AWS Account ID: "
    set /p ECR_REPO="Enter ECR Repository Name (default: !IMAGE_NAME!): "
    if "!ECR_REPO!"=="" set ECR_REPO=!IMAGE_NAME!
    
    set REGISTRY_URL=!AWS_ACCOUNT_ID!.dkr.ecr.!AWS_REGION!.amazonaws.com
    set FULL_IMAGE_NAME=!REGISTRY_URL!/!ECR_REPO!:!IMAGE_TAG!
    
    echo.
    echo Logging in to AWS ECR...
    for /f "delims=" %%p in ('aws ecr get-login-password --region !AWS_REGION!') do set ECR_PASSWORD=%%p
    echo !ECR_PASSWORD! | docker login --username AWS --password-stdin !REGISTRY_URL!
    
    if !ERRORLEVEL! neq 0 (
        echo Error: ECR login failed
        exit /b 1
    )
    
    echo Checking if ECR repository exists...
    aws ecr describe-repositories --repository-names !ECR_REPO! --region !AWS_REGION! >nul 2>&1
    if !ERRORLEVEL! neq 0 (
        echo Repository does not exist. Creating ECR repository: !ECR_REPO!
        aws ecr create-repository --repository-name !ECR_REPO! --region !AWS_REGION!
    )
    
) else if "!REGISTRY_CHOICE!"=="2" (
    REM Docker Hub
    echo.
    echo Docker Hub Configuration
    echo ------------------------
    
    set /p DOCKER_USERNAME="Enter Docker Hub username: "
    set /p DOCKER_PASSWORD="Enter Docker Hub password/token: "
    
    set FULL_IMAGE_NAME=!DOCKER_USERNAME!/!IMAGE_NAME!:!IMAGE_TAG!
    
    echo.
    echo Logging in to Docker Hub...
    echo !DOCKER_PASSWORD! | docker login --username !DOCKER_USERNAME! --password-stdin
    
    if !ERRORLEVEL! neq 0 (
        echo Error: Docker Hub login failed
        exit /b 1
    )
) else (
    echo Invalid choice. Exiting.
    exit /b 1
)

echo.
echo Building Docker image: !FULL_IMAGE_NAME!
echo ---------------------------------------------
docker build -t "!FULL_IMAGE_NAME!" .

if !ERRORLEVEL! neq 0 (
    echo Error: Docker build failed
    exit /b 1
)

echo.
echo Pushing image to registry...
echo -----------------------------
docker push "!FULL_IMAGE_NAME!"

if !ERRORLEVEL! neq 0 (
    echo Error: Docker push failed
    exit /b 1
)

echo.
echo =====================================
echo SUCCESS!
echo =====================================
echo Image: !FULL_IMAGE_NAME!
echo.
echo Use this image URI for deployment.
echo =====================================

endlocal
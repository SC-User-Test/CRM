@echo off
setlocal enabledelayedexpansion

echo === CRMTestComp AWS EKS Deployment Script ===
echo.

REM Prompt for AWS and EKS configuration
set /p AWS_REGION="Enter AWS region: "
set /p CLUSTER_NAME="Enter EKS cluster name: "
set /p IMAGE_URI="Enter Docker image URI (full path with tag): "

echo.
echo === Environment Configuration ===
echo Optional: Configure environment variables for external services
echo (Press Enter to skip any variable)
echo.

REM Database configuration
set /p DB_HOST="Enter database host (DB_HOST): "
set /p DB_PORT="Enter database port (DB_PORT, default 3306): "
if "!DB_PORT!"=="" set DB_PORT=3306
set /p DB_NAME="Enter database name (DB_NAME, default crm): "
if "!DB_NAME!"=="" set DB_NAME=crm
set /p DB_USER="Enter database username (DB_USER): "
set /p DB_PASSWORD="Enter database password (DB_PASSWORD): "

echo.
echo === Deployment Configuration ===
echo AWS Region: !AWS_REGION!
echo EKS Cluster: !CLUSTER_NAME!
echo Image URI: !IMAGE_URI!
echo Database Host: !DB_HOST!
echo Database Port: !DB_PORT!
echo Database Name: !DB_NAME!
echo Database User: !DB_USER!
echo.

set /p CONFIRM="Proceed with deployment (y/N): "
if /i not "!CONFIRM!"=="y" (
    echo Deployment cancelled.
    exit /b 0
)

echo.
echo === Configuring kubectl ===
aws eks update-kubeconfig --region "!AWS_REGION!" --name "!CLUSTER_NAME!"
if !ERRORLEVEL! neq 0 (
    echo Failed to configure kubectl
    exit /b 1
)

echo Verifying cluster connectivity...
kubectl cluster-info
if !ERRORLEVEL! neq 0 (
    echo Failed to connect to EKS cluster. Please check your AWS credentials and cluster name.
    exit /b 1
)

echo.
echo === Updating Kubernetes manifests ===

REM Update deployment manifest with image URI and environment variables
powershell -command "(Get-Content kubernetes/deployment.yaml) -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content kubernetes/deployment.yaml"
powershell -command "(Get-Content kubernetes/deployment.yaml) -replace '{{DB_HOST}}', '!DB_HOST!' | Set-Content kubernetes/deployment.yaml"
powershell -command "(Get-Content kubernetes/deployment.yaml) -replace '{{DB_PORT}}', '!DB_PORT!' | Set-Content kubernetes/deployment.yaml"
powershell -command "(Get-Content kubernetes/deployment.yaml) -replace '{{DB_NAME}}', '!DB_NAME!' | Set-Content kubernetes/deployment.yaml"
powershell -command "(Get-Content kubernetes/deployment.yaml) -replace '{{DB_USER}}', '!DB_USER!' | Set-Content kubernetes/deployment.yaml"
powershell -command "(Get-Content kubernetes/deployment.yaml) -replace '{{DB_PASSWORD}}', '!DB_PASSWORD!' | Set-Content kubernetes/deployment.yaml"

echo Manifests updated successfully.

echo.
echo === Deploying to Kubernetes ===

echo Creating namespace...
kubectl apply -f kubernetes/namespace.yaml
if !ERRORLEVEL! neq 0 (
    echo Failed to create namespace
    exit /b 1
)

echo Deploying application...
kubectl apply -f kubernetes/deployment.yaml
if !ERRORLEVEL! neq 0 (
    echo Failed to deploy application
    exit /b 1
)

echo Creating service...
kubectl apply -f kubernetes/service.yaml
if !ERRORLEVEL! neq 0 (
    echo Failed to create service
    exit /b 1
)

echo Creating ingress...
kubectl apply -f kubernetes/ingress.yaml
if !ERRORLEVEL! neq 0 (
    echo Failed to create ingress
    exit /b 1
)

echo.
echo === Waiting for deployment rollout ===
kubectl rollout status deployment/crmtestcomp -n crmtestcomp --timeout=300s

echo.
echo === Deployment Status ===
kubectl get pods,svc,ingress -n crmtestcomp

echo.
echo === Application Access ===
echo Application will be available at: http://crmtestcomp.example.com
echo Health check endpoint: http://crmtestcomp.example.com/appinfo/health
echo.
echo Note: Update your DNS to point crmtestcomp.example.com to the ALB address shown above.
echo.
echo === Deployment Complete ===
echo.
echo Rollback command if needed:
echo kubectl rollout undo deployment/crmtestcomp -n crmtestcomp
echo.

pause
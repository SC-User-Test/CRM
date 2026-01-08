@echo off
setlocal enabledelayedexpansion

echo =====================================
echo AWS EKS Deployment Script
echo =====================================
echo.

set PROJECT_NAME=test-km1
set NAMESPACE=test-km1
set KUBE_DIR=kubernetes

echo Project: %PROJECT_NAME%
echo Namespace: %NAMESPACE%
echo.

set /p AWS_REGION="Enter AWS region (e.g., us-east-1): "
if "!AWS_REGION!"==" " (
    echo ERROR: AWS region is required
    exit /b 1
)

set /p CLUSTER_NAME="Enter EKS cluster name: "
if "!CLUSTER_NAME!"==" " (
    echo ERROR: EKS cluster name is required
    exit /b 1
)

echo.
set /p IMAGE_URI="Enter Docker image URI (e.g., account.dkr.ecr.region.amazonaws.com/repo:tag): "
if "!IMAGE_URI!"==" " (
    echo ERROR: Docker image URI is required
    exit /b 1
)

echo.
echo --- Application Configuration ---
echo Press Enter to skip optional environment variables
echo.

set /p DATABASE_URL="Enter DATABASE_URL (or press Enter to skip): "
set /p DATABASE_USERNAME="Enter DATABASE_USERNAME (or press Enter to skip): "
set /p DATABASE_PASSWORD="Enter DATABASE_PASSWORD (or press Enter to skip): "

echo.
echo =====================================
echo Configuring kubectl for EKS cluster
echo =====================================
aws eks update-kubeconfig --region !AWS_REGION! --name !CLUSTER_NAME!

if !ERRORLEVEL! neq 0 (
    echo ERROR: Failed to configure kubectl for EKS cluster
    exit /b 1
)

echo.
echo Verifying cluster connectivity...
kubectl cluster-info
if !ERRORLEVEL! neq 0 (
    echo ERROR: Cannot connect to Kubernetes cluster
    exit /b 1
)

echo.
echo =====================================
echo Updating Kubernetes manifests
echo =====================================

set TMP_DIR=%TEMP%\k8s-deploy-%RANDOM%
mkdir !TMP_DIR!

xcopy /E /I /Y %KUBE_DIR% !TMP_DIR!

powershell -Command "(Get-Content '!TMP_DIR!\deployment.yaml') -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content '!TMP_DIR!\deployment.yaml'"

if "!DATABASE_URL!"==" " (
    set DATABASE_URL=jdbc:mysql://mysql-service:3306/testdb
)
powershell -Command "(Get-Content '!TMP_DIR!\deployment.yaml') -replace '{{DATABASE_URL}}', '!DATABASE_URL!' | Set-Content '!TMP_DIR!\deployment.yaml'"

if "!DATABASE_USERNAME!"==" " (
    set DATABASE_USERNAME=root
)
powershell -Command "(Get-Content '!TMP_DIR!\deployment.yaml') -replace '{{DATABASE_USERNAME}}', '!DATABASE_USERNAME!' | Set-Content '!TMP_DIR!\deployment.yaml'"

if "!DATABASE_PASSWORD!"==" " (
    set DATABASE_PASSWORD=password
)
powershell -Command "(Get-Content '!TMP_DIR!\deployment.yaml') -replace '{{DATABASE_PASSWORD}}', '!DATABASE_PASSWORD!' | Set-Content '!TMP_DIR!\deployment.yaml'"

echo Manifests updated successfully
echo.

echo =====================================
echo Deploying to EKS cluster
echo =====================================
echo.

echo Creating namespace...
kubectl apply -f !TMP_DIR!\namespace.yaml

echo.
echo Deploying application...
kubectl apply -f !TMP_DIR!\deployment.yaml

echo.
echo Creating service...
kubectl apply -f !TMP_DIR!\service.yaml

echo.
echo Creating ingress...
kubectl apply -f !TMP_DIR!\ingress.yaml

echo.
echo =====================================
echo Waiting for deployment rollout
echo =====================================
kubectl rollout status deployment/%PROJECT_NAME% -n %NAMESPACE% --timeout=5m

if !ERRORLEVEL! neq 0 (
    echo ERROR: Deployment rollout failed
    echo.
    echo Checking pod status...
    kubectl get pods -n %NAMESPACE%
    echo.
    echo Checking pod logs...
    kubectl logs -n %NAMESPACE% -l app=%PROJECT_NAME% --tail=50
    rmdir /S /Q !TMP_DIR!
    exit /b 1
)

echo.
echo =====================================
echo Deployment Status
echo =====================================
kubectl get pods,svc,ingress -n %NAMESPACE%

echo.
echo =====================================
echo Deployment successful!
echo =====================================
echo.
echo Application deployed to namespace: %NAMESPACE%
echo.
echo To check application logs:
echo   kubectl logs -n %NAMESPACE% -l app=%PROJECT_NAME% -f
echo.
echo To check pod status:
echo   kubectl get pods -n %NAMESPACE%
echo.
echo To get ingress URL:
echo   kubectl get ingress -n %NAMESPACE%
echo.
echo To rollback deployment:
echo   kubectl rollout undo deployment/%PROJECT_NAME% -n %NAMESPACE%
echo.

rmdir /S /Q !TMP_DIR!
endlocal
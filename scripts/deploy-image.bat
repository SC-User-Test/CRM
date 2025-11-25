@echo off
setlocal enabledelayedexpansion

echo === CRM Application EKS Deployment Script ===

:: Prompt for AWS and EKS configuration
set /p AWS_REGION="Enter AWS region: "
set /p CLUSTER_NAME="Enter EKS cluster name: "
set /p IMAGE_URI="Enter Docker image URI (full path with tag): "

:: Validate inputs
if "!AWS_REGION!"=="" (
    echo Error: AWS region is required.
    exit /b 1
)
if "!CLUSTER_NAME!"=="" (
    echo Error: EKS cluster name is required.
    exit /b 1
)
if "!IMAGE_URI!"=="" (
    echo Error: Docker image URI is required.
    exit /b 1
)

:: Optional environment variables
echo.
echo === Environment Configuration (optional) ===
set /p DB_HOST="Enter database host (or press Enter to skip): "
set /p DB_PORT="Enter database port (default: 3306): "
set /p DB_NAME="Enter database name (default: crm): "

:: Set defaults
if "!DB_PORT!"=="" set DB_PORT=3306
if "!DB_NAME!"=="" set DB_NAME=crm
if "!DB_HOST!"=="" set DB_HOST=mysql-host

echo Configuring kubectl for EKS cluster...
aws eks update-kubeconfig --region "!AWS_REGION!" --name "!CLUSTER_NAME!"

if %ERRORLEVEL% neq 0 (
    echo Failed to configure kubectl. Please check your AWS credentials and cluster name.
    exit /b 1
)

echo Verifying cluster connectivity...
kubectl cluster-info
if %ERRORLEVEL% neq 0 (
    echo Failed to connect to cluster. Please check your configuration.
    exit /b 1
)

echo Updating Kubernetes manifests with deployment values...

:: Update deployment manifest with image URI and environment variables
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{IMAGE_URI}}', '!IMAGE_URI!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_HOST}}', '!DB_HOST!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_PORT}}', '!DB_PORT!' | Set-Content kubernetes\deployment.yaml"
powershell -Command "(Get-Content kubernetes\deployment.yaml) -replace '{{DB_NAME}}', '!DB_NAME!' | Set-Content kubernetes\deployment.yaml"

echo Applying Kubernetes manifests...

echo Creating namespace...
kubectl apply -f kubernetes\namespace.yaml

echo Deploying application...
kubectl apply -f kubernetes\deployment.yaml

echo Creating service...
kubectl apply -f kubernetes\service.yaml

echo Creating ingress...
kubectl apply -f kubernetes\ingress.yaml

echo Waiting for deployment rollout...
kubectl rollout status deployment/crm-app -n crm --timeout=300s

if %ERRORLEVEL% neq 0 (
    echo Deployment rollout failed. Checking pod status...
    kubectl get pods -n crm
    kubectl describe pods -n crm
    exit /b 1
)

echo.
echo === Deployment Status ===
kubectl get pods,svc,ingress -n crm

echo.
echo === Application Access ===
echo Application will be available via the ingress URL once provisioned.
echo Check ingress status: kubectl get ingress -n crm

echo.
echo === Deployment Completed Successfully ===
echo.
echo Useful commands:
echo   View logs: kubectl logs -f deployment/crm-app -n crm
echo   Scale app: kubectl scale deployment/crm-app --replicas=3 -n crm
echo   Delete app: kubectl delete namespace crm

pause
#!/bin/bash
set -e
set -o pipefail

echo "=== CRMTestComp AWS EKS Deployment Script ==="
echo

# Prompt for AWS and EKS configuration
read -p "Enter AWS region: " AWS_REGION
read -p "Enter EKS cluster name: " CLUSTER_NAME
read -p "Enter Docker image URI (full path with tag): " IMAGE_URI

echo
echo "=== Environment Configuration ==="
echo "Optional: Configure environment variables for external services"
echo "(Press Enter to skip any variable)"
echo

# Database configuration
read -p "Enter database host (DB_HOST): " DB_HOST
read -p "Enter database port (DB_PORT, default 3306): " DB_PORT
DB_PORT=${DB_PORT:-3306}
read -p "Enter database name (DB_NAME, default crm): " DB_NAME
DB_NAME=${DB_NAME:-crm}
read -p "Enter database username (DB_USER): " DB_USER
read -s -p "Enter database password (DB_PASSWORD): " DB_PASSWORD
echo

echo
echo "=== Deployment Configuration ==="
echo "AWS Region: $AWS_REGION"
echo "EKS Cluster: $CLUSTER_NAME"
echo "Image URI: $IMAGE_URI"
echo "Database Host: ${DB_HOST:-not configured}"
echo "Database Port: $DB_PORT"
echo "Database Name: $DB_NAME"
echo "Database User: ${DB_USER:-not configured}"
echo

read -p "Proceed with deployment? (y/N): " CONFIRM
if [[ ! "$CONFIRM" =~ ^[Yy]$ ]]; then
    echo "Deployment cancelled."
    exit 0
fi

echo
echo "=== Configuring kubectl ==="
aws eks update-kubeconfig --region "$AWS_REGION" --name "$CLUSTER_NAME"

echo "Verifying cluster connectivity..."
kubectl cluster-info || {
    echo "Failed to connect to EKS cluster. Please check your AWS credentials and cluster name."
    exit 1
}

echo
echo "=== Updating Kubernetes manifests ==="

# Update deployment manifest with image URI and environment variables
sed -i 's|{{IMAGE_URI}}|'"$IMAGE_URI"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_HOST}}|'"${DB_HOST:-localhost}"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_PORT}}|'"$DB_PORT"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_NAME}}|'"$DB_NAME"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_USER}}|'"${DB_USER:-root}"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_PASSWORD}}|'"${DB_PASSWORD:-password}"'|g' kubernetes/deployment.yaml

echo "Manifests updated successfully."

echo
echo "=== Deploying to Kubernetes ==="

echo "Creating namespace..."
kubectl apply -f kubernetes/namespace.yaml

echo "Deploying application..."
kubectl apply -f kubernetes/deployment.yaml

echo "Creating service..."
kubectl apply -f kubernetes/service.yaml

echo "Creating ingress..."
kubectl apply -f kubernetes/ingress.yaml

echo
echo "=== Waiting for deployment rollout ==="
kubectl rollout status deployment/crmtestcomp -n crmtestcomp --timeout=300s

echo
echo "=== Deployment Status ==="
kubectl get pods,svc,ingress -n crmtestcomp

echo
echo "=== Application Access ==="
INGRESS_HOST=$(kubectl get ingress crmtestcomp-ingress -n crmtestcomp -o jsonpath='{.spec.rules[0].host}' 2>/dev/null || echo "crmtestcomp.example.com")
echo "Application will be available at: http://$INGRESS_HOST"
echo "Health check endpoint: http://$INGRESS_HOST/appinfo/health"
echo
echo "Note: Update your DNS to point $INGRESS_HOST to the ALB address shown above."
echo
echo "=== Deployment Complete ==="
echo
echo "Rollback command (if needed):"
echo "kubectl rollout undo deployment/crmtestcomp -n crmtestcomp"
echo
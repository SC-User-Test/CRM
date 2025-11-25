#!/bin/bash
set -e
set -o pipefail

echo "=== CRM Application EKS Deployment Script ==="

# Prompt for AWS and EKS configuration
read -p "Enter AWS region: " AWS_REGION
read -p "Enter EKS cluster name: " CLUSTER_NAME
read -p "Enter Docker image URI (full path with tag): " IMAGE_URI

# Validate inputs
if [ -z "$AWS_REGION" ] || [ -z "$CLUSTER_NAME" ] || [ -z "$IMAGE_URI" ]; then
    echo "Error: All fields are required."
    exit 1
fi

# Optional environment variables
echo "\n=== Environment Configuration (optional) ==="
read -p "Enter database host (or press Enter to skip): " DB_HOST
read -p "Enter database port (default: 3306): " DB_PORT
read -p "Enter database name (default: crm): " DB_NAME

# Set defaults
if [ -z "$DB_PORT" ]; then
    DB_PORT="3306"
fi
if [ -z "$DB_NAME" ]; then
    DB_NAME="crm"
fi
if [ -z "$DB_HOST" ]; then
    DB_HOST="mysql-host"
fi

echo "Configuring kubectl for EKS cluster..."
aws eks update-kubeconfig --region "$AWS_REGION" --name "$CLUSTER_NAME"

if [ $? -ne 0 ]; then
    echo "Failed to configure kubectl. Please check your AWS credentials and cluster name."
    exit 1
fi

echo "Verifying cluster connectivity..."
kubectl cluster-info || {
    echo "Failed to connect to cluster. Please check your configuration."
    exit 1
}

echo "Updating Kubernetes manifests with deployment values..."

# Update deployment manifest with image URI and environment variables
sed -i 's|{{IMAGE_URI}}|'"$IMAGE_URI"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_HOST}}|'"$DB_HOST"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_PORT}}|'"$DB_PORT"'|g' kubernetes/deployment.yaml
sed -i 's|{{DB_NAME}}|'"$DB_NAME"'|g' kubernetes/deployment.yaml

echo "Applying Kubernetes manifests..."

# Apply manifests in order
echo "Creating namespace..."
kubectl apply -f kubernetes/namespace.yaml

echo "Deploying application..."
kubectl apply -f kubernetes/deployment.yaml

echo "Creating service..."
kubectl apply -f kubernetes/service.yaml

echo "Creating ingress..."
kubectl apply -f kubernetes/ingress.yaml

echo "Waiting for deployment rollout..."
kubectl rollout status deployment/crm-app -n crm --timeout=300s

if [ $? -ne 0 ]; then
    echo "Deployment rollout failed. Checking pod status..."
    kubectl get pods -n crm
    kubectl describe pods -n crm
    exit 1
fi

echo "\n=== Deployment Status ==="
kubectl get pods,svc,ingress -n crm

echo "\n=== Application Access ==="
INGRESS_HOST=$(kubectl get ingress crm-app-ingress -n crm -o jsonpath='{.status.loadBalancer.ingress[0].hostname}' 2>/dev/null || echo "pending")
if [ "$INGRESS_HOST" != "pending" ] && [ -n "$INGRESS_HOST" ]; then
    echo "Application URL: http://$INGRESS_HOST"
    echo "Health Check: http://$INGRESS_HOST/appinfo/health"
else
    echo "Ingress is still provisioning. Check back in a few minutes:"
    echo "kubectl get ingress -n crm"
fi

echo "\n=== Deployment Completed Successfully ==="
echo "\nUseful commands:"
echo "  View logs: kubectl logs -f deployment/crm-app -n crm"
echo "  Scale app: kubectl scale deployment/crm-app --replicas=3 -n crm"
echo "  Delete app: kubectl delete namespace crm"
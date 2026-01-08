#!/bin/bash
set -e
set -o pipefail

echo "====================================="
echo "AWS EKS Deployment Script"
echo "====================================="
echo ""

# Project configuration
PROJECT_NAME="test-km1"
NAMESPACE="test-km1"
KUBE_DIR="kubernetes"

echo "Project: $PROJECT_NAME"
echo "Namespace: $NAMESPACE"
echo ""

# Prompt for AWS configuration
read -p "Enter AWS region (e.g., us-east-1): " AWS_REGION
if [ -z "$AWS_REGION" ]; then
    echo "ERROR: AWS region is required"
    exit 1
fi

read -p "Enter EKS cluster name: " CLUSTER_NAME
if [ -z "$CLUSTER_NAME" ]; then
    echo "ERROR: EKS cluster name is required"
    exit 1
fi

echo ""
read -p "Enter Docker image URI (e.g., account.dkr.ecr.region.amazonaws.com/repo:tag): " IMAGE_URI
if [ -z "$IMAGE_URI" ]; then
    echo "ERROR: Docker image URI is required"
    exit 1
fi

echo ""
echo "--- Application Configuration ---"
echo "Press Enter to skip optional environment variables"
echo ""

read -p "Enter DATABASE_URL (or press Enter to skip): " DATABASE_URL
read -p "Enter DATABASE_USERNAME (or press Enter to skip): " DATABASE_USERNAME
read -sp "Enter DATABASE_PASSWORD (or press Enter to skip): " DATABASE_PASSWORD
echo ""

echo ""
echo "====================================="
echo "Configuring kubectl for EKS cluster"
echo "====================================="
aws eks update-kubeconfig --region $AWS_REGION --name $CLUSTER_NAME

if [ $? -ne 0 ]; then
    echo "ERROR: Failed to configure kubectl for EKS cluster"
    exit 1
fi

echo ""
echo "Verifying cluster connectivity..."
kubectl cluster-info || {
    echo "ERROR: Cannot connect to Kubernetes cluster"
    exit 1
}

echo ""
echo "====================================="
echo "Updating Kubernetes manifests"
echo "====================================="

# Create temporary directory for modified manifests
TMP_DIR=$(mktemp -d)
trap "rm -rf $TMP_DIR" EXIT

# Copy manifests to temporary directory
cp -r $KUBE_DIR/* $TMP_DIR/

# Update image URI in deployment
sed -i "s|{{IMAGE_URI}}|$IMAGE_URI|g" $TMP_DIR/deployment.yaml

# Update environment variables if provided
if [ ! -z "$DATABASE_URL" ]; then
    sed -i "s|{{DATABASE_URL}}|$DATABASE_URL|g" $TMP_DIR/deployment.yaml
else
    sed -i "s|{{DATABASE_URL}}|jdbc:mysql://mysql-service:3306/testdb|g" $TMP_DIR/deployment.yaml
fi

if [ ! -z "$DATABASE_USERNAME" ]; then
    sed -i "s|{{DATABASE_USERNAME}}|$DATABASE_USERNAME|g" $TMP_DIR/deployment.yaml
else
    sed -i "s|{{DATABASE_USERNAME}}|root|g" $TMP_DIR/deployment.yaml
fi

if [ ! -z "$DATABASE_PASSWORD" ]; then
    sed -i "s|{{DATABASE_PASSWORD}}|$DATABASE_PASSWORD|g" $TMP_DIR/deployment.yaml
else
    sed -i "s|{{DATABASE_PASSWORD}}|password|g" $TMP_DIR/deployment.yaml
fi

echo "Manifests updated successfully"
echo ""

echo "====================================="
echo "Deploying to EKS cluster"
echo "====================================="
echo ""

echo "Creating namespace..."
kubectl apply -f $TMP_DIR/namespace.yaml

echo ""
echo "Deploying application..."
kubectl apply -f $TMP_DIR/deployment.yaml

echo ""
echo "Creating service..."
kubectl apply -f $TMP_DIR/service.yaml

echo ""
echo "Creating ingress..."
kubectl apply -f $TMP_DIR/ingress.yaml

echo ""
echo "====================================="
echo "Waiting for deployment rollout"
echo "====================================="
kubectl rollout status deployment/$PROJECT_NAME -n $NAMESPACE --timeout=5m

if [ $? -ne 0 ]; then
    echo "ERROR: Deployment rollout failed"
    echo ""
    echo "Checking pod status..."
    kubectl get pods -n $NAMESPACE
    echo ""
    echo "Checking pod logs..."
    kubectl logs -n $NAMESPACE -l app=$PROJECT_NAME --tail=50
    exit 1
fi

echo ""
echo "====================================="
echo "Deployment Status"
echo "====================================="
kubectl get pods,svc,ingress -n $NAMESPACE

echo ""
echo "====================================="
echo "Deployment successful!"
echo "====================================="
echo ""
echo "Application deployed to namespace: $NAMESPACE"
echo ""
echo "To check application logs:"
echo "  kubectl logs -n $NAMESPACE -l app=$PROJECT_NAME -f"
echo ""
echo "To check pod status:"
echo "  kubectl get pods -n $NAMESPACE"
echo ""
echo "To get ingress URL:"
echo "  kubectl get ingress -n $NAMESPACE"
echo ""
echo "To rollback deployment:"
echo "  kubectl rollout undo deployment/$PROJECT_NAME -n $NAMESPACE"
echo ""
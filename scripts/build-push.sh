#!/bin/bash
set -e

echo "=== CRMTestComp Docker Build and Push Script ==="
echo

# Project configuration
PROJECT_NAME="CRMTestComp"

# Sanitize image name
IMAGE_NAME=$(echo "$PROJECT_NAME" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9' '-' | sed 's/^-*//;s/-*$//')
echo "Sanitized image name: $IMAGE_NAME"
echo

# Prompt for image tag
read -p "Enter image tag (default: latest): " IMAGE_TAG
IMAGE_TAG=${IMAGE_TAG:-latest}
IMAGE_TAG=$(echo "$IMAGE_TAG" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9.-' '-' | sed 's/^-*//;s/-*$//')
echo "Using tag: $IMAGE_TAG"
echo

# Registry selection
echo "Select Docker registry:"
echo "1. AWS ECR"
echo "2. Docker Hub"
read -p "Enter choice (1-2): " REGISTRY_CHOICE

case $REGISTRY_CHOICE in
    1)
        echo "=== AWS ECR Configuration ==="
        read -p "Enter AWS region: " AWS_REGION
        read -p "Enter AWS Account ID: " AWS_ACCOUNT_ID
        
        ECR_REPO="$IMAGE_NAME"
        REGISTRY_URL="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"
        FULL_IMAGE_NAME="$REGISTRY_URL/$ECR_REPO:$IMAGE_TAG"
        
        echo "Full image name: $FULL_IMAGE_NAME"
        echo
        
        echo "Authenticating with AWS ECR..."
        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $REGISTRY_URL
        
        echo "Checking if ECR repository exists..."
        aws ecr describe-repositories --repository-names $ECR_REPO --region $AWS_REGION >/dev/null 2>&1 || {
            echo "Creating ECR repository: $ECR_REPO"
            aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION
        }
        ;;
    2)
        echo "=== Docker Hub Configuration ==="
        read -p "Enter Docker Hub username: " DOCKER_USERNAME
        read -s -p "Enter Docker Hub password: " DOCKER_PASSWORD
        echo
        
        FULL_IMAGE_NAME="$DOCKER_USERNAME/$IMAGE_NAME:$IMAGE_TAG"
        
        echo "Full image name: $FULL_IMAGE_NAME"
        echo
        
        echo "Authenticating with Docker Hub..."
        echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin
        ;;
    *)
        echo "Invalid choice. Exiting."
        exit 1
        ;;
esac

echo
echo "Building Docker image..."
docker build -t "$FULL_IMAGE_NAME" .

echo
echo "Pushing Docker image..."
docker push "$FULL_IMAGE_NAME"

echo
echo "=== Build and Push Complete ==="
echo "Image: $FULL_IMAGE_NAME"
echo "You can now use this image URI in your deployment scripts."
echo
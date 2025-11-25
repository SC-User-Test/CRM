#!/bin/bash
set -e
set -o pipefail

echo "=== CRM Application Docker Build and Push Script ==="

# Project configuration
PROJECT_NAME="crm-app"
IMAGE_NAME=$(echo "$PROJECT_NAME" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9' '-' | sed 's/^-*//;s/-*$//')

# Prompt for image tag
echo "Enter image tag (default: latest):"
read -r IMAGE_TAG
if [ -z "$IMAGE_TAG" ]; then
    IMAGE_TAG="latest"
else
    IMAGE_TAG=$(echo "$IMAGE_TAG" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9.-' '-' | sed 's/^-*//;s/-*$//')
fi

echo "Select registry:"
echo "1. AWS ECR"
echo "2. Docker Hub"
read -p "Enter choice (1 or 2): " REGISTRY_CHOICE

if [ "$REGISTRY_CHOICE" = "1" ]; then
    echo "=== AWS ECR Configuration ==="
    read -p "Enter AWS region: " AWS_REGION
    read -p "Enter AWS Account ID: " AWS_ACCOUNT_ID
    read -p "Enter ECR repository name (default: $IMAGE_NAME): " ECR_REPO
    
    if [ -z "$ECR_REPO" ]; then
        ECR_REPO="$IMAGE_NAME"
    fi
    
    REGISTRY_URL="$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"
    FULL_IMAGE_NAME="$REGISTRY_URL/$ECR_REPO:$IMAGE_TAG"
    
    echo "Logging into AWS ECR..."
    aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$REGISTRY_URL"
    
    if [ $? -ne 0 ]; then
        echo "ECR login failed. Please check your AWS credentials and region."
        exit 1
    fi
    
    echo "Checking if ECR repository exists..."
    aws ecr describe-repositories --repository-names "$ECR_REPO" --region "$AWS_REGION" >/dev/null 2>&1 || {
        echo "Creating ECR repository: $ECR_REPO"
        aws ecr create-repository --repository-name "$ECR_REPO" --region "$AWS_REGION"
    }
    
elif [ "$REGISTRY_CHOICE" = "2" ]; then
    echo "=== Docker Hub Configuration ==="
    read -p "Enter Docker Hub username: " DOCKER_USERNAME
    read -s -p "Enter Docker Hub password: " DOCKER_PASSWORD
    echo
    
    FULL_IMAGE_NAME="$DOCKER_USERNAME/$IMAGE_NAME:$IMAGE_TAG"
    
    echo "Logging into Docker Hub..."
    echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin
    
    if [ $? -ne 0 ]; then
        echo "Docker Hub login failed. Please check your credentials."
        exit 1
    fi
else
    echo "Invalid choice. Exiting."
    exit 1
fi

echo "Building Docker image: $FULL_IMAGE_NAME"
docker build -t "$FULL_IMAGE_NAME" .

if [ $? -ne 0 ]; then
    echo "Docker build failed. Please check the Dockerfile and project structure."
    exit 1
fi

echo "Pushing Docker image: $FULL_IMAGE_NAME"
docker push "$FULL_IMAGE_NAME"

if [ $? -ne 0 ]; then
    echo "Docker push failed. Please check your network connection and registry credentials."
    exit 1
fi

echo "=== Build and Push Completed Successfully ==="
echo "Image: $FULL_IMAGE_NAME"
echo "You can now deploy this image using the deploy-image.sh script."
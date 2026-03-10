#!/bin/bash
# === build-push.sh ===
# Build and push Docker image for CompappCRM application

set -e

echo "====================================="
echo "CompappCRM Docker Build & Push Script"
echo "====================================="
echo ""

# Project configuration
PROJECT_NAME="CompappCRM"

# Sanitize project name for Docker tag (lowercase, hyphenate, trim hyphens)
IMAGE_NAME=$(echo "$PROJECT_NAME" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9' '-' | sed 's/^-*//;s/-*$//')

echo "Image name: $IMAGE_NAME"
echo ""

# Prompt for image tag
echo "Enter image tag (default: latest):"
read -r IMAGE_TAG
IMAGE_TAG=${IMAGE_TAG:-latest}

# Sanitize tag (lowercase, hyphenate, trim hyphens)
IMAGE_TAG=$(echo "$IMAGE_TAG" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9.-' '-' | sed 's/^-*//;s/-*$//')

echo "Using tag: $IMAGE_TAG"
echo ""

# Select registry
echo "Select container registry:"
echo "1. AWS ECR (Elastic Container Registry)"
echo "2. Docker Hub"
read -r -p "Enter choice (1 or 2): " REGISTRY_CHOICE

if [ "$REGISTRY_CHOICE" = "1" ]; then
    # AWS ECR
    echo ""
    echo "AWS ECR Configuration"
    echo "---------------------"
    
    read -r -p "Enter AWS Region (e.g., us-east-1): " AWS_REGION
    read -r -p "Enter AWS Account ID: " AWS_ACCOUNT_ID
    read -r -p "Enter ECR Repository Name (default: $IMAGE_NAME): " ECR_REPO
    ECR_REPO=${ECR_REPO:-$IMAGE_NAME}
    
    REGISTRY_URL="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
    FULL_IMAGE_NAME="${REGISTRY_URL}/${ECR_REPO}:${IMAGE_TAG}"
    
    echo ""
    echo "Logging in to AWS ECR..."
    aws ecr get-login-password --region "$AWS_REGION" | docker login --username AWS --password-stdin "$REGISTRY_URL"
    
    if [ $? -ne 0 ]; then
        echo "Error: ECR login failed"
        exit 1
    fi
    
    echo "Checking if ECR repository exists..."
    aws ecr describe-repositories --repository-names "$ECR_REPO" --region "$AWS_REGION" >/dev/null 2>&1 || \
    {
        echo "Repository does not exist. Creating ECR repository: $ECR_REPO"
        aws ecr create-repository --repository-name "$ECR_REPO" --region "$AWS_REGION"
    }
    
elif [ "$REGISTRY_CHOICE" = "2" ]; then
    # Docker Hub
    echo ""
    echo "Docker Hub Configuration"
    echo "------------------------"
    
    read -r -p "Enter Docker Hub username: " DOCKER_USERNAME
    read -r -s -p "Enter Docker Hub password/token: " DOCKER_PASSWORD
    echo ""
    
    FULL_IMAGE_NAME="${DOCKER_USERNAME}/${IMAGE_NAME}:${IMAGE_TAG}"
    
    echo ""
    echo "Logging in to Docker Hub..."
    echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin
    
    if [ $? -ne 0 ]; then
        echo "Error: Docker Hub login failed"
        exit 1
    fi
else
    echo "Invalid choice. Exiting."
    exit 1
fi

echo ""
echo "Building Docker image: $FULL_IMAGE_NAME"
echo "---------------------------------------------"
docker build -t "$FULL_IMAGE_NAME" .

if [ $? -ne 0 ]; then
    echo "Error: Docker build failed"
    exit 1
fi

echo ""
echo "Pushing image to registry..."
echo "-----------------------------"
docker push "$FULL_IMAGE_NAME"

if [ $? -ne 0 ]; then
    echo "Error: Docker push failed"
    exit 1
fi

echo ""
echo "====================================="
echo "SUCCESS!"
echo "====================================="
echo "Image: $FULL_IMAGE_NAME"
echo ""
echo "Use this image URI for deployment."
echo "====================================="
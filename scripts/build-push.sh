#!/bin/bash
set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}CRM Application - Build and Push Script${NC}"
echo "=========================================="

# Get project name from directory or use default
PROJECT_NAME=${PWD##*/}
if [ -z "$PROJECT_NAME" ]; then
    PROJECT_NAME="crm"
fi

# Sanitize project name for Docker
IMAGE_NAME=$(echo "$PROJECT_NAME" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9' '-' | sed 's/^-*//;s/-*$//')

echo -e "${YELLOW}Project: $PROJECT_NAME${NC}"
echo -e "${YELLOW}Image name: $IMAGE_NAME${NC}"
echo ""

# Prompt for image tag
read -p "Enter image tag (default: latest): " IMAGE_TAG
if [ -z "$IMAGE_TAG" ]; then
    IMAGE_TAG="latest"
fi

# Sanitize tag
IMAGE_TAG=$(echo "$IMAGE_TAG" | tr '[:upper:]' '[:lower:]' | tr -cs 'a-z0-9.-' '-' | sed 's/^-*//;s/-*$//')
if [ -z "$IMAGE_TAG" ]; then
    IMAGE_TAG="latest"
fi

echo -e "${YELLOW}Using tag: $IMAGE_TAG${NC}"
echo ""

# Registry selection
echo "Select container registry:"
echo "1. AWS ECR"
echo "2. Docker Hub"
read -p "Enter choice (1-2): " REGISTRY_CHOICE

case $REGISTRY_CHOICE in
    1)
        echo -e "${GREEN}Selected: AWS ECR${NC}"
        read -p "Enter AWS region (default: us-east-1): " AWS_REGION
        if [ -z "$AWS_REGION" ]; then
            AWS_REGION="us-east-1"
        fi
        
        read -p "Enter ECR repository name (default: $IMAGE_NAME): " ECR_REPO
        if [ -z "$ECR_REPO" ]; then
            ECR_REPO="$IMAGE_NAME"
        fi
        
        # Get AWS Account ID
        ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
        if [ $? -ne 0 ]; then
            echo -e "${RED}Failed to get AWS Account ID. Please check AWS CLI configuration.${NC}"
            exit 1
        fi
        
        REGISTRY_URL="$ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"
        FULL_IMAGE_NAME="$REGISTRY_URL/$ECR_REPO:$IMAGE_TAG"
        
        echo -e "${YELLOW}Registry URL: $REGISTRY_URL${NC}"
        echo -e "${YELLOW}Full image name: $FULL_IMAGE_NAME${NC}"
        echo ""
        
        # ECR Login
        echo -e "${YELLOW}Logging into ECR...${NC}"
        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $REGISTRY_URL
        if [ $? -ne 0 ]; then
            echo -e "${RED}ECR login failed${NC}"
            exit 1
        fi
        
        # Check if repository exists, create if it doesn't
        echo -e "${YELLOW}Checking ECR repository...${NC}"
        aws ecr describe-repositories --repository-names $ECR_REPO --region $AWS_REGION >/dev/null 2>&1 || {
            echo -e "${YELLOW}Creating ECR repository: $ECR_REPO${NC}"
            aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION
            if [ $? -ne 0 ]; then
                echo -e "${RED}Failed to create ECR repository${NC}"
                exit 1
            fi
        }
        ;;
    2)
        echo -e "${GREEN}Selected: Docker Hub${NC}"
        read -p "Enter Docker Hub username: " DOCKER_USERNAME
        read -s -p "Enter Docker Hub password: " DOCKER_PASSWORD
        echo ""
        
        if [ -z "$DOCKER_USERNAME" ] || [ -z "$DOCKER_PASSWORD" ]; then
            echo -e "${RED}Docker Hub credentials are required${NC}"
            exit 1
        fi
        
        FULL_IMAGE_NAME="$DOCKER_USERNAME/$IMAGE_NAME:$IMAGE_TAG"
        
        echo -e "${YELLOW}Full image name: $FULL_IMAGE_NAME${NC}"
        echo ""
        
        # Docker Hub Login
        echo -e "${YELLOW}Logging into Docker Hub...${NC}"
        echo $DOCKER_PASSWORD | docker login --username $DOCKER_USERNAME --password-stdin
        if [ $? -ne 0 ]; then
            echo -e "${RED}Docker Hub login failed${NC}"
            exit 1
        fi
        ;;
    *)
        echo -e "${RED}Invalid choice${NC}"
        exit 1
        ;;
esac

# Build Docker image
echo -e "${YELLOW}Building Docker image...${NC}"
docker build -t $FULL_IMAGE_NAME .
if [ $? -ne 0 ]; then
    echo -e "${RED}Docker build failed${NC}"
    exit 1
fi

# Push Docker image
echo -e "${YELLOW}Pushing Docker image...${NC}"
docker push $FULL_IMAGE_NAME
if [ $? -ne 0 ]; then
    echo -e "${RED}Docker push failed${NC}"
    exit 1
fi

echo -e "${GREEN}Successfully built and pushed: $FULL_IMAGE_NAME${NC}"
echo ""
echo "Next steps:"
echo "1. Update your deployment configuration with the image URI"
echo "2. Deploy to your target platform (ECS, Kubernetes, etc.)"
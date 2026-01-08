# Deployment Guide - test-km1

This comprehensive guide covers building, containerizing, and deploying the test-km1 Java Spring Boot application to AWS EKS (Elastic Kubernetes Service).

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Technology Stack](#technology-stack)
3. [Local Development Setup](#local-development-setup)
4. [Docker Build and Push](#docker-build-and-push)
5. [AWS EKS Deployment](#aws-eks-deployment)
6. [Configuration Management](#configuration-management)
7. [Monitoring and Troubleshooting](#monitoring-and-troubleshooting)
8. [Scaling and Updates](#scaling-and-updates)
9. [Security Considerations](#security-considerations)
10. [Rollback Procedures](#rollback-procedures)

---

## Prerequisites

### Required Tools

#### For Local Development:
- **Docker**: Version 20.10 or later
  - Installation: https://docs.docker.com/get-docker/
- **Docker Compose**: Version 2.0 or later
  - Installation: https://docs.docker.com/compose/install/
- **Java Development Kit (JDK)**: Version 8
  - Installation: https://adoptium.net/
- **Maven**: Version 3.6 or later
  - Installation: https://maven.apache.org/install.html

#### For AWS EKS Deployment:
- **AWS CLI**: Version 2.x
  - Installation: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
  - Configuration: `aws configure`
- **kubectl**: Version 1.24 or later
  - Installation: https://kubernetes.io/docs/tasks/tools/
- **eksctl** (optional, for cluster creation): Version 0.140 or later
  - Installation: https://eksctl.io/introduction/#installation

### AWS Prerequisites

1. **AWS Account** with appropriate permissions
2. **IAM User/Role** with the following permissions:
   - EKS: Full access or specific cluster access
   - ECR: Push/pull images
   - EC2: Describe instances, security groups
   - VPC: Describe VPC, subnets
   - CloudFormation: Create/update stacks (if using eksctl)

3. **EKS Cluster** (if not already created):
   ```bash
   eksctl create cluster \
     --name test-km1-cluster \
     --region us-east-1 \
     --nodegroup-name standard-workers \
     --node-type t3.medium \
     --nodes 3 \
     --nodes-min 1 \
     --nodes-max 5 \
     --managed
   ```

4. **AWS Load Balancer Controller** installed on EKS cluster:
   ```bash
   eksctl utils associate-iam-oidc-provider --region=us-east-1 --cluster=test-km1-cluster --approve
   
   eksctl create iamserviceaccount \
     --cluster=test-km1-cluster \
     --namespace=kube-system \
     --name=aws-load-balancer-controller \
     --attach-policy-arn=arn:aws:iam::aws:policy/ElasticLoadBalancingFullAccess \
     --override-existing-serviceaccounts \
     --approve
   
   helm repo add eks https://aws.github.io/eks-charts
   helm repo update
   helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
     -n kube-system \
     --set clusterName=test-km1-cluster \
     --set serviceAccount.create=false \
     --set serviceAccount.name=aws-load-balancer-controller
   ```

---

## Technology Stack

### Application Details
- **Framework**: Spring Boot
- **Java Version**: 8
- **Build Tool**: Maven
- **Package Type**: JAR
- **Application Port**: 8080
- **Health Endpoint**: /actuator/health

### Container Configuration
- **Base Image (Runtime)**: amazoncorretto:8
- **Builder Image**: maven:3.8.6-openjdk-8-slim
- **Multi-stage Build**: Yes

### Deployment Platform
- **Target**: AWS EKS (Elastic Kubernetes Service)
- **Orchestration**: Kubernetes
- **Ingress**: AWS Application Load Balancer (ALB)

---

## Local Development Setup

### 1. Build Application Locally

```bash
# Navigate to project directory
cd /modernize-data/studio-data/TNT1001/APP2835/transformed-code/955/studio-workspace/test_km1

# Build with Maven
mvn clean package -DskipTests

# Run application
java -jar target/*.jar

# Test application
curl http://localhost:8080/actuator/health
```

### 2. Run with Docker Compose

```bash
# Build and start application
docker-compose up --build

# Run in detached mode
docker-compose up -d

# View logs
docker-compose logs -f test-km1

# Stop application
docker-compose down
```

### 3. Environment Variables

Create a `.env` file in the project root:

```env
# Database Configuration
DATABASE_URL=jdbc:mysql://host.docker.internal:3306/testdb
DATABASE_USERNAME=root
DATABASE_PASSWORD=password

# Spring Configuration
SPRING_PROFILES_ACTIVE=docker

# JVM Configuration
JAVA_OPTS=-Xmx512m -Xms256m
```

---

## Docker Build and Push

### Option 1: Using Build Scripts (Recommended)

#### Linux/Mac:
```bash
chmod +x scripts/build-push.sh
./scripts/build-push.sh
```

#### Windows:
```cmd
scripts\build-push.bat
```

The script will:
1. Prompt for registry selection (AWS ECR or Docker Hub)
2. Request necessary credentials
3. Build the Docker image
4. Authenticate with the selected registry
5. Push the image

### Option 2: Manual Docker Build and Push

#### AWS ECR:

```bash
# Set variables
export AWS_REGION=us-east-1
export AWS_ACCOUNT_ID=123456789012
export ECR_REPO=test-km1
export IMAGE_TAG=latest

# Authenticate Docker to ECR
aws ecr get-login-password --region $AWS_REGION | \
  docker login --username AWS --password-stdin \
  $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com

# Create ECR repository (if it doesn't exist)
aws ecr create-repository --repository-name $ECR_REPO --region $AWS_REGION || true

# Build image
docker build -t $ECR_REPO:$IMAGE_TAG .

# Tag image
docker tag $ECR_REPO:$IMAGE_TAG \
  $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:$IMAGE_TAG

# Push image
docker push $AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com/$ECR_REPO:$IMAGE_TAG
```

#### Docker Hub:

```bash
# Set variables
export DOCKER_USERNAME=your-username
export IMAGE_TAG=latest

# Login to Docker Hub
docker login -u $DOCKER_USERNAME

# Build image
docker build -t test-km1:$IMAGE_TAG .

# Tag image
docker tag test-km1:$IMAGE_TAG $DOCKER_USERNAME/test-km1:$IMAGE_TAG

# Push image
docker push $DOCKER_USERNAME/test-km1:$IMAGE_TAG
```

---

## AWS EKS Deployment

### Quick Deployment with Scripts

#### Linux/Mac:
```bash
chmod +x scripts/deploy-image.sh
./scripts/deploy-image.sh
```

#### Windows:
```cmd
scripts\deploy-image.bat
```

The deployment script will:
1. Configure kubectl for your EKS cluster
2. Update Kubernetes manifests with your image URI
3. Apply namespace, deployment, service, and ingress configurations
4. Wait for deployment to complete
5. Display deployment status and access information

### Manual Deployment Steps

#### 1. Configure kubectl

```bash
aws eks update-kubeconfig --region us-east-1 --name test-km1-cluster

# Verify connection
kubectl cluster-info
kubectl get nodes
```

#### 2. Update Kubernetes Manifests

Edit `kubernetes/deployment.yaml` and replace placeholders:

```yaml
image: 123456789012.dkr.ecr.us-east-1.amazonaws.com/test-km1:latest

env:
- name: DATABASE_URL
  value: "jdbc:mysql://mysql-host:3306/testdb"
- name: DATABASE_USERNAME
  value: "your-username"
- name: DATABASE_PASSWORD
  value: "your-password"
```

#### 3. Apply Kubernetes Resources

```bash
# Create namespace
kubectl apply -f kubernetes/namespace.yaml

# Deploy application
kubectl apply -f kubernetes/deployment.yaml

# Create service
kubectl apply -f kubernetes/service.yaml

# Create ingress
kubectl apply -f kubernetes/ingress.yaml

# Wait for rollout
kubectl rollout status deployment/test-km1 -n test-km1
```

#### 4. Verify Deployment

```bash
# Check pods
kubectl get pods -n test-km1

# Check services
kubectl get svc -n test-km1

# Check ingress
kubectl get ingress -n test-km1

# View logs
kubectl logs -n test-km1 -l app=test-km1 -f
```

#### 5. Get Application URL

```bash
# Get ALB DNS name
kubectl get ingress test-km1-ingress -n test-km1 -o jsonpath='{.status.loadBalancer.ingress[0].hostname}'

# Test application
curl http://<alb-dns-name>/actuator/health
```

---

## Configuration Management

### Environment Variables

Update deployment configuration using ConfigMaps or Secrets:

#### Using ConfigMap:

```bash
kubectl create configmap test-km1-config \
  --from-literal=DATABASE_URL=jdbc:mysql://mysql-host:3306/testdb \
  -n test-km1
```

Update `deployment.yaml`:
```yaml
env:
- name: DATABASE_URL
  valueFrom:
    configMapKeyRef:
      name: test-km1-config
      key: DATABASE_URL
```

#### Using Secrets:

```bash
kubectl create secret generic test-km1-secrets \
  --from-literal=DATABASE_PASSWORD=your-password \
  -n test-km1
```

Update `deployment.yaml`:
```yaml
env:
- name: DATABASE_PASSWORD
  valueFrom:
    secretKeyRef:
      name: test-km1-secrets
      key: DATABASE_PASSWORD
```

### Spring Profiles

Change active profile:
```yaml
env:
- name: SPRING_PROFILES_ACTIVE
  value: "production"
```

---

## Monitoring and Troubleshooting

### View Logs

```bash
# All pods in namespace
kubectl logs -n test-km1 -l app=test-km1 --tail=100

# Follow logs
kubectl logs -n test-km1 -l app=test-km1 -f

# Specific pod
kubectl logs -n test-km1 <pod-name>

# Previous container logs (if pod crashed)
kubectl logs -n test-km1 <pod-name> --previous
```

### Pod Troubleshooting

```bash
# Describe pod
kubectl describe pod -n test-km1 <pod-name>

# Get pod events
kubectl get events -n test-km1 --sort-by='.lastTimestamp'

# Execute commands in pod
kubectl exec -it -n test-km1 <pod-name> -- /bin/sh

# Check pod resource usage
kubectl top pod -n test-km1
```

### Common Issues

#### ImagePullBackOff:
```bash
# Check image name and tag
kubectl describe pod -n test-km1 <pod-name> | grep Image

# Verify ECR authentication
aws ecr get-login-password --region us-east-1
```

#### CrashLoopBackOff:
```bash
# Check application logs
kubectl logs -n test-km1 <pod-name> --previous

# Verify environment variables
kubectl exec -n test-km1 <pod-name> -- env
```

#### Service Not Accessible:
```bash
# Check service endpoints
kubectl get endpoints -n test-km1

# Test service from within cluster
kubectl run -it --rm debug --image=busybox --restart=Never -- \
  wget -O- http://test-km1-service.test-km1.svc.cluster.local
```

---

## Scaling and Updates

### Manual Scaling

```bash
# Scale deployment
kubectl scale deployment test-km1 -n test-km1 --replicas=5

# Verify scaling
kubectl get pods -n test-km1
```

### Horizontal Pod Autoscaler (HPA)

The HPA is pre-configured in `deployment.yaml`:
- Min replicas: 2
- Max replicas: 10
- Target CPU: 70%
- Target Memory: 80%

```bash
# Check HPA status
kubectl get hpa -n test-km1

# Describe HPA
kubectl describe hpa test-km1-hpa -n test-km1
```

### Rolling Updates

```bash
# Update image
kubectl set image deployment/test-km1 \
  test-km1=<new-image-uri> \
  -n test-km1

# Watch rollout
kubectl rollout status deployment/test-km1 -n test-km1

# Check rollout history
kubectl rollout history deployment/test-km1 -n test-km1
```

---

## Security Considerations

### Container Security

1. **Non-root User**: Application runs as non-root user (`appuser`)
2. **Image Scanning**: Scan images before deployment
   ```bash
   aws ecr start-image-scan --repository-name test-km1 --image-id imageTag=latest
   ```

### Network Security

1. **Network Policies**: Implement Kubernetes Network Policies
2. **TLS/SSL**: Configure HTTPS in ingress with ACM certificates
3. **Security Groups**: Restrict EKS cluster access

### Secrets Management

1. **AWS Secrets Manager Integration**:
   ```bash
   # Install Secrets Store CSI Driver
   helm repo add secrets-store-csi-driver https://kubernetes-sigs.github.io/secrets-store-csi-driver/charts
   helm install csi-secrets-store secrets-store-csi-driver/secrets-store-csi-driver --namespace kube-system
   ```

2. **Encrypt Kubernetes Secrets**: Enable encryption at rest in EKS

### IAM Roles for Service Accounts (IRSA)

```bash
# Create IAM role for application
eksctl create iamserviceaccount \
  --name test-km1-sa \
  --namespace test-km1 \
  --cluster test-km1-cluster \
  --attach-policy-arn arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess \
  --approve
```

---

## Rollback Procedures

### Rollback Deployment

```bash
# Rollback to previous version
kubectl rollout undo deployment/test-km1 -n test-km1

# Rollback to specific revision
kubectl rollout undo deployment/test-km1 -n test-km1 --to-revision=2

# Check rollback status
kubectl rollout status deployment/test-km1 -n test-km1
```

### Verify Rollback

```bash
# Check running pods
kubectl get pods -n test-km1

# Verify application health
kubectl exec -n test-km1 <pod-name> -- curl -s http://localhost:8080/actuator/health
```

---

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [AWS EKS Documentation](https://docs.aws.amazon.com/eks/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

## Support and Troubleshooting

For issues or questions:
1. Check application logs: `kubectl logs -n test-km1 -l app=test-km1`
2. Review pod status: `kubectl describe pod -n test-km1 <pod-name>`
3. Verify cluster health: `kubectl get nodes`
4. Check AWS EKS console for cluster insights

---

**Document Version**: 1.0  
**Last Updated**: 2026-01-08  
**Application**: test-km1  
**Platform**: AWS EKS
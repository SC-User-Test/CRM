# CRMTestComp Deployment Guide

This guide provides comprehensive instructions for deploying the CRMTestComp Java Spring Boot application using Docker and AWS EKS.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Docker Containerization](#docker-containerization)
4. [AWS EKS Deployment](#aws-eks-deployment)
5. [Configuration Management](#configuration-management)
6. [Troubleshooting](#troubleshooting)
7. [Security Considerations](#security-considerations)
8. [Technology-Specific Notes](#technology-specific-notes)

## Prerequisites

### System Requirements

- **Java**: OpenJDK 8 or Amazon Corretto 8
- **Maven**: 3.6.0 or higher
- **Docker**: 20.10.0 or higher
- **Docker Compose**: 1.29.0 or higher
- **AWS CLI**: 2.0 or higher
- **kubectl**: 1.21 or higher
- **eksctl**: 0.100 or higher (optional, for cluster creation)

### AWS Requirements

- AWS Account with appropriate permissions
- IAM user with the following policies:
  - `AmazonEKSClusterPolicy`
  - `AmazonEKSWorkerNodePolicy`
  - `AmazonEKS_CNI_Policy`
  - `AmazonEC2ContainerRegistryFullAccess`
  - `ElasticLoadBalancingFullAccess`
- AWS CLI configured with credentials

### Application Requirements

- **Database**: MySQL 5.7+ or compatible database
- **Memory**: Minimum 1GB RAM for container
- **Storage**: 10GB available disk space
- **Network**: Ports 8080 (application) accessible

## Local Development Setup

### 1. Clone and Build

```bash
# Navigate to project directory
cd /modernize-data/studio-data/TNT1001/APP1083/transformed-code/50/studio-workspace/CRMTestComp

# Build the application
mvn clean package -DskipTests

# Run locally
mvn spring-boot:run
```

### 2. Local Docker Setup

```bash
# Build Docker image
docker build -t crmtestcomp:latest .

# Run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f crmtestcomp

# Stop services
docker-compose down
```

### 3. Access Application

- **Application**: http://localhost:8080
- **Health Check**: http://localhost:8080/appinfo/health
- **Management Endpoints**: http://localhost:8080/appinfo

## Docker Containerization

### Build and Push Images

#### Linux/macOS

```bash
# Make script executable
chmod +x scripts/build-push.sh

# Run build and push script
./scripts/build-push.sh
```

#### Windows

```cmd
# Run build and push script
scripts\build-push.bat
```

The script will:
1. Prompt for registry selection (AWS ECR or Docker Hub)
2. Collect registry credentials and configuration
3. Build the Docker image with multi-stage build
4. Push to the selected registry
5. Provide the image URI for deployment

### Manual Docker Commands

```bash
# Build image
docker build -t crmtestcomp:latest .

# Tag for registry
docker tag crmtestcomp:latest <registry>/<repository>:latest

# Push to registry
docker push <registry>/<repository>:latest
```

## AWS EKS Deployment

### 1. EKS Cluster Setup

#### Option A: Using eksctl (Recommended)

```bash
# Create EKS cluster
eksctl create cluster \
  --name crmtestcomp-cluster \
  --region us-west-2 \
  --node-type t3.medium \
  --nodes 2 \
  --nodes-min 1 \
  --nodes-max 4 \
  --managed

# Install AWS Load Balancer Controller
export CLUSTER_NAME=crmtestcomp-cluster
export AWS_REGION=us-west-2

# Create IAM OIDC provider
eksctl utils associate-iam-oidc-provider \
  --region $AWS_REGION \
  --cluster $CLUSTER_NAME \
  --approve

# Create IAM service account for AWS Load Balancer Controller
eksctl create iamserviceaccount \
  --cluster=$CLUSTER_NAME \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --role-name="AmazonEKSLoadBalancerControllerRole" \
  --attach-policy-arn=arn:aws:iam::aws:policy/ElasticLoadBalancingFullAccess \
  --approve

# Install AWS Load Balancer Controller
helm repo add eks https://aws.github.io/eks-charts
helm repo update
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=$CLUSTER_NAME \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller
```

#### Option B: Using AWS Console

1. Navigate to AWS EKS console
2. Create new cluster with the following settings:
   - **Name**: crmtestcomp-cluster
   - **Kubernetes version**: 1.27 or later
   - **Service role**: EKS service role
   - **VPC**: Default or custom VPC
   - **Subnets**: At least 2 subnets in different AZs
3. Create managed node group:
   - **Instance type**: t3.medium
   - **Scaling configuration**: 1-4 nodes
   - **Node group role**: EKS node group role

### 2. Deploy Application

#### Linux/macOS

```bash
# Make script executable
chmod +x scripts/deploy-image.sh

# Run deployment script
./scripts/deploy-image.sh
```

#### Windows

```cmd
# Run deployment script
scripts\deploy-image.bat
```

The deployment script will:
1. Prompt for AWS region and EKS cluster name
2. Prompt for Docker image URI
3. Collect database configuration
4. Update Kubernetes manifests
5. Deploy all resources to EKS
6. Verify deployment status
7. Provide access URLs

### 3. Manual Kubernetes Deployment

```bash
# Configure kubectl
aws eks update-kubeconfig --region <region> --name <cluster-name>

# Apply manifests
kubectl apply -f kubernetes/namespace.yaml
kubectl apply -f kubernetes/deployment.yaml
kubectl apply -f kubernetes/service.yaml
kubectl apply -f kubernetes/ingress.yaml

# Check deployment status
kubectl get pods -n crmtestcomp
kubectl rollout status deployment/crmtestcomp -n crmtestcomp
```

## Configuration Management

### Environment Variables

The application supports the following environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SPRING_PROFILES_ACTIVE` | Spring Boot profile | `docker` | No |
| `DB_HOST` | Database hostname | `localhost` | Yes* |
| `DB_PORT` | Database port | `3306` | No |
| `DB_NAME` | Database name | `crm` | No |
| `DB_USER` | Database username | `root` | Yes* |
| `DB_PASSWORD` | Database password | `password` | Yes* |
| `JAVA_OPTS` | JVM options | See below | No |

*Required for production deployments

### JVM Configuration

Default JVM options for containerized deployment:

```bash
JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
```

### Database Setup

The application requires a MySQL-compatible database:

```sql
-- Create database
CREATE DATABASE crm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user (optional)
CREATE USER 'crm_user'@'%' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON crm.* TO 'crm_user'@'%';
FLUSH PRIVILEGES;
```

### Spring Boot Profiles

The application includes the following profiles:

- **default**: Local development with H2 database
- **docker**: Docker container deployment
- **kubernetes**: Kubernetes deployment

## Troubleshooting

### Common Issues

#### 1. Pod CrashLoopBackOff

```bash
# Check pod logs
kubectl logs -n crmtestcomp deployment/crmtestcomp

# Describe pod for events
kubectl describe pods -n crmtestcomp -l app=crmtestcomp
```

**Common causes:**
- Database connection issues
- Insufficient memory allocation
- Missing environment variables
- Image pull errors

#### 2. Service Not Accessible

```bash
# Check service endpoints
kubectl get endpoints -n crmtestcomp

# Port forward for debugging
kubectl port-forward -n crmtestcomp service/crmtestcomp-service 8080:80
```

#### 3. Ingress Issues

```bash
# Check ingress status
kubectl describe ingress -n crmtestcomp crmtestcomp-ingress

# Check AWS Load Balancer Controller logs
kubectl logs -n kube-system deployment/aws-load-balancer-controller
```

#### 4. Database Connection Errors

- Verify database credentials and connectivity
- Check security groups and network ACLs
- Ensure database is accessible from EKS subnets
- Verify environment variables are correctly set

### Debugging Commands

```bash
# Get all resources
kubectl get all -n crmtestcomp

# Check resource quotas
kubectl describe quota -n crmtestcomp

# View events
kubectl get events -n crmtestcomp --sort-by=.metadata.creationTimestamp

# Execute into pod
kubectl exec -it -n crmtestcomp deployment/crmtestcomp -- /bin/bash

# Check application logs
kubectl logs -f -n crmtestcomp deployment/crmtestcomp
```

### Performance Tuning

#### Memory Issues

```yaml
# Increase memory limits in deployment.yaml
resources:
  limits:
    memory: "2Gi"
  requests:
    memory: "1Gi"
```

#### CPU Issues

```yaml
# Increase CPU limits in deployment.yaml
resources:
  limits:
    cpu: "1000m"
  requests:
    cpu: "500m"
```

## Security Considerations

### Container Security

- Non-root user execution (UID 1000)
- No privileged escalation
- Read-only root filesystem where possible
- Minimal base image (Amazon Corretto)

### Kubernetes Security

- Pod Security Standards enforcement
- Network policies for traffic control
- RBAC for service account permissions
- Secrets management for sensitive data

### Recommendations

1. **Use AWS Secrets Manager** for database credentials
2. **Enable Pod Security Policies** or Pod Security Standards
3. **Implement Network Policies** to restrict pod communication
4. **Regular security scanning** of container images
5. **Monitor and audit** cluster activities

### Secrets Management

```bash
# Create secret for database credentials
kubectl create secret generic db-credentials \
  --from-literal=username=crm_user \
  --from-literal=password=secure_password \
  -n crmtestcomp

# Reference in deployment
env:
- name: DB_USER
  valueFrom:
    secretKeyRef:
      name: db-credentials
      key: username
```

## Technology-Specific Notes

### Spring Boot 1.5.10 (Legacy Version)

**Important**: This application uses Spring Boot 1.5.10, which is an older version. Consider upgrading for:

- Security updates and patches
- Better Kubernetes integration
- Improved monitoring and observability
- Cloud-native features

#### Key Differences from Modern Spring Boot:

- Management endpoints at `/appinfo` instead of `/actuator`
- Different configuration property names
- Legacy security configuration
- Older dependency versions

#### Health Check Configuration

```properties
# Spring Boot 1.5.x health check configuration
management.context-path=/appinfo
management.security.enabled=false
endpoints.health.enabled=true
```

### Java 8 Considerations

- Extended support lifecycle ending
- Consider migration to Java 11 or 17
- Security updates available through Oracle or OpenJDK
- Container optimizations available in newer JDK versions

### Maven Build Optimization

```xml
<!-- Recommended Maven properties for containerization -->
<properties>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <spring-boot.repackage.skip>false</spring-boot.repackage.skip>
</properties>
```

### Performance Monitoring

#### JVM Metrics (Spring Boot 1.5.x)

- **Heap Usage**: `/appinfo/metrics/mem`
- **GC Information**: `/appinfo/metrics/gc.*`
- **Thread Information**: `/appinfo/metrics/threads`

#### Application Metrics

- **HTTP Requests**: `/appinfo/metrics/counter.status.*`
- **Response Times**: `/appinfo/metrics/gauge.response.*`
- **Database Connections**: Custom metrics through DataSource

---

## Support and Maintenance

### Monitoring Setup

1. Configure application monitoring with Prometheus and Grafana
2. Set up log aggregation with ELK stack or AWS CloudWatch
3. Implement alerting for critical metrics
4. Regular health checks and uptime monitoring

### Backup and Recovery

1. Database backup procedures
2. Application configuration backup
3. Kubernetes manifest versioning
4. Disaster recovery procedures

### Scaling

```bash
# Scale deployment
kubectl scale deployment crmtestcomp --replicas=5 -n crmtestcomp

# Set up Horizontal Pod Autoscaler
kubectl autoscale deployment crmtestcomp --cpu-percent=70 --min=2 --max=10 -n crmtestcomp
```

For additional support, refer to the official documentation:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [AWS EKS Documentation](https://docs.aws.amazon.com/eks/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)

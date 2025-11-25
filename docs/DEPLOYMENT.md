# CRM Application Deployment Guide

This guide provides comprehensive instructions for deploying the Spring Boot CRM application using Docker and Kubernetes on AWS EKS.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Docker Deployment](#docker-deployment)
4. [AWS EKS Deployment](#aws-eks-deployment)
5. [Configuration Management](#configuration-management)
6. [Troubleshooting](#troubleshooting)
7. [Security Considerations](#security-considerations)
8. [Technology-Specific Notes](#technology-specific-notes)

## Prerequisites

### System Requirements

- Java 8 or higher
- Maven 3.6 or higher
- Docker 20.10 or higher
- Docker Compose 1.28 or higher

### AWS EKS Requirements

- AWS CLI v2
- kubectl 1.21 or higher
- eksctl (optional, for cluster creation)
- Valid AWS credentials with appropriate permissions

### Required AWS Permissions

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "eks:DescribeCluster",
                "eks:ListClusters",
                "ecr:GetAuthorizationToken",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetDownloadUrlForLayer",
                "ecr:BatchGetImage",
                "ecr:CreateRepository",
                "ecr:DescribeRepositories"
            ],
            "Resource": "*"
        }
    ]
}
```

## Local Development Setup

### 1. Clone and Build

```bash
# Clone the repository
git clone <repository-url>
cd CRM-003

# Build the application
mvn clean package -DskipTests
```

### 2. Run with Docker Compose

```bash
# Build and start the application
docker-compose up --build

# Access the application
open http://localhost:8080

# Check health status
curl http://localhost:8080/appinfo/health
```

### 3. Environment Configuration

Create a `.env` file for local development:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=crm
DB_USERNAME=root
DB_PASSWORD=password
SPRING_PROFILES_ACTIVE=docker
JAVA_OPTS=-Xmx512m -Xms256m
```

## Docker Deployment

### 1. Build Docker Image

```bash
# Using the build script
./scripts/build-push.sh

# Or manually
docker build -t crm-app:latest .
```

### 2. Run Container

```bash
# Run with environment variables
docker run -d \
  --name crm-app \
  -p 8080:8080 \
  -e DB_HOST=your-db-host \
  -e DB_USERNAME=your-username \
  -e DB_PASSWORD=your-password \
  crm-app:latest
```

### 3. Verify Deployment

```bash
# Check container status
docker ps

# View logs
docker logs crm-app

# Test health endpoint
curl http://localhost:8080/appinfo/health
```

## AWS EKS Deployment

### 1. Cluster Setup

#### Option A: Using eksctl (Recommended)

```bash
# Create EKS cluster
eksctl create cluster \
  --name crm-cluster \
  --region us-west-2 \
  --nodegroup-name workers \
  --node-type t3.medium \
  --nodes 2 \
  --nodes-min 1 \
  --nodes-max 4
```

#### Option B: Using AWS Console

1. Navigate to AWS EKS Console
2. Create new cluster with the following specifications:
   - Kubernetes version: 1.21 or higher
   - Node group: t3.medium instances
   - Desired capacity: 2 nodes

### 2. Configure kubectl

```bash
# Update kubeconfig
aws eks update-kubeconfig --region us-west-2 --name crm-cluster

# Verify connection
kubectl cluster-info
kubectl get nodes
```

### 3. Build and Push Image

```bash
# Build and push to ECR
./scripts/build-push.sh

# Select option 1 (AWS ECR)
# Enter your AWS region and account ID
# Repository will be created automatically
```

### 4. Deploy Application

```bash
# Deploy to EKS
./scripts/deploy-image.sh

# Follow prompts for:
# - AWS region
# - EKS cluster name
# - Docker image URI
# - Database configuration
```

### 5. Verify EKS Deployment

```bash
# Check deployment status
kubectl get pods -n crm
kubectl get svc -n crm
kubectl get ingress -n crm

# View application logs
kubectl logs -f deployment/crm-app -n crm

# Check health endpoint
kubectl port-forward svc/crm-app-service 8080:80 -n crm
curl http://localhost:8080/appinfo/health
```

## Configuration Management

### Environment Variables

The application supports the following environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | Database hostname |
| `DB_PORT` | 3306 | Database port |
| `DB_NAME` | crm | Database name |
| `DB_USERNAME` | root | Database username |
| `DB_PASSWORD` | password | Database password |
| `SPRING_PROFILES_ACTIVE` | docker | Spring profiles |
| `JAVA_OPTS` | -Xmx512m -Xms256m | JVM options |

### Kubernetes Secrets

For production deployments, create Kubernetes secrets for sensitive data:

```bash
# Create database secret
kubectl create secret generic crm-db-secret \
  --from-literal=username=your-username \
  --from-literal=password=your-password \
  -n crm

# Verify secret
kubectl get secrets -n crm
```

### ConfigMaps

Store non-sensitive configuration in ConfigMaps:

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: crm-config
  namespace: crm
data:
  application.properties: |
    spring.jpa.hibernate.ddl-auto=validate
    spring.thymeleaf.cache=true
    logging.level.crm=INFO
```

## Troubleshooting

### Common Issues

#### 1. Pod Startup Failures

```bash
# Check pod status and events
kubectl describe pod <pod-name> -n crm
kubectl logs <pod-name> -n crm

# Common causes:
# - Incorrect image URI
# - Missing environment variables
# - Database connectivity issues
# - Resource constraints
```

#### 2. Database Connection Issues

```bash
# Test database connectivity from pod
kubectl exec -it <pod-name> -n crm -- bash
# Inside pod:
telnet $DB_HOST $DB_PORT
```

#### 3. Ingress Not Working

```bash
# Check ALB controller status
kubectl get pods -n kube-system | grep aws-load-balancer

# Verify ingress annotations
kubectl describe ingress crm-app-ingress -n crm

# Check ALB in AWS Console
aws elbv2 describe-load-balancers --region us-west-2
```

#### 4. Performance Issues

```bash
# Check resource usage
kubectl top pods -n crm
kubectl top nodes

# Adjust resource limits in deployment.yaml
# Scale horizontally
kubectl scale deployment/crm-app --replicas=3 -n crm
```

### Health Check Endpoints

- **Application Health**: `/appinfo/health`
- **Application Info**: `/appinfo/info`
- **Actuator Endpoints**: `/appinfo/*`

### Log Analysis

```bash
# Stream logs from all pods
kubectl logs -f deployment/crm-app -n crm

# Get logs from specific time
kubectl logs deployment/crm-app -n crm --since=1h

# Export logs for analysis
kubectl logs deployment/crm-app -n crm > crm-app.log
```

## Security Considerations

### 1. Image Security

- Use official base images (eclipse-temurin)
- Run as non-root user
- Regularly update base images
- Scan images for vulnerabilities

### 2. Kubernetes Security

- Use secrets for sensitive data
- Implement network policies
- Configure resource limits
- Enable pod security policies

### 3. Database Security

- Use encrypted connections (SSL/TLS)
- Store credentials in Kubernetes secrets
- Implement proper access controls
- Regular security updates

### 4. Network Security

- Use HTTPS for external access
- Implement ingress TLS termination
- Configure security groups/NACLs
- Monitor network traffic

## Technology-Specific Notes

### Spring Boot 1.5.10

- **Legacy Version**: Consider upgrading to Spring Boot 2.x or 3.x
- **Security**: Review security configurations for known vulnerabilities
- **Performance**: JVM tuning is critical for container environments
- **Monitoring**: Limited actuator endpoints in 1.5.x

### Java 8 Considerations

- **End of Life**: Java 8 is approaching end of support
- **Container Support**: Limited container awareness
- **Memory Management**: Requires manual JVM tuning
- **Security**: Regular updates required

### JVM Tuning for Containers

```bash
# Recommended JVM options
JAVA_OPTS="-Xmx512m -Xms256m \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=75.0 \
  -XX:+UseG1GC \
  -XX:+UseStringDeduplication"
```

### Database Compatibility

- **MySQL**: Ensure version compatibility
- **H2**: Only for development/testing
- **Connection Pooling**: Configure appropriate pool sizes
- **SSL**: Enable for production deployments

## Scaling and Management

### Horizontal Pod Autoscaling

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: crm-app-hpa
  namespace: crm
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: crm-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

### Rolling Updates

```bash
# Update image
kubectl set image deployment/crm-app crm-app=new-image:tag -n crm

# Monitor rollout
kubectl rollout status deployment/crm-app -n crm

# Rollback if needed
kubectl rollout undo deployment/crm-app -n crm
```

### Backup and Recovery

- Database backups using AWS RDS automated backups
- Configuration backups using kubectl
- Disaster recovery procedures
- Regular backup testing

---

## Support and Maintenance

For additional support:

1. Check application logs: `kubectl logs -f deployment/crm-app -n crm`
2. Review AWS EKS documentation
3. Monitor AWS CloudWatch metrics
4. Regular security updates and patches

**Note**: This deployment guide is specific to the legacy Spring Boot 1.5.10 application. Consider modernizing the application stack for better security, performance, and maintainability.
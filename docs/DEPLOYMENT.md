# CompCRM - AWS ECS Fargate Deployment Guide

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Local Development Setup](#local-development-setup)
4. [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
5. [Build and Push Docker Image](#build-and-push-docker-image)
6. [AWS ECS Fargate Deployment](#aws-ecs-fargate-deployment)
7. [Configuration Management](#configuration-management)
8. [Monitoring and Logging](#monitoring-and-logging)
9. [Troubleshooting](#troubleshooting)
10. [Security Considerations](#security-considerations)
11. [Scaling and Management](#scaling-and-management)

---

## Overview

CompCRM is a Spring Boot 1.5.10 application running on Java 8. This guide provides comprehensive instructions for containerizing and deploying the application to AWS ECS Fargate.

**Technology Stack:**
- **Framework**: Spring Boot 1.5.10
- **Java Version**: 8 (Amazon Corretto 8)
- **Build Tool**: Maven 3.9.4
- **Database**: MySQL (configurable via environment variables)
- **Application Port**: 8080
- **Management Endpoint**: /appinfo/health
- **Deployment Platform**: AWS ECS Fargate

---

## Prerequisites

### Required Tools

1. **Docker** (version 20.10 or later)
   - Linux/macOS: https://docs.docker.com/get-docker/
   - Windows: https://docs.docker.com/desktop/windows/install/

2. **AWS CLI** (version 2.x)
   - Installation: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
   - Configure credentials: `aws configure`

3. **Git**
   - For cloning and version control

4. **Text Editor/IDE**
   - VS Code, IntelliJ IDEA, or similar

### System Requirements

- **Linux/macOS**:
  - Bash shell
  - 4GB RAM minimum
  - 10GB free disk space

- **Windows**:
  - Windows 10/11 or Windows Server 2019+
  - PowerShell 5.1 or later
  - 4GB RAM minimum
  - 10GB free disk space

---

## Local Development Setup

### 1. Clone the Repository

```bash
cd /path/to/project
```

### 2. Review Configuration

The application uses `application.properties` for configuration:

```properties
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:crm}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
management.context-path=/appinfo
```

### 3. Build with Docker Compose

```bash
docker-compose up --build
```

The application will be available at: http://localhost:8080

Health check endpoint: http://localhost:8080/appinfo/health

### 4. Stop Local Environment

```bash
docker-compose down
```

---

## AWS ECS Fargate Prerequisites

### 1. AWS Account Setup

- Active AWS account with appropriate permissions
- IAM user with programmatic access
- AWS CLI configured with credentials

### 2. Required AWS Resources

#### VPC Configuration

You need:
- **VPC ID**: Virtual Private Cloud for your resources
- **Subnets**: At least 2 subnets in different Availability Zones (for high availability)
- **Security Groups**: Configure inbound rules:
  - Port 8080 (application)
  - Port 80/443 (if using ALB)

#### IAM Roles

**ECS Task Execution Role** (`ecsTaskExecutionRole`):

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ecs-tasks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

Attach managed policy: `AmazonECSTaskExecutionRolePolicy`

**ECS Task Role** (`ecsTaskRole`) - Optional:

For application-specific AWS service access (S3, DynamoDB, etc.)

#### ECR Repository

Create an ECR repository to store Docker images:

```bash
aws ecr create-repository --repository-name compcrm --region us-east-1
```

Or use the build-push scripts which will auto-create the repository.

#### CloudWatch Log Group

Create log group for application logs:

```bash
aws logs create-log-group --log-group-name /ecs/compcrm --region us-east-1
```

---

## Build and Push Docker Image

### Using Linux/macOS

```bash
chmod +x scripts/build-push.sh
./scripts/build-push.sh
```

### Using Windows

```cmd
scripts\build-push.bat
```

### Script Workflow

1. **Select Registry**:
   - Option 1: AWS ECR (recommended for ECS)
   - Option 2: Docker Hub

2. **Provide Registry Details**:
   - **For ECR**: AWS Region, Account ID, Repository Name
   - **For Docker Hub**: Username, Password/Token

3. **Enter Image Tag**: Default is `latest`

4. **Build and Push**: Script will:
   - Authenticate with selected registry
   - Create ECR repository if it doesn't exist
   - Build Docker image using multi-stage build
   - Tag image appropriately
   - Push to registry

### Example ECR Image URI

```
123456789012.dkr.ecr.us-east-1.amazonaws.com/compcrm:latest
```

---

## AWS ECS Fargate Deployment

### ECS Task Definition Overview

**File**: `ecs/task-definition.json`

Key configurations:
- **Launch Type**: FARGATE
- **Network Mode**: awsvpc (required for Fargate)
- **CPU**: 512 (.5 vCPU)
- **Memory**: 1024 MB (1 GB)
- **Container Port**: 8080
- **Health Check**: /appinfo/health

**Valid Fargate CPU/Memory Combinations**:

| CPU (units) | Memory (MB) |
|-------------|-------------|
| 256 | 512, 1024, 2048 |
| 512 | 1024, 2048, 3072, 4096 |
| 1024 | 2048-8192 (1GB increments) |
| 2048 | 4096-16384 (1GB increments) |
| 4096 | 8192-30720 (1GB increments) |

### ECS Service Definition Overview

**File**: `ecs/service-definition.json`

Key configurations:
- **Service Name**: compcrm-service
- **Desired Count**: 2 (for high availability)
- **Launch Type**: FARGATE
- **Network Mode**: awsvpc
- **Load Balancer**: Optional Application Load Balancer
- **Deployment Circuit Breaker**: Enabled with automatic rollback

### Deployment Steps

#### Using Linux/macOS

```bash
chmod +x scripts/deploy-image.sh
./scripts/deploy-image.sh
```

#### Using Windows

```cmd
scripts\deploy-image.bat
```

### Deployment Script Workflow

1. **AWS Configuration**:
   - Enter AWS Region (e.g., us-east-1)
   - Enter ECS Cluster Name (will be created if doesn't exist)

2. **Network Configuration**:
   - Enter VPC ID
   - Enter Subnet IDs (comma-separated, at least 2)
   - Enter Security Group ID

3. **Container Configuration**:
   - Enter ECR Image URI (from build-push step)

4. **Load Balancer** (Optional):
   - Choose whether to create an Application Load Balancer
   - If yes, script will automatically create ALB and Target Group
   - Target Group configured with:
     - Target Type: IP (required for Fargate)
     - Health Check Path: /appinfo/health
     - Health Check Interval: 30 seconds

5. **Deployment**:
   - Register task definition
   - Create or update ECS service
   - Wait for service stabilization
   - Display deployment status and URLs

### Verify Deployment

```bash
aws ecs describe-services --cluster <CLUSTER_NAME> --services compcrm-service --region <REGION>
```

Check running tasks:

```bash
aws ecs list-tasks --cluster <CLUSTER_NAME> --service-name compcrm-service --region <REGION>
```

---

## Configuration Management

### Environment Variables

Configure in `ecs/task-definition.json` under `containerDefinitions[0].environment`:

```json
{
  "name": "DB_HOST",
  "value": "your-rds-endpoint.amazonaws.com"
},
{
  "name": "DB_PORT",
  "value": "3306"
},
{
  "name": "DB_NAME",
  "value": "crm"
},
{
  "name": "DB_USERNAME",
  "value": "admin"
},
{
  "name": "DB_PASSWORD",
  "value": "CHANGE_ME_IN_PRODUCTION"
}
```

**IMPORTANT**: For production, use AWS Secrets Manager or Parameter Store instead of hardcoding passwords.

### Using AWS Secrets Manager

Replace environment variables with secrets:

```json
{
  "name": "DB_PASSWORD",
  "valueFrom": "arn:aws:secretsmanager:region:account-id:secret:db-password"
}
```

### JVM Configuration

Adjust JVM settings in task definition:

```json
{
  "name": "JAVA_OPTS",
  "value": "-Xmx768m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
}
```

---

## Monitoring and Logging

### CloudWatch Logs

Application logs are sent to CloudWatch Logs:

**Log Group**: `/ecs/compcrm`
**Log Stream**: `ecs/compcrm/<task-id>`

View logs:

```bash
aws logs tail /ecs/compcrm --follow --region <REGION>
```

### CloudWatch Metrics

ECS provides default metrics:
- CPUUtilization
- MemoryUtilization
- NetworkRxBytes
- NetworkTxBytes

Access via AWS Console:
CloudWatch > Metrics > ECS > ClusterName, ServiceName

### Health Checks

**Application Load Balancer Health Check**:
- Path: `/appinfo/health`
- Interval: 30 seconds
- Timeout: 5 seconds
- Healthy Threshold: 2
- Unhealthy Threshold: 3

**Spring Boot Actuator Endpoint**:

```bash
curl http://<ALB_DNS>/appinfo/health
```

Response:

```json
{
  "status": "UP"
}
```

---

## Troubleshooting

### Common Issues

#### 1. Task Fails to Start

**Symptom**: Task immediately enters STOPPED state

**Diagnosis**:

```bash
aws ecs describe-tasks --cluster <CLUSTER> --tasks <TASK_ARN> --region <REGION>
```

**Common Causes**:
- Invalid CPU/memory combination
- Missing IAM permissions
- Image pull failure
- Invalid environment variables

**Solutions**:
- Verify CPU/memory are valid Fargate combinations
- Ensure ecsTaskExecutionRole has ECR and CloudWatch permissions
- Check ECR image URI is correct
- Validate all required environment variables

#### 2. Cannot Pull Image from ECR

**Error**: `CannotPullContainerError`

**Solutions**:
- Verify ECR repository exists
- Check ecsTaskExecutionRole has `ecr:GetAuthorizationToken`, `ecr:BatchCheckLayerAvailability`, `ecr:GetDownloadUrlForLayer`, `ecr:BatchGetImage`
- Ensure image tag exists in ECR

#### 3. Application Not Accessible

**Symptom**: Cannot access application via ALB

**Diagnosis**:
- Check ALB target group health
- Verify security group rules
- Check task network configuration

**Solutions**:
- Ensure security group allows inbound traffic on port 8080
- Verify subnets have route to internet gateway (for outbound)
- Check target group targets are registered and healthy

#### 4. High Memory Usage

**Symptom**: Tasks killed due to OOM

**Solutions**:
- Increase task memory (use valid Fargate combinations)
- Adjust JVM heap size:
  ```json
  {
    "name": "JAVA_OPTS",
    "value": "-Xmx512m -Xms256m"
  }
  ```
- Enable container memory limits awareness:
  ```
  -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0
  ```

#### 5. Slow Application Startup

**Symptom**: Health checks failing during startup

**Solutions**:
- Increase `healthCheckGracePeriodSeconds` in service definition (default: 300)
- Optimize Spring Boot startup:
  ```properties
  spring.jpa.hibernate.ddl-auto=validate
  spring.devtools.restart.enabled=false
  ```

### Debug Commands

**List running tasks**:

```bash
aws ecs list-tasks --cluster <CLUSTER> --service-name compcrm-service --region <REGION>
```

**Describe task**:

```bash
aws ecs describe-tasks --cluster <CLUSTER> --tasks <TASK_ARN> --region <REGION>
```

**View logs**:

```bash
aws logs get-log-events --log-group-name /ecs/compcrm --log-stream-name <STREAM_NAME> --region <REGION>
```

**Check service events**:

```bash
aws ecs describe-services --cluster <CLUSTER> --services compcrm-service --region <REGION> --query 'services[0].events'
```

---

## Security Considerations

### 1. IAM Best Practices

- Use separate task execution role and task role
- Grant least privilege permissions
- Rotate credentials regularly
- Enable CloudTrail for auditing

### 2. Network Security

**Security Group Rules**:

- **Inbound**:
  - Port 8080: From ALB security group only
  - Port 80/443: From internet (for ALB)

- **Outbound**:
  - Port 443: To AWS services (ECR, S3, CloudWatch)
  - Port 3306: To RDS security group (database)

**Use Private Subnets** (recommended):
- Deploy tasks in private subnets
- Use NAT Gateway for outbound internet access
- Place ALB in public subnets

### 3. Secrets Management

**Never hardcode secrets in task definitions**

Use AWS Secrets Manager:

```bash
aws secretsmanager create-secret --name compcrm/db-password --secret-string "YourSecurePassword" --region <REGION>
```

Reference in task definition:

```json
{
  "name": "DB_PASSWORD",
  "valueFrom": "arn:aws:secretsmanager:<REGION>:<ACCOUNT>:secret:compcrm/db-password"
}
```

### 4. Image Security

- Scan images for vulnerabilities:
  ```bash
  aws ecr start-image-scan --repository-name compcrm --image-id imageTag=latest --region <REGION>
  ```
- Use specific image tags (avoid `latest` in production)
- Enable ECR image scanning on push
- Regularly update base images

### 5. Application Security

- Enable HTTPS (TLS) on ALB
- Use AWS WAF for application firewall
- Enable Spring Security configurations
- Implement proper authentication/authorization

---

## Scaling and Management

### Auto Scaling

#### Configure Service Auto Scaling

**1. Register scalable target**:

```bash
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --resource-id service/<CLUSTER>/compcrm-service \
  --scalable-dimension ecs:service:DesiredCount \
  --min-capacity 2 \
  --max-capacity 10 \
  --region <REGION>
```

**2. Create scaling policy (CPU-based)**:

```bash
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --resource-id service/<CLUSTER>/compcrm-service \
  --scalable-dimension ecs:service:DesiredCount \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration file://scaling-policy.json \
  --region <REGION>
```

**scaling-policy.json**:

```json
{
  "TargetValue": 75.0,
  "PredefinedMetricSpecification": {
    "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
  },
  "ScaleInCooldown": 300,
  "ScaleOutCooldown": 60
}
```

### Blue/Green Deployments

ECS supports blue/green deployments with AWS CodeDeploy:

1. Create CodeDeploy application and deployment group
2. Configure ALB with two target groups (blue and green)
3. Deploy new task definition via CodeDeploy
4. Automated traffic shifting and validation

### Rolling Updates

Update service with new task definition:

```bash
aws ecs update-service \
  --cluster <CLUSTER> \
  --service compcrm-service \
  --task-definition compcrm-task:2 \
  --force-new-deployment \
  --region <REGION>
```

Deployment configuration controls:
- **maximumPercent**: 200 (allows temporary over-provisioning)
- **minimumHealthyPercent**: 50 (ensures availability during updates)

### Maintenance Tasks

**Update application**:

1. Build and push new image with new tag
2. Update task definition with new image URI
3. Update service to use new task definition

**Scale service manually**:

```bash
aws ecs update-service \
  --cluster <CLUSTER> \
  --service compcrm-service \
  --desired-count 4 \
  --region <REGION>
```

**Stop all tasks** (for maintenance):

```bash
aws ecs update-service \
  --cluster <CLUSTER> \
  --service compcrm-service \
  --desired-count 0 \
  --region <REGION>
```

---

## Spring Boot Specific Considerations

### 1. Graceful Shutdown

Spring Boot 1.5.x does not have built-in graceful shutdown. Consider upgrading to Spring Boot 2.3+ for:

```properties
server.shutdown=graceful
spring.lifecycle.timeout-per-shutdown-phase=30s
```

### 2. Actuator Endpoints

Enable additional actuator endpoints for monitoring:

```properties
management.security.enabled=false
management.endpoints.web.exposure.include=health,info,metrics
```

### 3. Database Connection Pooling

Configure HikariCP for optimal performance:

```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### 4. Logging Configuration

Ensure logs are sent to STDOUT for CloudWatch:

```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.level.root=INFO
logging.level.crm=DEBUG
```

---

## Cost Optimization

### 1. Right-Size Resources

- Start with smallest valid Fargate configuration (CPU: 256, Memory: 512)
- Monitor actual usage and scale up if needed
- Use CloudWatch metrics to identify over-provisioning

### 2. Use Fargate Spot

For fault-tolerant workloads:

```json
{
  "capacityProviderStrategy": [
    {
      "capacityProvider": "FARGATE_SPOT",
      "weight": 1
    }
  ]
}
```

### 3. Schedule Scaling

Scale down during off-peak hours:

```bash
aws application-autoscaling put-scheduled-action \
  --service-namespace ecs \
  --resource-id service/<CLUSTER>/compcrm-service \
  --scalable-dimension ecs:service:DesiredCount \
  --schedule "cron(0 22 * * ? *)" \
  --scheduled-action-name scale-down-evening \
  --scalable-target-action MinCapacity=1,MaxCapacity=2
```

---

## Additional Resources

- **AWS ECS Documentation**: https://docs.aws.amazon.com/ecs/
- **AWS Fargate Pricing**: https://aws.amazon.com/fargate/pricing/
- **Spring Boot Documentation**: https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/
- **Docker Best Practices**: https://docs.docker.com/develop/dev-best-practices/

---

## Support

For issues or questions:
1. Check CloudWatch Logs for application errors
2. Review ECS service events for deployment issues
3. Consult AWS Support or documentation
4. Review Spring Boot application logs

---

**Document Version**: 1.0
**Last Updated**: 2025-12-03
**Target Platform**: AWS ECS Fargate

# CRM Application - AWS ECS Fargate Deployment Guide

This guide provides comprehensive instructions for deploying the CRM Spring Boot application to AWS ECS Fargate.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Local Development Setup](#local-development-setup)
- [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
- [Building and Pushing Docker Images](#building-and-pushing-docker-images)
- [ECS Fargate Deployment](#ecs-fargate-deployment)
- [Configuration Management](#configuration-management)
- [Monitoring and Logging](#monitoring-and-logging)
- [Troubleshooting](#troubleshooting)
- [Scaling and Management](#scaling-and-management)

## Prerequisites

### System Requirements
- Docker 20.0+ installed
- AWS CLI 2.0+ installed and configured
- Java 8+ (for local development)
- Maven 3.6+ (for local development)

### AWS Account Requirements
- AWS Account with appropriate permissions
- VPC with at least 2 public subnets in different AZs
- Security groups configured for web traffic
- IAM roles for ECS task execution

## Local Development Setup

### 1. Clone and Build
```bash
# Clone the repository
git clone <repository-url>
cd CRMTestComp5

# Build with Maven
mvn clean package -DskipTests
```

### 2. Local Docker Development
```bash
# Build Docker image
docker build -t crm-app:latest .

# Run with Docker Compose
docker-compose up -d

# Access application
# Application: http://localhost:8080
# Health check: http://localhost:8080/appinfo/health
```

### 3. Application Configuration
The application uses the following configuration:
- **Port**: 8080 (web application)
- **Management Port**: 8080 (actuator endpoints)
- **Health Endpoint**: `/appinfo/health`
- **Profile**: `docker` (for containerized deployment)
- **Database**: MySQL (configured via environment variables)

## AWS ECS Fargate Prerequisites

### 1. AWS CLI Configuration
```bash
# Configure AWS CLI
aws configure
# Enter your AWS Access Key ID, Secret Access Key, Region, and Output format

# Verify configuration
aws sts get-caller-identity
```

### 2. Required IAM Roles

#### ECS Task Execution Role
Create an IAM role named `ecsTaskExecutionRole` with the following policy:
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ecr:GetAuthorizationToken",
                "ecr:BatchCheckLayerAvailability",
                "ecr:GetDownloadUrlForLayer",
                "ecr:BatchGetImage",
                "logs:CreateLogStream",
                "logs:PutLogEvents"
            ],
            "Resource": "*"
        }
    ]
}
```

#### ECS Task Role (Optional)
Create an IAM role named `ecsTaskRole` for application-specific permissions:
```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:GetObject",
                "s3:PutObject",
                "secretsmanager:GetSecretValue"
            ],
            "Resource": "*"
        }
    ]
}
```

### 3. Network Configuration

#### VPC Requirements
- VPC with internet gateway attached
- At least 2 public subnets in different availability zones
- Route table with route to internet gateway (0.0.0.0/0)

#### Security Group Configuration
Create a security group with the following rules:
- **Inbound Rules**:
  - Type: HTTP, Protocol: TCP, Port: 80, Source: 0.0.0.0/0 (for ALB)
  - Type: Custom TCP, Protocol: TCP, Port: 8080, Source: 0.0.0.0/0 (for direct access)
- **Outbound Rules**:
  - Type: All Traffic, Protocol: All, Port Range: All, Destination: 0.0.0.0/0

### 4. CloudWatch Log Group
Create a CloudWatch log group (automatically created by deployment script):
```bash
aws logs create-log-group --log-group-name "/ecs/crm" --region us-east-1
```

## Building and Pushing Docker Images

### Using Build Script (Recommended)

#### Linux/macOS:
```bash
# Make script executable
chmod +x scripts/build-push.sh

# Run build script
./scripts/build-push.sh
```

#### Windows:
```cmd
# Run batch script
scripts\build-push.bat
```

The script will:
1. Prompt for registry selection (AWS ECR or Docker Hub)
2. Sanitize image names and tags
3. Authenticate with the selected registry
4. Build and push the Docker image
5. Auto-create ECR repository if using AWS ECR

### Manual Build Process

#### For AWS ECR:
```bash
# Get AWS Account ID
ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
REGION="us-east-1"
REPO_NAME="crm"

# Create ECR repository (if it doesn't exist)
aws ecr create-repository --repository-name $REPO_NAME --region $REGION || true

# Login to ECR
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com

# Build and tag image
docker build -t $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO_NAME:latest .

# Push image
docker push $ACCOUNT_ID.dkr.ecr.$REGION.amazonaws.com/$REPO_NAME:latest
```

## ECS Fargate Deployment

### Using Deployment Script (Recommended)

#### Linux/macOS:
```bash
# Make script executable
chmod +x scripts/deploy-image.sh

# Run deployment script
./scripts/deploy-image.sh
```

#### Windows:
```cmd
# Run batch script
scripts\deploy-image.bat
```

The deployment script will:
1. Validate AWS CLI configuration
2. Prompt for deployment parameters
3. Create/verify ECS cluster
4. Optionally create Application Load Balancer
5. Register ECS task definition
6. Create/update ECS service
7. Wait for deployment stability
8. Display deployment summary

### Manual Deployment Process

#### 1. Create ECS Cluster
```bash
aws ecs create-cluster --cluster-name crm-cluster --region us-east-1
```

#### 2. Register Task Definition
Update `ecs/task-definition.json` with your image URI and register it:
```bash
aws ecs register-task-definition --cli-input-json file://ecs/task-definition.json --region us-east-1
```

#### 3. Create ECS Service
Update `ecs/service-definition.json` with your configuration and create the service:
```bash
aws ecs create-service --cli-input-json file://ecs/service-definition.json --region us-east-1
```

### ECS Task Definition Explained

#### Fargate Configuration
- **Launch Type**: FARGATE (serverless containers)
- **Network Mode**: awsvpc (each task gets its own ENI)
- **CPU**: 512 (.5 vCPU)
- **Memory**: 1024 MB (1 GB)

#### Container Configuration
- **Name**: crm
- **Port**: 8080
- **Health Check**: `/appinfo/health` endpoint
- **Logging**: CloudWatch logs to `/ecs/crm`

#### Environment Variables
- `JAVA_OPTS`: JVM optimization for containers
- `SPRING_PROFILES_ACTIVE`: docker
- `DB_HOST`, `DB_PORT`, `DB_NAME`: Database connection
- `DB_USER`, `DB_PASSWORD`: Database credentials

### ECS Service Configuration

#### Service Parameters
- **Desired Count**: 2 (for high availability)
- **Launch Type**: FARGATE
- **Network**: awsvpc with public IP assignment
- **Load Balancer**: Optional Application Load Balancer
- **Health Check Grace Period**: 300 seconds

#### Deployment Configuration
- **Maximum Percent**: 200% (allows rolling updates)
- **Minimum Healthy Percent**: 50% (maintains availability)

## Configuration Management

### Environment Variables
The application supports the following environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `DB_HOST` | Database hostname | localhost | Yes |
| `DB_PORT` | Database port | 3306 | No |
| `DB_NAME` | Database name | crm | No |
| `DB_USER` | Database username | root | Yes |
| `DB_PASSWORD` | Database password | - | Yes |
| `JAVA_OPTS` | JVM options | -Xmx512m -Xms256m | No |
| `SPRING_PROFILES_ACTIVE` | Spring profile | docker | No |
| `TZ` | Timezone | UTC | No |

### Database Configuration
The application requires a MySQL database. Configure the following:
1. Create a MySQL RDS instance or use external MySQL
2. Update security groups to allow ECS tasks to connect
3. Provide database credentials through environment variables

### Spring Boot Profiles
- **docker**: Optimized for containerized deployment
- **dev**: Development configuration
- **prod**: Production configuration

## Monitoring and Logging

### CloudWatch Logs
Application logs are automatically sent to CloudWatch:
- **Log Group**: `/ecs/crm`
- **Log Stream**: `ecs/crm/{container-id}`

#### Viewing Logs
```bash
# View recent logs
aws logs tail /ecs/crm --follow --region us-east-1

# View logs for specific time period
aws logs filter-log-events --log-group-name "/ecs/crm" --start-time 1609459200000 --region us-east-1
```

### Health Monitoring

#### Application Health Checks
- **Endpoint**: `http://your-alb-dns/appinfo/health`
- **Expected Response**: `{"status":"UP"}`
- **Check Interval**: 30 seconds

#### ECS Service Monitoring
```bash
# Check service status
aws ecs describe-services --cluster crm-cluster --services crm-service --region us-east-1

# Check running tasks
aws ecs list-tasks --cluster crm-cluster --service-name crm-service --region us-east-1
```

### CloudWatch Metrics
Monitor the following metrics:
- **ECS Service**: CPUUtilization, MemoryUtilization, RunningTaskCount
- **ALB**: RequestCount, ResponseTime, HTTPCode_Target_2XX_Count
- **Custom Metrics**: Application-specific metrics via Micrometer

## Troubleshooting

### Common Issues and Solutions

#### 1. Task Fails to Start
**Symptoms**: Tasks start and immediately stop

**Possible Causes**:
- Image not found or incorrect URI
- Insufficient memory or CPU allocation
- Application startup failure

**Solutions**:
```bash
# Check task definition
aws ecs describe-task-definition --task-definition crm-task --region us-east-1

# Check stopped tasks
aws ecs describe-tasks --cluster crm-cluster --tasks $(aws ecs list-tasks --cluster crm-cluster --desired-status STOPPED --query 'taskArns[0]' --output text) --region us-east-1

# Check CloudWatch logs
aws logs tail /ecs/crm --region us-east-1
```

#### 2. Service Fails Health Checks
**Symptoms**: Tasks start but fail load balancer health checks

**Possible Causes**:
- Application not responding on port 8080
- Health check endpoint not accessible
- Security group blocking traffic

**Solutions**:
- Verify application starts correctly in logs
- Test health endpoint: `curl http://task-ip:8080/appinfo/health`
- Check security group rules
- Verify target group configuration

#### 3. Database Connection Issues
**Symptoms**: Application fails with database connection errors

**Solutions**:
- Verify RDS security group allows connections from ECS tasks
- Check database credentials and connection parameters
- Ensure RDS instance is in same VPC or properly configured for external access

#### 4. Memory/CPU Issues
**Symptoms**: Tasks killed due to resource constraints

**Solutions**:
- Monitor CloudWatch metrics for high resource usage
- Adjust task definition CPU/memory allocation
- Optimize JVM heap settings in JAVA_OPTS

### Debugging Commands

```bash
# Describe service events
aws ecs describe-services --cluster crm-cluster --services crm-service --query 'services[0].events[:5]' --region us-east-1

# Get task details
TASK_ARN=$(aws ecs list-tasks --cluster crm-cluster --service-name crm-service --query 'taskArns[0]' --output text --region us-east-1)
aws ecs describe-tasks --cluster crm-cluster --tasks $TASK_ARN --region us-east-1

# Check load balancer target health
aws elbv2 describe-target-health --target-group-arn <target-group-arn> --region us-east-1

# View service logs
aws logs filter-log-events --log-group-name "/ecs/crm" --filter-pattern "ERROR" --region us-east-1
```

## Scaling and Management

### Service Auto Scaling

#### Enable Application Auto Scaling
```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
    --service-namespace ecs \
    --resource-id service/crm-cluster/crm-service \
    --scalable-dimension ecs:service:DesiredCount \
    --min-capacity 2 \
    --max-capacity 10 \
    --region us-east-1

# Create scaling policy
aws application-autoscaling put-scaling-policy \
    --service-namespace ecs \
    --resource-id service/crm-cluster/crm-service \
    --scalable-dimension ecs:service:DesiredCount \
    --policy-name crm-cpu-scaling \
    --policy-type TargetTrackingScaling \
    --target-tracking-scaling-policy-configuration file://scaling-policy.json \
    --region us-east-1
```

#### scaling-policy.json
```json
{
    "TargetValue": 70.0,
    "PredefinedMetricSpecification": {
        "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
    },
    "ScaleOutCooldown": 300,
    "ScaleInCooldown": 300
}
```

### Manual Scaling
```bash
# Scale service to 5 tasks
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-service \
    --desired-count 5 \
    --region us-east-1
```

### Blue/Green Deployments
For zero-downtime deployments:

1. **CodeDeploy Integration**:
   - Use AWS CodeDeploy with ECS for automated blue/green deployments
   - Configure deployment configuration for traffic shifting

2. **Manual Blue/Green**:
   - Create new task definition with updated image
   - Update service with new task definition
   - ECS automatically performs rolling deployment

### Service Updates

#### Update Application Image
```bash
# Register new task definition
aws ecs register-task-definition --cli-input-json file://ecs/task-definition.json --region us-east-1

# Update service
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-service \
    --task-definition crm-task:NEW_REVISION \
    --region us-east-1
```

#### Update Service Configuration
```bash
# Update desired count
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-service \
    --desired-count 3 \
    --region us-east-1

# Update deployment configuration
aws ecs update-service \
    --cluster crm-cluster \
    --service crm-service \
    --deployment-configuration maximumPercent=200,minimumHealthyPercent=75 \
    --region us-east-1
```

## Security Considerations

### Container Security
- Application runs as non-root user
- Minimal base image (Amazon Corretto)
- No unnecessary packages installed
- Regular security updates for base images

### Network Security
- VPC isolation with private subnets (optional)
- Security groups with minimal required ports
- ALB with AWS WAF integration (recommended)
- VPC endpoints for AWS services (optional)

### Secrets Management
- Use AWS Secrets Manager for database passwords
- Environment variables for non-sensitive configuration
- IAM roles instead of hardcoded credentials

### Example Secrets Manager Integration
```json
{
  "name": "DB_PASSWORD",
  "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789:secret:crm/db-password-AbCdEf"
}
```

## Java/Spring Boot Specific Notes

### JVM Optimization for Containers
- `-XX:+UseContainerSupport`: Enable container-aware JVM
- `-XX:MaxRAMPercentage=75.0`: Use 75% of container memory
- `-Xmx512m -Xms256m`: Explicit heap settings
- `-Djava.security.egd=file:/dev/./urandom`: Faster startup

### Spring Boot Production Configuration
- Actuator endpoints secured in production
- Connection pooling configured for database
- Logging optimized for CloudWatch
- Profile-based configuration management

### Performance Tuning
- Appropriate JVM heap size for workload
- Database connection pool sizing
- HTTP connection timeouts
- Garbage collection tuning for low latency

## Cost Optimization

### Fargate Pricing Optimization
- Right-size CPU and memory allocations
- Use Spot capacity for non-critical workloads
- Implement auto-scaling to minimize idle capacity
- Schedule scaling for predictable traffic patterns

### Resource Monitoring
- Monitor CPU and memory utilization
- Set up CloudWatch alarms for resource waste
- Regular review of task resource requirements
- Consider reserved capacity for stable workloads

## Support and Resources

### AWS Documentation
- [Amazon ECS Developer Guide](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/)
- [AWS Fargate User Guide](https://docs.aws.amazon.com/AmazonECS/latest/userguide/)
- [Application Load Balancer User Guide](https://docs.aws.amazon.com/elasticloadbalancing/latest/application/)

### Spring Boot Resources
- [Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Spring Boot Production Best Practices](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html)

### Monitoring and Observability
- CloudWatch for metrics and logging
- AWS X-Ray for distributed tracing
- Spring Boot Micrometer for custom metrics
- Application Performance Monitoring (APM) tools

---

## Quick Reference Commands

```bash
# Build and push
./scripts/build-push.sh

# Deploy to ECS
./scripts/deploy-image.sh

# Check service status
aws ecs describe-services --cluster crm-cluster --services crm-service --region us-east-1

# View logs
aws logs tail /ecs/crm --follow --region us-east-1

# Scale service
aws ecs update-service --cluster crm-cluster --service crm-service --desired-count 3 --region us-east-1

# Health check
curl http://your-alb-dns/appinfo/health
```

This deployment guide provides comprehensive instructions for successfully deploying your CRM Spring Boot application to AWS ECS Fargate with production-ready configuration, monitoring, and scaling capabilities.
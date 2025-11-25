# AWS ECS Fargate Deployment Guide for CRMCompTest

This guide provides comprehensive instructions for deploying the CRMCompTest Java application to AWS ECS Fargate.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [AWS ECS Fargate Prerequisites](#aws-ecs-fargate-prerequisites)
4. [Building and Pushing Docker Images](#building-and-pushing-docker-images)
5. [ECS Task Definition](#ecs-task-definition)
6. [ECS Service Configuration](#ecs-service-configuration)
7. [Deployment Steps](#deployment-steps)
8. [Monitoring and Logging](#monitoring-and-logging)
9. [Troubleshooting](#troubleshooting)
10. [Security Best Practices](#security-best-practices)
11. [Scaling and Performance](#scaling-and-performance)

---

## Prerequisites

### Required Tools

- **Docker**: Version 20.10 or higher
- **AWS CLI**: Version 2.x
- **Java Development Kit (JDK)**: Version 8 (for local development)
- **Maven**: Version 3.6 or higher (for local builds)
- **Git**: For version control

### AWS Account Requirements

- Active AWS account with appropriate permissions
- IAM permissions for:
  - ECS (create/update clusters, services, task definitions)
  - ECR (create repositories, push images)
  - CloudWatch Logs (create log groups, write logs)
  - VPC (describe subnets, security groups)
  - IAM (create/attach roles)
  - Elastic Load Balancing (optional, for ALB/NLB)

### System Requirements

- **Memory**: Minimum 4GB RAM for local Docker builds
- **Disk Space**: At least 2GB free space
- **Operating System**: Linux, macOS, or Windows 10/11 with WSL2

---

## Local Development Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd CRMCompTest
```

### 2. Build the Application Locally

```bash
mvn clean package -DskipTests
```

The compiled JAR file will be located in `target/` directory.

### 3. Run with Docker Compose

```bash
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

### 4. Test the Application

```bash
curl http://localhost:8080/health
```

### 5. Stop the Application

```bash
docker-compose down
```

---

## AWS ECS Fargate Prerequisites

### 1. VPC and Networking Setup

**Create or identify a VPC** with the following:

- At least 2 public subnets in different Availability Zones
- Internet Gateway attached to VPC
- Route tables configured for internet access

**Create a Security Group** for ECS tasks:

```bash
aws ec2 create-security-group \
  --group-name crmcomptest-sg \
  --description "Security group for CRMCompTest ECS tasks" \
  --vpc-id <your-vpc-id>
```

**Allow inbound traffic on port 8080:**

```bash
aws ec2 authorize-security-group-ingress \
  --group-id <security-group-id> \
  --protocol tcp \
  --port 8080 \
  --cidr 0.0.0.0/0
```

### 2. IAM Roles Setup

**Create ECS Task Execution Role:**

This role allows ECS to pull container images from ECR and write logs to CloudWatch.

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

**Attach the AWS managed policy:**

```bash
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
```

**Create ECS Task Role (optional):**

This role provides permissions to the application running in the container.

```bash
aws iam create-role \
  --role-name ecsTaskRole \
  --assume-role-policy-document file://task-role-trust-policy.json
```

### 3. CloudWatch Logs Setup

**Create a log group for the application:**

```bash
aws logs create-log-group \
  --log-group-name /ecs/crmcomptest \
  --region us-east-1
```

**Set retention policy (optional):**

```bash
aws logs put-retention-policy \
  --log-group-name /ecs/crmcomptest \
  --retention-in-days 7
```

---

## Building and Pushing Docker Images

### Option 1: Using AWS ECR

#### Step 1: Run the Build Script

**Linux/macOS:**

```bash
chmod +x scripts/build-push.sh
./scripts/build-push.sh
```

**Windows:**

```cmd
scripts\build-push.bat
```

#### Step 2: Follow the Prompts

- Select **1** for AWS ECR
- Enter your AWS Region (e.g., `us-east-1`)
- Enter your AWS Account ID
- Enter ECR repository name (default: `crmcomptest`)
- Enter image tag (default: `latest`)

#### Step 3: Verify Image in ECR

```bash
aws ecr describe-images \
  --repository-name crmcomptest \
  --region us-east-1
```

### Option 2: Using Docker Hub

#### Step 1: Run the Build Script

```bash
./scripts/build-push.sh  # Linux/macOS
scripts\build-push.bat   # Windows
```

#### Step 2: Follow the Prompts

- Select **2** for Docker Hub
- Enter your Docker Hub username
- Enter your Docker Hub password or access token
- Enter image tag (default: `latest`)

---

## ECS Task Definition

### Overview

The task definition defines:

- **Launch Type**: FARGATE
- **Network Mode**: awsvpc (required for Fargate)
- **CPU**: 512 (.5 vCPU)
- **Memory**: 1024 MB (1 GB)
- **Container Configuration**:
  - Image URI from ECR or Docker Hub
  - Port 8080 exposed
  - Environment variables for JVM tuning
  - CloudWatch Logs integration
  - Health check configuration

### Key Configuration

```json
{
  "family": "crmcomptest-task",
  "requiresCompatibilities": ["FARGATE"],
  "networkMode": "awsvpc",
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::<account-id>:role/ecsTaskExecutionRole"
}
```

### Valid Fargate CPU/Memory Combinations

| CPU (vCPU) | Memory (MB) Options |
|------------|---------------------|
| 256 (.25)  | 512, 1024, 2048 |
| 512 (.5)   | 1024, 2048, 3072, 4096 |
| 1024 (1)   | 2048-8192 (increments of 1024) |
| 2048 (2)   | 4096-16384 (increments of 1024) |
| 4096 (4)   | 8192-30720 (increments of 1024) |

### JVM Memory Configuration

The task definition includes JVM tuning:

```
JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
```

**Explanation:**

- `-Xmx512m`: Maximum heap size (50% of container memory)
- `-Xms256m`: Initial heap size (25% of container memory)
- `-XX:+UseContainerSupport`: JVM respects container memory limits
- `-XX:MaxRAMPercentage=75.0`: Use up to 75% of container memory

---

## ECS Service Configuration

### Overview

The service definition specifies:

- **Launch Type**: FARGATE
- **Desired Count**: 2 (for high availability)
- **Network Configuration**: awsvpc mode with subnets and security groups
- **Deployment Configuration**: Rolling updates with circuit breaker
- **Load Balancer Integration**: Optional ALB/NLB

### Key Configuration

```json
{
  "serviceName": "crmcomptest-service",
  "launchType": "FARGATE",
  "desiredCount": 2,
  "networkConfiguration": {
    "awsvpcConfiguration": {
      "subnets": ["subnet-xxx", "subnet-yyy"],
      "securityGroups": ["sg-xxx"],
      "assignPublicIp": "ENABLED"
    }
  }
}
```

### Deployment Strategies

**Rolling Update (Default):**

- `maximumPercent`: 200 (allows 2x tasks during deployment)
- `minimumHealthyPercent`: 50 (ensures at least 50% tasks remain healthy)

**Circuit Breaker:**

- Automatically rolls back failed deployments
- Enabled by default in service definition

---

## Deployment Steps

### Step 1: Prepare ECS Artifacts

Ensure the following files exist:

- `ecs/task-definition.json`
- `ecs/service-definition.json`

### Step 2: Run Deployment Script

**Linux/macOS:**

```bash
chmod +x scripts/deploy-image.sh
./scripts/deploy-image.sh
```

**Windows:**

```cmd
scripts\deploy-image.bat
```

### Step 3: Provide Required Information

The script will prompt for:

1. **AWS Region** (e.g., `us-east-1`)
2. **ECS Cluster Name** (will be created if it doesn't exist)
3. **VPC ID**
4. **Subnet IDs** (comma-separated, at least 2)
5. **Security Group ID**
6. **ECR Image URI** (e.g., `123456789.dkr.ecr.us-east-1.amazonaws.com/crmcomptest:latest`)
7. **Load Balancer** (y/n)

### Step 4: Monitor Deployment

The script will:

1. Register the task definition
2. Create or update the ECS service
3. Wait for service to become stable
4. Display service details and logs

### Step 5: Verify Deployment

**Check service status:**

```bash
aws ecs describe-services \
  --cluster <cluster-name> \
  --services crmcomptest-service \
  --region us-east-1
```

**Check running tasks:**

```bash
aws ecs list-tasks \
  --cluster <cluster-name> \
  --service-name crmcomptest-service \
  --region us-east-1
```

**Test the application:**

```bash
curl http://<alb-dns-name>/health
```

---

## Monitoring and Logging

### CloudWatch Logs

**View logs in real-time:**

```bash
aws logs tail /ecs/crmcomptest --follow --region us-east-1
```

**Search logs:**

```bash
aws logs filter-log-events \
  --log-group-name /ecs/crmcomptest \
  --filter-pattern "ERROR" \
  --region us-east-1
```

### CloudWatch Metrics

ECS automatically publishes metrics:

- CPU Utilization
- Memory Utilization
- Network In/Out

**View metrics:**

```bash
aws cloudwatch get-metric-statistics \
  --namespace AWS/ECS \
  --metric-name CPUUtilization \
  --dimensions Name=ServiceName,Value=crmcomptest-service \
  --start-time 2025-01-01T00:00:00Z \
  --end-time 2025-01-01T23:59:59Z \
  --period 3600 \
  --statistics Average
```

### Health Checks

**Container Health Check:**

```json
{
  "command": ["CMD-SHELL", "wget -q --spider http://localhost:8080/health || exit 1"],
  "interval": 30,
  "timeout": 5,
  "retries": 3,
  "startPeriod": 60
}
```

**Load Balancer Health Check:**

- Path: `/health`
- Interval: 30 seconds
- Timeout: 5 seconds
- Healthy threshold: 2
- Unhealthy threshold: 3

---

## Troubleshooting

### Common Issues

#### 1. Task Fails to Start

**Symptoms:** Tasks transition from PENDING to STOPPED

**Causes:**

- Insufficient permissions (check IAM roles)
- Image pull errors (verify ECR permissions)
- Invalid CPU/memory combination
- Network configuration issues

**Solutions:**

```bash
# Check stopped task reason
aws ecs describe-tasks \
  --cluster <cluster-name> \
  --tasks <task-id> \
  --region us-east-1

# Check IAM role
aws iam get-role --role-name ecsTaskExecutionRole

# Verify ECR permissions
aws ecr get-login-password --region us-east-1
```

#### 2. Health Check Failures

**Symptoms:** Tasks fail health checks and are replaced

**Causes:**

- Application not responding on port 8080
- Health endpoint not implemented
- Insufficient startup time

**Solutions:**

- Increase `startPeriod` in health check configuration
- Verify application logs for startup errors
- Test health endpoint locally

#### 3. Network Connectivity Issues

**Symptoms:** Cannot access application from internet

**Causes:**

- Security group not allowing inbound traffic
- Tasks in private subnets without NAT gateway
- Load balancer misconfiguration

**Solutions:**

```bash
# Check security group rules
aws ec2 describe-security-groups --group-ids <sg-id>

# Verify subnet route tables
aws ec2 describe-route-tables --filters "Name=association.subnet-id,Values=<subnet-id>"

# Check load balancer target health
aws elbv2 describe-target-health --target-group-arn <tg-arn>
```

#### 4. Memory Issues

**Symptoms:** Tasks stop with OutOfMemory errors

**Causes:**

- JVM heap size exceeds container memory
- Memory leak in application
- Insufficient container memory

**Solutions:**

- Adjust `JAVA_OPTS` to reduce heap size
- Increase task memory in task definition
- Profile application for memory leaks

#### 5. Image Pull Errors

**Symptoms:** "CannotPullContainerError" in task stopped reason

**Causes:**

- Invalid image URI
- Missing ECR permissions
- Image doesn't exist in registry

**Solutions:**

```bash
# Verify image exists
aws ecr describe-images --repository-name crmcomptest

# Check repository policy
aws ecr get-repository-policy --repository-name crmcomptest

# Attach ECR permissions to execution role
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly
```

---

## Security Best Practices

### 1. IAM Roles and Policies

- Use separate execution and task roles
- Follow principle of least privilege
- Regularly audit IAM permissions
- Use IAM roles instead of access keys

### 2. Network Security

- Use private subnets with NAT gateway for production
- Restrict security group rules to known IP ranges
- Enable VPC Flow Logs
- Use AWS PrivateLink for AWS service access

### 3. Container Security

- Run containers as non-root user (already configured)
- Scan images for vulnerabilities
- Use official base images (eclipse-temurin)
- Keep base images updated

### 4. Secrets Management

**Use AWS Secrets Manager or Parameter Store:**

```json
{
  "secrets": [
    {
      "name": "DATABASE_PASSWORD",
      "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789:secret:db-password"
    }
  ]
}
```

### 5. Logging and Monitoring

- Enable CloudWatch Container Insights
- Configure log retention policies
- Set up CloudWatch alarms for critical metrics
- Use AWS GuardDuty for threat detection

---

## Scaling and Performance

### Auto Scaling

**Configure Service Auto Scaling:**

```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --resource-id service/<cluster-name>/crmcomptest-service \
  --scalable-dimension ecs:service:DesiredCount \
  --min-capacity 2 \
  --max-capacity 10

# Create scaling policy (target tracking)
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --resource-id service/<cluster-name>/crmcomptest-service \
  --scalable-dimension ecs:service:DesiredCount \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration file://scaling-policy.json
```

**scaling-policy.json:**

```json
{
  "TargetValue": 70.0,
  "PredefinedMetricSpecification": {
    "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
  },
  "ScaleInCooldown": 300,
  "ScaleOutCooldown": 60
}
```

### Performance Tuning

**JVM Optimization:**

- Use appropriate garbage collector (G1GC for Java 8)
- Monitor GC logs and tune parameters
- Profile application with JProfiler or VisualVM

**Container Optimization:**

- Right-size CPU and memory based on load testing
- Use multi-stage builds to reduce image size
- Enable Docker layer caching

**Application Optimization:**

- Implement connection pooling
- Use caching strategies (Redis, in-memory)
- Optimize database queries
- Enable compression

### Blue/Green Deployments

**Using AWS CodeDeploy:**

```bash
aws deploy create-deployment-group \
  --application-name AppECS-<cluster>-<service> \
  --deployment-group-name DgpECS-<cluster>-<service> \
  --deployment-config-name CodeDeployDefault.ECSAllAtOnce \
  --ecs-services serviceName=crmcomptest-service,clusterName=<cluster> \
  --load-balancer-info targetGroupInfoList=[{name=<tg-name>}]
```

---

## Additional Resources

- [AWS ECS Fargate Documentation](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Java Container Best Practices](https://www.eclipse.org/openj9/docs/xxusecontainersupport/)
- [AWS CLI Reference](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/ecs/index.html)

---

## Support and Feedback

For issues or questions:

1. Check CloudWatch Logs: `/ecs/crmcomptest`
2. Review ECS service events
3. Consult AWS Support
4. Contact development team

---

**Document Version:** 1.0  
**Last Updated:** 2025-11-25  
**Maintained By:** DevOps Team
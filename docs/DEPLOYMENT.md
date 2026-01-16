=== docs/DEPLOYMENT.md ===
# CompTestCRM - AWS ECS Fargate Deployment Guide

## Table of Contents
1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Local Development Setup](#local-development-setup)
4. [Docker Deployment](#docker-deployment)
5. [AWS ECS Fargate Deployment](#aws-ecs-fargate-deployment)
6. [Configuration Management](#configuration-management)
7. [Monitoring and Logging](#monitoring-and-logging)
8. [Troubleshooting](#troubleshooting)
9. [Security Best Practices](#security-best-practices)
10. [Scaling and Performance](#scaling-and-performance)

---

## Overview

CompTestCRM is a Spring Boot 1.5.10 web application built with Java 8, providing customer relationship management capabilities with features including:

- Customer and contract management
- User authentication and authorization (Spring Security)
- Data export to PDF, CSV, and Excel formats
- Thymeleaf-based web UI
- Spring Boot Actuator for health monitoring

**Technology Stack:**
- Java 8
- Spring Boot 1.5.10.RELEASE
- Maven 3.9.4
- MySQL/H2 Database
- Thymeleaf Template Engine
- Spring Data JPA

**Application Details:**
- Application Port: `8080`
- Health Check Endpoint: `/appinfo/health`
- Management Context Path: `/appinfo`
- Package Type: Executable JAR

---

## Prerequisites

### Required Tools

1. **Docker** (version 20.x or later)
   - Installation: https://docs.docker.com/get-docker/
   - Verify: `docker --version`

2. **AWS CLI** (version 2.x or later)
   - Installation: https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html
   - Verify: `aws --version`
   - Configure: `aws configure`

3. **Git** (for cloning repository)
   - Installation: https://git-scm.com/downloads
   - Verify: `git --version`

4. **Java 8 JDK** (for local development)
   - Installation: https://adoptium.net/temurin/releases/
   - Verify: `java -version`

5. **Maven 3.9.x** (for local builds)
   - Installation: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

### AWS Requirements

1. **AWS Account** with appropriate permissions
2. **IAM User** or Role with permissions for:
   - Amazon ECS (Full Access)
   - Amazon ECR (Full Access)
   - Amazon VPC (Read Access)
   - CloudWatch Logs (Full Access)
   - Elastic Load Balancing (Full Access)
   - IAM (Limited - for role creation)

3. **VPC Configuration:**
   - VPC with at least 2 subnets in different Availability Zones
   - Internet Gateway attached to VPC
   - Route table with route to Internet Gateway
   - Subnets with "Auto-assign public IPv4 address" enabled

4. **Security Group:**
   - Inbound rule: Port 8080 (TCP) from load balancer security group
   - Inbound rule: Port 80 (TCP) from 0.0.0.0/0 (for ALB)
   - Outbound rule: All traffic to 0.0.0.0/0

5. **IAM Roles:**
   
   **ecsTaskExecutionRole** (allows ECS to pull images and write logs):
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
   Attach managed policy: `arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy`

   **ecsTaskRole** (optional - for application permissions):
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

6. **Database:**
   - Amazon RDS MySQL instance (recommended for production)
   - Or external MySQL database accessible from ECS tasks
   - Security group allowing inbound port 3306 from ECS tasks

---

## Local Development Setup

### 1. Clone Repository

```bash
git clone <repository-url>
cd CompTestCRM
```

### 2. Configure Application

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/crm
spring.datasource.username=root
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Management Endpoint
management.context-path=/appinfo
management.security.enabled=false
```

### 3. Run MySQL Database (Docker)

```bash
docker run -d \
  --name mysql-crm \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=crm \
  -p 3306:3306 \
  mysql:5.7
```

### 4. Build and Run Application

```bash
# Build application
mvn clean package -DskipTests

# Run application
java -jar target/crm-0.0.1-SNAPSHOT.jar
```

### 5. Access Application

- Application: http://localhost:8080
- Health Check: http://localhost:8080/appinfo/health
- Actuator Info: http://localhost:8080/appinfo/info

**Default Users** (loaded from `data.sql`):
- Admin: `admin@admin.com` / `admin`
- User: `user@user.com` / `user`
- Manager: `manager@manager.com` / `manager`
- Owner: `owner@owner.com` / `owner`

---

## Docker Deployment

### 1. Build Docker Image

```bash
# Using docker-compose
docker-compose build

# Or using Dockerfile directly
docker build -t comptestcrm:latest .
```

### 2. Run with Docker Compose

```bash
# Set environment variables
export DB_HOST=your-database-host
export DB_PORT=3306
export DB_NAME=crm
export DB_USER=root
export DB_PASSWORD=your-password

# Start application
docker-compose up -d

# View logs
docker-compose logs -f comptestcrm-app

# Stop application
docker-compose down
```

### 3. Run with Docker CLI

```bash
docker run -d \
  --name comptestcrm \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e DB_HOST=your-database-host \
  -e DB_PORT=3306 \
  -e DB_NAME=crm \
  -e DB_USER=root \
  -e DB_PASSWORD=your-password \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  comptestcrm:latest
```

---

## AWS ECS Fargate Deployment

### Step 1: Build and Push Docker Image

Use the provided build script to build and push your Docker image to a container registry.

#### Using AWS ECR (Recommended)

**Linux/macOS:**
```bash
cd scripts
chmod +x build-push.sh
./build-push.sh
```

**Windows:**
```cmd
cd scripts
build-push.bat
```

**Script Workflow:**
1. Select registry type (1. AWS ECR, 2. Docker Hub)
2. Enter registry details:
   - **AWS ECR**: Region, Account ID, Repository Name
   - **Docker Hub**: Username, Password/Token
3. Enter image tag (default: `latest`)
4. Script automatically:
   - Authenticates with selected registry
   - Creates ECR repository if it doesn't exist (AWS ECR only)
   - Builds Docker image
   - Pushes image to registry

**Example Output:**
```
Project: comptestcrm
Sanitized Image Name: comptestcrm
Image Tag: v1.0.0
Registry: 123456789012.dkr.ecr.us-east-1.amazonaws.com/comptestcrm:v1.0.0
```

### Step 2: Prepare AWS Infrastructure

#### 2.1 Create VPC and Subnets (if not exists)

```bash
# Create VPC
VPC_ID=$(aws ec2 create-vpc \
  --cidr-block 10.0.0.0/16 \
  --region us-east-1 \
  --query 'Vpc.VpcId' \
  --output text)

# Create Internet Gateway
IGW_ID=$(aws ec2 create-internet-gateway \
  --region us-east-1 \
  --query 'InternetGateway.InternetGatewayId' \
  --output text)

# Attach Internet Gateway to VPC
aws ec2 attach-internet-gateway \
  --vpc-id $VPC_ID \
  --internet-gateway-id $IGW_ID \
  --region us-east-1

# Create Subnets
SUBNET_1=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.1.0/24 \
  --availability-zone us-east-1a \
  --region us-east-1 \
  --query 'Subnet.SubnetId' \
  --output text)

SUBNET_2=$(aws ec2 create-subnet \
  --vpc-id $VPC_ID \
  --cidr-block 10.0.2.0/24 \
  --availability-zone us-east-1b \
  --region us-east-1 \
  --query 'Subnet.SubnetId' \
  --output text)

# Enable auto-assign public IP
aws ec2 modify-subnet-attribute \
  --subnet-id $SUBNET_1 \
  --map-public-ip-on-launch \
  --region us-east-1

aws ec2 modify-subnet-attribute \
  --subnet-id $SUBNET_2 \
  --map-public-ip-on-launch \
  --region us-east-1
```

#### 2.2 Create Security Group

```bash
# Create Security Group
SG_ID=$(aws ec2 create-security-group \
  --group-name comptestcrm-sg \
  --description "Security group for CompTestCRM ECS tasks" \
  --vpc-id $VPC_ID \
  --region us-east-1 \
  --query 'GroupId' \
  --output text)

# Allow inbound HTTP (port 80) from anywhere (for ALB)
aws ec2 authorize-security-group-ingress \
  --group-id $SG_ID \
  --protocol tcp \
  --port 80 \
  --cidr 0.0.0.0/0 \
  --region us-east-1

# Allow inbound port 8080 from anywhere (or restrict to ALB SG)
aws ec2 authorize-security-group-ingress \
  --group-id $SG_ID \
  --protocol tcp \
  --port 8080 \
  --cidr 0.0.0.0/0 \
  --region us-east-1
```

#### 2.3 Create IAM Roles

**Create ecsTaskExecutionRole:**

```bash
# Create trust policy document
cat > trust-policy.json <<EOF
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
EOF

# Create role
aws iam create-role \
  --role-name ecsTaskExecutionRole \
  --assume-role-policy-document file://trust-policy.json

# Attach managed policy
aws iam attach-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-arn arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy
```

**Create ecsTaskRole (optional):**

```bash
# Create role
aws iam create-role \
  --role-name ecsTaskRole \
  --assume-role-policy-document file://trust-policy.json

# Attach custom policies as needed for your application
```

### Step 3: Deploy to ECS Fargate

Use the provided deployment script to deploy your application to ECS Fargate.

**Linux/macOS:**
```bash
cd scripts
chmod +x deploy-image.sh
./deploy-image.sh
```

**Windows:**
```cmd
cd scripts
deploy-image.bat
```

**Script Workflow:**

1. **Prompts for Configuration:**
   - AWS Region (e.g., `us-east-1`)
   - ECS Cluster Name (will be created if doesn't exist)
   - VPC ID
   - Subnet IDs (comma-separated)
   - Security Group ID
   - Docker Image URI (from Step 1)

2. **Database Configuration:**
   - Database Host
   - Database Port (default: 3306)
   - Database Name (default: crm)
   - Database User
   - Database Password

3. **Load Balancer Setup:**
   - Prompts whether to create load balancer (y/n)
   - If yes: Automatically creates Application Load Balancer and Target Group
   - If no: Deploys without load balancer

4. **Deployment Actions:**
   - Creates/verifies ECS cluster
   - Creates CloudWatch log group
   - Registers ECS task definition with your configuration
   - Creates or updates ECS service
   - Waits for service to become stable
   - Displays deployment status and access information

**Example Execution:**

```bash
./deploy-image.sh

Enter AWS Region: us-east-1
Enter ECS Cluster Name: comptestcrm-cluster
Enter VPC ID: vpc-0123456789abcdef0
Enter Subnet IDs: subnet-0123456789abcdef0,subnet-0123456789abcdef1
Enter Security Group ID: sg-0123456789abcdef0
Enter Docker Image URI: 123456789012.dkr.ecr.us-east-1.amazonaws.com/comptestcrm:latest

Enter Database Host: mydb.c9akciq32.us-east-1.rds.amazonaws.com
Enter Database Port: 3306
Enter Database Name: crm
Enter Database User: admin
Enter Database Password: ********

Do you need a load balancer? (y/n): y

Creating Application Load Balancer and Target Group...
Load Balancer ARN: arn:aws:elasticloadbalancing:us-east-1:123456789012:loadbalancer/app/comptestcrm-alb/...
Target Group ARN: arn:aws:elasticloadbalancing:us-east-1:123456789012:targetgroup/comptestcrm-tg/...
Load Balancer DNS: comptestcrm-alb-1234567890.us-east-1.elb.amazonaws.com

Registering task definition...
Task Definition ARN: arn:aws:ecs:us-east-1:123456789012:task-definition/comptestcrm-task:1

Creating service...
Waiting for service to become stable...

Deployment Completed Successfully!

Service Details:
  Status: ACTIVE
  Desired Count: 2
  Running Count: 2
  Pending Count: 0

CloudWatch Logs: /ecs/comptestcrm
Application URL: http://comptestcrm-alb-1234567890.us-east-1.elb.amazonaws.com
```

### Step 4: Verify Deployment

#### Check Service Status

```bash
aws ecs describe-services \
  --cluster comptestcrm-cluster \
  --services comptestcrm-service \
  --region us-east-1
```

#### Check Running Tasks

```bash
aws ecs list-tasks \
  --cluster comptestcrm-cluster \
  --service-name comptestcrm-service \
  --region us-east-1
```

#### View Logs

```bash
# Stream logs
aws logs tail /ecs/comptestcrm --follow --region us-east-1

# Get recent logs
aws logs tail /ecs/comptestcrm --since 1h --region us-east-1
```

#### Test Application

```bash
# Health check (via ALB)
curl http://<ALB_DNS>/appinfo/health

# Access application
open http://<ALB_DNS>
```

---

## Configuration Management

### Environment Variables

The application supports the following environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SPRING_PROFILES_ACTIVE` | Spring active profile | `docker` | No |
| `JAVA_OPTS` | JVM options | `-Xmx512m -Xms256m` | No |
| `DB_HOST` | Database host | `localhost` | Yes |
| `DB_PORT` | Database port | `3306` | No |
| `DB_NAME` | Database name | `crm` | No |
| `DB_USER` | Database username | `root` | Yes |
| `DB_PASSWORD` | Database password | - | Yes |
| `MANAGEMENT_CONTEXT_PATH` | Actuator context path | `/appinfo` | No |
| `TZ` | Timezone | `UTC` | No |

### Updating Configuration

#### Update Task Definition

1. Modify `ecs/task-definition.json`
2. Re-run deployment script: `./scripts/deploy-image.sh`

#### Update Service Configuration

```bash
aws ecs update-service \
  --cluster comptestcrm-cluster \
  --service comptestcrm-service \
  --desired-count 3 \
  --region us-east-1
```

#### Force New Deployment

```bash
aws ecs update-service \
  --cluster comptestcrm-cluster \
  --service comptestcrm-service \
  --force-new-deployment \
  --region us-east-1
```

---

## Monitoring and Logging

### CloudWatch Logs

**Log Group:** `/ecs/comptestcrm`

**View Logs:**
```bash
# Stream logs in real-time
aws logs tail /ecs/comptestcrm --follow --region us-east-1

# Filter logs
aws logs tail /ecs/comptestcrm --filter-pattern "ERROR" --region us-east-1

# Get logs from specific time
aws logs tail /ecs/comptestcrm --since 2h --region us-east-1
```

### CloudWatch Metrics

ECS automatically publishes metrics to CloudWatch:

- **CPUUtilization**: CPU usage of tasks
- **MemoryUtilization**: Memory usage of tasks
- **TargetResponseTime**: Application response time (if using ALB)
- **RequestCount**: Number of requests (if using ALB)
- **HealthyHostCount**: Number of healthy targets (if using ALB)

**View Metrics:**
```bash
aws cloudwatch get-metric-statistics \
  --namespace AWS/ECS \
  --metric-name CPUUtilization \
  --dimensions Name=ServiceName,Value=comptestcrm-service Name=ClusterName,Value=comptestcrm-cluster \
  --start-time $(date -u -d '1 hour ago' +%Y-%m-%dT%H:%M:%S) \
  --end-time $(date -u +%Y-%m-%dT%H:%M:%S) \
  --period 300 \
  --statistics Average \
  --region us-east-1
```

### Application Health Monitoring

**Spring Boot Actuator Endpoints:**

- Health: `http://<ALB_DNS>/appinfo/health`
- Info: `http://<ALB_DNS>/appinfo/info`
- Metrics: `http://<ALB_DNS>/appinfo/metrics` (if enabled)

**Configure CloudWatch Alarms:**

```bash
# CPU alarm
aws cloudwatch put-metric-alarm \
  --alarm-name comptestcrm-cpu-high \
  --alarm-description "Alert when CPU exceeds 80%" \
  --metric-name CPUUtilization \
  --namespace AWS/ECS \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --evaluation-periods 2 \
  --dimensions Name=ServiceName,Value=comptestcrm-service Name=ClusterName,Value=comptestcrm-cluster \
  --region us-east-1

# Memory alarm
aws cloudwatch put-metric-alarm \
  --alarm-name comptestcrm-memory-high \
  --alarm-description "Alert when memory exceeds 80%" \
  --metric-name MemoryUtilization \
  --namespace AWS/ECS \
  --statistic Average \
  --period 300 \
  --threshold 80 \
  --comparison-operator GreaterThanThreshold \
  --evaluation-periods 2 \
  --dimensions Name=ServiceName,Value=comptestcrm-service Name=ClusterName,Value=comptestcrm-cluster \
  --region us-east-1
```

---

## Troubleshooting

### Common Issues and Solutions

#### 1. Task Failed to Start

**Symptom:** Tasks repeatedly fail to start or stop immediately after starting.

**Possible Causes:**
- Invalid Docker image URI
- Insufficient IAM permissions
- Image not accessible in ECR
- Invalid CPU/memory combination

**Solution:**
```bash
# Check task stopped reason
aws ecs describe-tasks \
  --cluster comptestcrm-cluster \
  --tasks <TASK_ARN> \
  --region us-east-1 \
  --query 'tasks[0].stoppedReason'

# Check CloudWatch logs for errors
aws logs tail /ecs/comptestcrm --region us-east-1

# Verify image exists in ECR
aws ecr describe-images \
  --repository-name comptestcrm \
  --region us-east-1
```

#### 2. Health Check Failures

**Symptom:** Tasks are unhealthy according to load balancer health checks.

**Possible Causes:**
- Application not responding on port 8080
- Health endpoint not accessible
- Database connection issues
- Insufficient startup time

**Solution:**
```bash
# Check task network configuration
aws ecs describe-tasks \
  --cluster comptestcrm-cluster \
  --tasks <TASK_ARN> \
  --region us-east-1

# Test health endpoint directly on task
# Get task private IP
TASK_IP=$(aws ecs describe-tasks --cluster comptestcrm-cluster --tasks <TASK_ARN> --region us-east-1 --query 'tasks[0].attachments[0].details[?name==`privateIPv4Address`].value' --output text)
curl http://$TASK_IP:8080/appinfo/health

# Increase health check grace period
aws ecs update-service \
  --cluster comptestcrm-cluster \
  --service comptestcrm-service \
  --health-check-grace-period-seconds 300 \
  --region us-east-1
```

#### 3. Database Connection Issues

**Symptom:** Application logs show database connection errors.

**Possible Causes:**
- Incorrect database credentials
- Database not accessible from ECS tasks
- Security group rules blocking traffic

**Solution:**
```bash
# Verify database environment variables in task definition
aws ecs describe-task-definition \
  --task-definition comptestcrm-task \
  --region us-east-1 \
  --query 'taskDefinition.containerDefinitions[0].environment'

# Check RDS security group allows inbound from ECS security group
aws ec2 describe-security-groups \
  --group-ids <RDS_SG_ID> \
  --region us-east-1

# Test database connectivity from ECS task
aws ecs execute-command \
  --cluster comptestcrm-cluster \
  --task <TASK_ARN> \
  --container comptestcrm \
  --command "/bin/sh" \
  --interactive
```

#### 4. Out of Memory Errors

**Symptom:** Tasks being killed due to OOM errors.

**Possible Causes:**
- Insufficient memory allocation
- Memory leak in application
- JVM heap size too large for container

**Solution:**
```bash
# Increase task memory in task definition
# Edit ecs/task-definition.json
# Change "memory": "1024" to "memory": "2048"
# Ensure CPU/memory combination is valid for Fargate

# Adjust JVM heap size
# Modify JAVA_OPTS environment variable
# Example: -Xmx768m -Xms384m (leave ~25% for non-heap memory)

# Re-deploy with updated configuration
./scripts/deploy-image.sh
```

#### 5. Slow Application Startup

**Symptom:** Application takes a long time to become healthy.

**Possible Causes:**
- Large application with many dependencies
- Database schema initialization
- JVM warmup time

**Solution:**
```bash
# Increase healthCheckGracePeriodSeconds
aws ecs update-service \
  --cluster comptestcrm-cluster \
  --service comptestcrm-service \
  --health-check-grace-period-seconds 300 \
  --region us-east-1

# Optimize application.properties
# Change spring.jpa.hibernate.ddl-auto to "validate" or "none" in production
# Disable unnecessary Spring Boot auto-configurations
```

#### 6. Service Not Accessible via Load Balancer

**Symptom:** Cannot access application via ALB DNS name.

**Possible Causes:**
- ALB security group not allowing inbound traffic
- Target group health checks failing
- ALB listener not configured correctly

**Solution:**
```bash
# Check ALB status
aws elbv2 describe-load-balancers \
  --names comptestcrm-alb \
  --region us-east-1

# Check target health
aws elbv2 describe-target-health \
  --target-group-arn <TARGET_GROUP_ARN> \
  --region us-east-1

# Verify ALB security group allows inbound port 80
aws ec2 describe-security-groups \
  --group-ids <ALB_SG_ID> \
  --region us-east-1
```

### Debugging Tools

#### Enable ECS Exec for Interactive Debugging

```bash
# Update service to enable execute command
aws ecs update-service \
  --cluster comptestcrm-cluster \
  --service comptestcrm-service \
  --enable-execute-command \
  --region us-east-1

# Get task ARN
TASK_ARN=$(aws ecs list-tasks --cluster comptestcrm-cluster --service-name comptestcrm-service --region us-east-1 --query 'taskArns[0]' --output text)

# Connect to task
aws ecs execute-command \
  --cluster comptestcrm-cluster \
  --task $TASK_ARN \
  --container comptestcrm \
  --command "/bin/sh" \
  --interactive
```

#### Application Logs Analysis

```bash
# Search for errors
aws logs filter-log-events \
  --log-group-name /ecs/comptestcrm \
  --filter-pattern "ERROR" \
  --region us-east-1

# Search for specific exceptions
aws logs filter-log-events \
  --log-group-name /ecs/comptestcrm \
  --filter-pattern "SQLException" \
  --region us-east-1
```

---

## Security Best Practices

### 1. Image Security

- Use minimal base images (eclipse-temurin:8-jre-alpine)
- Run application as non-root user (implemented in Dockerfile)
- Scan images for vulnerabilities:
  ```bash
  aws ecr start-image-scan \
    --repository-name comptestcrm \
    --image-id imageTag=latest \
    --region us-east-1
  ```

### 2. Secrets Management

**Use AWS Secrets Manager for sensitive data:**

```bash
# Create secret for database password
aws secretsmanager create-secret \
  --name comptestcrm/db/password \
  --secret-string "your-password" \
  --region us-east-1

# Update task definition to use secrets
# In ecs/task-definition.json, replace environment variable:
"secrets": [
  {
    "name": "DB_PASSWORD",
    "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789012:secret:comptestcrm/db/password"
  }
]
```

**Grant task execution role access to secrets:**

```bash
cat > secrets-policy.json <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "secretsmanager:GetSecretValue"
      ],
      "Resource": [
        "arn:aws:secretsmanager:us-east-1:123456789012:secret:comptestcrm/*"
      ]
    }
  ]
}
EOF

aws iam put-role-policy \
  --role-name ecsTaskExecutionRole \
  --policy-name SecretsAccess \
  --policy-document file://secrets-policy.json
```

### 3. Network Security

- Use private subnets for ECS tasks (requires NAT Gateway)
- Restrict security group rules to minimum required ports
- Enable VPC Flow Logs for network monitoring
- Use AWS PrivateLink for accessing AWS services

### 4. IAM Best Practices

- Use separate IAM roles for task execution and task
- Follow principle of least privilege
- Enable CloudTrail for API audit logging
- Rotate credentials regularly

### 5. Application Security

- Enable HTTPS on ALB (requires ACM certificate)
- Configure Spring Security properly
- Keep dependencies updated (check for CVEs)
- Enable CSRF protection
- Implement rate limiting

---

## Scaling and Performance

### Auto Scaling Configuration

#### Service Auto Scaling (Task Count)

```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/comptestcrm-cluster/comptestcrm-service \
  --min-capacity 2 \
  --max-capacity 10 \
  --region us-east-1

# Target tracking scaling policy (CPU)
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/comptestcrm-cluster/comptestcrm-service \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration '{
    "TargetValue": 70.0,
    "PredefinedMetricSpecification": {
      "PredefinedMetricType": "ECSServiceAverageCPUUtilization"
    },
    "ScaleOutCooldown": 60,
    "ScaleInCooldown": 300
  }' \
  --region us-east-1

# Target tracking scaling policy (Memory)
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/comptestcrm-cluster/comptestcrm-service \
  --policy-name memory-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration '{
    "TargetValue": 80.0,
    "PredefinedMetricSpecification": {
      "PredefinedMetricType": "ECSServiceAverageMemoryUtilization"
    },
    "ScaleOutCooldown": 60,
    "ScaleInCooldown": 300
  }' \
  --region us-east-1
```

### Performance Tuning

#### JVM Tuning for Containers

Optimal JVM settings for 1GB container:

```bash
JAVA_OPTS="
  -Xmx512m 
  -Xms256m 
  -XX:+UseContainerSupport 
  -XX:MaxRAMPercentage=75.0 
  -XX:InitialRAMPercentage=50.0 
  -XX:+UseG1GC 
  -XX:MaxGCPauseMillis=200 
  -XX:+ParallelRefProcEnabled 
  -XX:+UnlockExperimentalVMOptions 
  -XX:+DisableExplicitGC
"
```

#### Spring Boot Optimizations

**application.properties:**

```properties
# Connection pool settings
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# Tomcat thread pool
server.tomcat.threads.max=200
server.tomcat.threads.min-spare=10
server.tomcat.accept-count=100
server.tomcat.max-connections=10000

# Disable unnecessary features
spring.jmx.enabled=false
spring.devtools.restart.enabled=false
```

### Blue/Green Deployment

```bash
# Create new task definition revision
aws ecs register-task-definition --cli-input-json file://ecs/task-definition.json

# Update service with deployment configuration
aws ecs update-service \
  --cluster comptestcrm-cluster \
  --service comptestcrm-service \
  --task-definition comptestcrm-task:2 \
  --deployment-configuration "{
    \"deploymentCircuitBreaker\": {
      \"enable\": true,
      \"rollback\": true
    },
    \"maximumPercent\": 200,
    \"minimumHealthyPercent\": 100
  }" \
  --region us-east-1
```

### Monitoring Performance

```bash
# View service performance metrics
aws cloudwatch get-metric-statistics \
  --namespace AWS/ECS \
  --metric-name CPUUtilization \
  --dimensions Name=ServiceName,Value=comptestcrm-service Name=ClusterName,Value=comptestcrm-cluster \
  --start-time $(date -u -d '24 hours ago' +%Y-%m-%dT%H:%M:%S) \
  --end-time $(date -u +%Y-%m-%dT%H:%M:%S) \
  --period 3600 \
  --statistics Average,Maximum \
  --region us-east-1
```

---

## Additional Resources

### Documentation

- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS Fargate Documentation](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/)
- [Docker Documentation](https://docs.docker.com/)

### Tools

- [AWS CLI Reference](https://awscli.amazonaws.com/v2/documentation/api/latest/reference/index.html)
- [ECS CLI](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ECS_CLI.html)
- [AWS Copilot](https://aws.github.io/copilot-cli/) - Simplified ECS deployment

### Support

- [AWS Support](https://console.aws.amazon.com/support/)
- [Spring Community](https://spring.io/community)
- [Docker Community](https://www.docker.com/community/)

---

## Conclusion

This guide provides comprehensive instructions for deploying the CompTestCRM Spring Boot application to AWS ECS Fargate. Follow the steps carefully and refer to the troubleshooting section for common issues.

For production deployments, ensure you implement security best practices, monitoring, and auto-scaling configurations appropriate for your workload.

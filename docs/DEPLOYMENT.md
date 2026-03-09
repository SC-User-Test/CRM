# CRM Application - Deployment Guide

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Docker Build and Push](#docker-build-and-push)
4. [AWS ECS Fargate Deployment](#aws-ecs-fargate-deployment)
5. [Configuration Management](#configuration-management)
6. [Troubleshooting](#troubleshooting)
7. [Scaling and Management](#scaling-and-management)

---

## Prerequisites

### Required Software
- **Docker** (v20.10+): Container runtime
- **Docker Compose** (v2.0+): Multi-container orchestration
- **AWS CLI** (v2.0+): AWS command-line interface
- **Git**: Version control
- **Java 8**: For local development
- **Maven 3.6+**: Build tool

### AWS Requirements
- AWS Account with appropriate permissions
- IAM user with programmatic access (Access Key ID and Secret Access Key)
- VPC with at least 2 subnets in different availability zones
- Security Group allowing inbound traffic on port 8080
- ECR repository (can be auto-created by build script)

### IAM Permissions Required
Your AWS user/role must have permissions for:
- ECS (create/update clusters, services, task definitions)
- ECR (push/pull images)
- EC2 (describe VPCs, subnets, security groups)
- IAM (pass role to ECS tasks)
- CloudWatch Logs (create log groups, put log events)
- Elastic Load Balancing (create/manage ALB and target groups)

---

## Local Development Setup

### 1. Clone Repository
```bash
git clone <repository-url>
cd CRMTestComp
```

### 2. Configure Database
The application requires a MySQL database. Update `src/main/resources/application.properties` for local development:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/crm?useSSL=false
spring.datasource.username=root
spring.datasource.password=password
```

Or set environment variables:
```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=crm
export DB_USERNAME=root
export DB_PASSWORD=password
```

### 3. Build Locally
```bash
# Using Maven
mvn clean package

# Run application
java -jar target/crm-0.0.1-SNAPSHOT.jar
```

### 4. Access Application
- Application: http://localhost:8080
- Health Check: http://localhost:8080/appinfo/health
- Actuator Info: http://localhost:8080/appinfo/info

---

## Docker Build and Push

### Build Docker Image Locally
```bash
docker build -t crm:latest .
```

### Run with Docker Compose
```bash
# Update environment variables in docker-compose.yml
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Push to Container Registry

#### Option 1: AWS ECR (Recommended)
```bash
# Make script executable
chmod +x scripts/build-push.sh

# Run build and push script
./scripts/build-push.sh

# Follow prompts:
# - Select registry: 1 (AWS ECR)
# - Enter AWS region (e.g., us-east-1)
# - Enter AWS account ID
# - Enter ECR repository name (default: crm)
# - Enter image tag (default: latest)
```

#### Option 2: Docker Hub
```bash
./scripts/build-push.sh

# Follow prompts:
# - Select registry: 2 (Docker Hub)
# - Enter Docker Hub username
# - Enter Docker Hub password/token
# - Enter image tag (default: latest)
```

#### Windows Users
Use the Windows batch script:
```cmd
scripts\build-push.bat
```

---

## AWS ECS Fargate Deployment

### Overview
AWS ECS (Elastic Container Service) Fargate is a serverless compute engine for containers. This deployment guide uses:
- **Launch Type**: Fargate (no EC2 instance management)
- **Network Mode**: awsvpc (each task gets its own ENI)
- **CPU**: 0.5 vCPU (512 units)
- **Memory**: 1 GB (1024 MB)

### Prerequisites for ECS Fargate

#### 1. VPC and Networking
Ensure you have:
- VPC with internet gateway (for public subnets)
- At least 2 subnets in different availability zones
- Route tables configured for internet access
- Security group allowing:
  - Inbound: Port 8080 (from ALB or internet)
  - Outbound: All traffic (for database and external services)

#### 2. IAM Roles

**ECS Task Execution Role** (required):
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

**ECS Task Role** (optional, for application permissions):
Create if your application needs AWS SDK access (S3, DynamoDB, etc.)

#### 3. CloudWatch Log Group
The deployment script will automatically create:
- Log Group: `/ecs/crm-task`
- Retention: 30 days (default)

### Deployment Steps

#### Step 1: Prepare ECS Artifacts
Ensure the following files exist:
- `ecs/task-definition.json`
- `ecs/service-definition.json`

#### Step 2: Run Deployment Script

**Linux/macOS**:
```bash
# Make script executable
chmod +x scripts/deploy-image.sh

# Run deployment
./scripts/deploy-image.sh
```

**Windows**:
```cmd
scripts\deploy-image.bat
```

#### Step 3: Provide Configuration
The script will prompt for:

1. **AWS Region**: e.g., `us-east-1`
2. **ECS Cluster Name**: e.g., `crm-cluster`
3. **VPC ID**: e.g., `vpc-0123456789abcdef`
4. **Subnet IDs**: e.g., `subnet-111,subnet-222` (comma-separated)
5. **Security Group ID**: e.g., `sg-0123456789abcdef`
6. **ECR Image URI**: e.g., `123456789.dkr.ecr.us-east-1.amazonaws.com/crm:latest`
7. **Database Configuration**:
   - Host: e.g., `my-rds-instance.us-east-1.rds.amazonaws.com`
   - Port: `3306` (default)
   - Name: `crm` (default)
   - Username: e.g., `admin`
   - Password: (hidden input)
8. **Load Balancer**: `y` (yes) or `n` (no)

#### Step 4: Verify Deployment
After deployment completes:

```bash
# Check service status
aws ecs describe-services \
  --cluster crm-cluster \
  --services crm-service \
  --region us-east-1

# View running tasks
aws ecs list-tasks \
  --cluster crm-cluster \
  --service-name crm-service \
  --region us-east-1

# Check CloudWatch logs
aws logs tail /ecs/crm-task --follow --region us-east-1
```

### ECS Task Definition Explained

#### Fargate CPU and Memory
Valid combinations for Fargate:
- CPU: 256 (.25 vCPU) → Memory: 512 MB, 1 GB, 2 GB
- CPU: 512 (.5 vCPU) → Memory: 1 GB, 2 GB, 3 GB, 4 GB
- CPU: 1024 (1 vCPU) → Memory: 2-8 GB (increments of 1 GB)
- CPU: 2048 (2 vCPU) → Memory: 4-16 GB (increments of 1 GB)

Default: CPU 512, Memory 1024 MB

#### Container Definitions
- **Image**: ECR image URI
- **Port Mappings**: Container port 8080 (no host port needed for Fargate)
- **Environment Variables**: Database connection, JVM options
- **Health Check**: Simple exit 0 check (rely on ALB health checks)
- **Logging**: CloudWatch Logs with awslogs driver

### ECS Service Configuration

#### Fargate Launch Type
- **Network Mode**: awsvpc (each task gets ENI with private IP)
- **Assign Public IP**: ENABLED (for internet access in public subnets)

#### Desired Count
- Default: 2 tasks for high availability
- Minimum: 1 task during deployment (50% minimum healthy percent)
- Maximum: 4 tasks during deployment (200% maximum percent)

#### Load Balancer Integration
If enabled:
- **Type**: Application Load Balancer (ALB)
- **Target Type**: IP (required for Fargate awsvpc)
- **Health Check Path**: `/appinfo/health`
- **Health Check Interval**: 30 seconds
- **Healthy Threshold**: 2 consecutive successes
- **Unhealthy Threshold**: 3 consecutive failures

---

## Configuration Management

### Environment Variables
Manage environment variables in:
1. **Local Development**: `docker-compose.yml`
2. **ECS Deployment**: `ecs/task-definition.json`

### Spring Boot Profiles
- **local**: Development with H2 database
- **docker**: Docker Compose with MySQL
- **production**: ECS Fargate with RDS MySQL

Set via environment variable:
```bash
SPRING_PROFILES_ACTIVE=production
```

### Database Secrets
**Best Practice**: Use AWS Secrets Manager

1. Create secret:
```bash
aws secretsmanager create-secret \
  --name crm/db/password \
  --secret-string "your-secure-password" \
  --region us-east-1
```

2. Update task definition to use secrets:
```json
"secrets": [
  {
    "name": "DB_PASSWORD",
    "valueFrom": "arn:aws:secretsmanager:us-east-1:123456789:secret:crm/db/password"
  }
]
```

---

## Troubleshooting

### Common ECS Issues

#### Task Fails to Start
**Symptoms**: Tasks in PENDING state or immediately fail

**Possible Causes**:
1. **Image Pull Error**:
   - Verify ECR image URI is correct
   - Check ECS task execution role has ECR permissions
   - Ensure image exists in ECR

2. **Network Configuration**:
   - Verify subnets have route to internet gateway
   - Check security group allows outbound traffic
   - Ensure NAT gateway (if using private subnets)

3. **CPU/Memory Limits**:
   - Verify valid Fargate CPU/memory combination
   - Check container doesn't exceed task memory limits

**Solution**:
```bash
# View stopped task details
aws ecs describe-tasks \
  --cluster crm-cluster \
  --tasks <task-id> \
  --region us-east-1
```

#### Health Check Failures
**Symptoms**: Tasks start but fail ALB health checks

**Possible Causes**:
- Application not starting properly (check logs)
- Health check path incorrect
- Security group not allowing ALB → ECS traffic
- Application taking too long to start (increase `healthCheckGracePeriodSeconds`)

**Solution**:
```bash
# Check CloudWatch logs
aws logs tail /ecs/crm-task --follow --region us-east-1

# Test health endpoint from ECS task
aws ecs execute-command \
  --cluster crm-cluster \
  --task <task-id> \
  --container crm-container \
  --interactive \
  --command "/bin/sh"
```

#### Database Connection Issues
**Symptoms**: Application logs show database connection errors

**Possible Causes**:
- Database security group doesn't allow ECS traffic
- Database credentials incorrect
- Database not accessible from ECS subnets

**Solution**:
1. Verify RDS security group allows inbound from ECS security group
2. Test database connectivity from ECS task
3. Check environment variables in task definition

### View Logs

**CloudWatch Logs**:
```bash
# Tail logs
aws logs tail /ecs/crm-task --follow --region us-east-1

# Filter logs
aws logs filter-log-events \
  --log-group-name /ecs/crm-task \
  --filter-pattern "ERROR" \
  --region us-east-1
```

**ECS Events**:
```bash
aws ecs describe-services \
  --cluster crm-cluster \
  --services crm-service \
  --region us-east-1 \
  --query 'services[0].events[:10]'
```

---

## Scaling and Management

### Manual Scaling
```bash
# Scale to 4 tasks
aws ecs update-service \
  --cluster crm-cluster \
  --service crm-service \
  --desired-count 4 \
  --region us-east-1
```

### Auto Scaling
Enable Service Auto Scaling:

```bash
# Register scalable target
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/crm-cluster/crm-service \
  --min-capacity 2 \
  --max-capacity 10 \
  --region us-east-1

# Create scaling policy (target tracking)
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/crm-cluster/crm-service \
  --policy-name cpu-target-tracking \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration file://scaling-policy.json \
  --region us-east-1
```

**scaling-policy.json**:
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

### Blue/Green Deployments
For zero-downtime deployments:

1. Update task definition with new image
2. ECS creates new tasks with new version
3. ALB health checks pass for new tasks
4. Traffic gradually shifts to new tasks
5. Old tasks are drained and terminated

Configure in service definition:
```json
"deploymentConfiguration": {
  "maximumPercent": 200,
  "minimumHealthyPercent": 100
}
```

### Rolling Back
```bash
# List task definition revisions
aws ecs list-task-definitions \
  --family-prefix crm-task \
  --region us-east-1

# Rollback to previous version
aws ecs update-service \
  --cluster crm-cluster \
  --service crm-service \
  --task-definition crm-task:1 \
  --region us-east-1
```

---

## Security Best Practices

1. **Use AWS Secrets Manager** for sensitive data (database passwords, API keys)
2. **Enable VPC Flow Logs** for network monitoring
3. **Use private subnets** with NAT gateway for production
4. **Implement least privilege IAM roles** for tasks
5. **Enable CloudTrail** for API audit logging
6. **Use HTTPS/TLS** with ALB and ACM certificates
7. **Regularly update base images** to patch vulnerabilities
8. **Scan images** with Amazon ECR image scanning

---

## Technology-Specific Notes

### Spring Boot 1.5.x
- Health endpoint: `/appinfo/health` (not `/actuator/health`)
- Management context path: `/appinfo`
- Actuator security disabled in application.properties

### Java 8 / Amazon Corretto
- Base image: `amazoncorretto:8`
- JVM options optimized for containers
- Uses container-aware memory settings

### MySQL Database
- Application supports both H2 (dev) and MySQL (production)
- Connection pooling configured via Spring Boot defaults
- Hibernate DDL set to `create-drop` (change to `validate` for production)

---

## Support and Resources

- [AWS ECS Documentation](https://docs.aws.amazon.com/ecs/)
- [AWS Fargate Documentation](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/AWS_Fargate.html)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/1.5.x/reference/html/)
- [Docker Documentation](https://docs.docker.com/)

---

## License
[Add your license information here]

## Contact
[Add contact information here]
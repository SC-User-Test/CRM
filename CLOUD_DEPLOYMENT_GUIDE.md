# Cloud Deployment Guide - CompTestCRM

## Overview
This application has been modernized for cloud deployment on AWS, Azure, or GCP. All cloud readiness blockers have been resolved.

## Cloud Readiness Fixes Applied

### 1. Hard-coded File Paths (cr-java-0061)
**File:** `ReadDataUtils.java`
**Fix:** Replaced absolute file paths with environment variable references and classpath resources.
- Uses `ClassPathResource` for bundled resources
- Supports environment variables: `DATA_DIRECTORY`, `UPLOAD_DIRECTORY`
- Default paths use `/tmp` for cloud compatibility

### 2. Local File System Write Operations (cr-java-0062)
**File:** `PdfController.java`
**Fix:** Replaced local file writes with persistent volume mount support.
- Uses environment variable: `PDF_STORAGE_PATH`
- Creates directories automatically if they don't exist
- Compatible with AWS EFS, Azure Files, GCP Filestore
- Default path: `/tmp/pdfs` (should be mounted persistent volume in production)

### 3. Java.io.File Usage for Data Storage (cr-java-0063)
**File:** `CSVTest.java`
**Fix:** Replaced `java.io.File` with cloud-native resource loading.
- Supports classpath resources via `ClassPathResource`
- Supports persistent storage via environment variable: `CSV_STORAGE_PATH`
- Uses Spring's `Resource` abstraction for cloud compatibility

### 4. Static Initializers with I/O (cr-java-0105)
**File:** `CSVTest.java`
**Fix:** Removed static `main` method with I/O operations.
- Converted to Spring `@Component` with dependency injection
- I/O operations moved to instance methods
- Supports lazy initialization and proper error handling

### 5. Hardcoded Database Credentials
**File:** `application.properties`
**Fix:** Externalized all configuration to environment variables.
- Database URL: `DB_URL`
- Database username: `DB_USERNAME`
- Database password: `DB_PASSWORD`
- All sensitive values use environment variables

## Environment Variables

### Required for Production

```bash
# Database Configuration
export DB_URL="jdbc:mysql://your-rds-endpoint:3306/crm?useSSL=true"
export DB_USERNAME="your-db-username"
export DB_PASSWORD="your-db-password"

# File Storage Paths (mount persistent volumes to these paths)
export PDF_STORAGE_PATH="/mnt/efs/pdfs"
export CSV_STORAGE_PATH="/mnt/efs/csv"
export UPLOAD_STORAGE_PATH="/mnt/efs/uploads"

# Application Configuration
export SERVER_PORT="8080"
export APP_NAME="CompTestCRM"
```

### Optional Configuration

```bash
# Database Pool Configuration
export DB_POOL_SIZE="20"
export DB_POOL_MIN_IDLE="5"
export DB_CONNECTION_TIMEOUT="30000"

# Logging Configuration
export LOG_LEVEL_ROOT="INFO"
export LOG_LEVEL_APP="DEBUG"

# AWS Configuration (if using AWS services)
export AWS_REGION="us-east-1"
export S3_BUCKET_NAME="your-bucket-name"
```

## AWS Deployment

### Prerequisites
1. RDS MySQL database instance
2. EFS file system for persistent storage
3. ECS/EKS cluster or Elastic Beanstalk environment
4. Secrets Manager for database credentials (recommended)

### Deployment Steps

#### 1. Create RDS Database
```bash
aws rds create-db-instance \
  --db-instance-identifier crm-db \
  --db-instance-class db.t3.micro \
  --engine mysql \
  --master-username admin \
  --master-user-password <password> \
  --allocated-storage 20
```

#### 2. Create EFS File System
```bash
aws efs create-file-system \
  --performance-mode generalPurpose \
  --throughput-mode bursting \
  --tags Key=Name,Value=crm-storage
```

#### 3. Store Secrets in AWS Secrets Manager
```bash
aws secretsmanager create-secret \
  --name crm/db/credentials \
  --secret-string '{"username":"admin","password":"<password>"}'
```

#### 4. Deploy to ECS

**Dockerfile:**
```dockerfile
FROM openjdk:8-jre-alpine
VOLUME /tmp
VOLUME /mnt/efs
COPY target/crm-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
```

**ECS Task Definition:**
```json
{
  "family": "crm-app",
  "containerDefinitions": [
    {
      "name": "crm",
      "image": "your-ecr-repo/crm:latest",
      "memory": 1024,
      "cpu": 512,
      "essential": true,
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "DB_URL",
          "value": "jdbc:mysql://your-rds-endpoint:3306/crm?useSSL=true"
        },
        {
          "name": "PDF_STORAGE_PATH",
          "value": "/mnt/efs/pdfs"
        }
      ],
      "secrets": [
        {
          "name": "DB_USERNAME",
          "valueFrom": "arn:aws:secretsmanager:region:account:secret:crm/db/credentials:username::"
        },
        {
          "name": "DB_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:region:account:secret:crm/db/credentials:password::"
        }
      ],
      "mountPoints": [
        {
          "sourceVolume": "efs-storage",
          "containerPath": "/mnt/efs"
        }
      ]
    }
  ],
  "volumes": [
    {
      "name": "efs-storage",
      "efsVolumeConfiguration": {
        "fileSystemId": "fs-xxxxxxxx",
        "transitEncryption": "ENABLED"
      }
    }
  ]
}
```

#### 5. Deploy to Elastic Beanstalk

Create `.ebextensions/environment.config`:
```yaml
option_settings:
  aws:elasticbeanstalk:application:environment:
    DB_URL: jdbc:mysql://your-rds-endpoint:3306/crm?useSSL=true
    PDF_STORAGE_PATH: /mnt/efs/pdfs
    CSV_STORAGE_PATH: /mnt/efs/csv
    UPLOAD_STORAGE_PATH: /mnt/efs/uploads
    SERVER_PORT: 5000
```

Deploy:
```bash
eb init -p java-8 crm-app
eb create crm-env
eb deploy
```

## Azure Deployment

### Prerequisites
1. Azure Database for MySQL
2. Azure Files for persistent storage
3. Azure Container Instances or AKS

### Deployment Steps

#### 1. Create Azure Database for MySQL
```bash
az mysql server create \
  --resource-group crm-rg \
  --name crm-mysql \
  --admin-user admin \
  --admin-password <password> \
  --sku-name B_Gen5_1
```

#### 2. Create Azure Files Share
```bash
az storage share create \
  --name crm-storage \
  --account-name crmstorageaccount
```

#### 3. Deploy to Azure Container Instances
```bash
az container create \
  --resource-group crm-rg \
  --name crm-app \
  --image your-acr.azurecr.io/crm:latest \
  --cpu 1 \
  --memory 1 \
  --ports 8080 \
  --environment-variables \
    DB_URL="jdbc:mysql://crm-mysql.mysql.database.azure.com:3306/crm?useSSL=true" \
    PDF_STORAGE_PATH="/mnt/azure/pdfs" \
  --secure-environment-variables \
    DB_USERNAME="admin@crm-mysql" \
    DB_PASSWORD="<password>" \
  --azure-file-volume-account-name crmstorageaccount \
  --azure-file-volume-account-key <key> \
  --azure-file-volume-share-name crm-storage \
  --azure-file-volume-mount-path /mnt/azure
```

## GCP Deployment

### Prerequisites
1. Cloud SQL MySQL instance
2. GCS bucket with FUSE mount or Filestore
3. GKE cluster or Cloud Run

### Deployment Steps

#### 1. Create Cloud SQL Instance
```bash
gcloud sql instances create crm-db \
  --database-version=MYSQL_5_7 \
  --tier=db-f1-micro \
  --region=us-central1
```

#### 2. Create GCS Bucket
```bash
gsutil mb gs://crm-storage-bucket
```

#### 3. Deploy to Cloud Run
```bash
gcloud run deploy crm-app \
  --image gcr.io/your-project/crm:latest \
  --platform managed \
  --region us-central1 \
  --set-env-vars DB_URL="jdbc:mysql:///<crm>?cloudSqlInstance=project:region:crm-db&socketFactory=com.google.cloud.sql.mysql.SocketFactory" \
  --set-env-vars PDF_STORAGE_PATH="/mnt/gcs/pdfs" \
  --set-secrets DB_USERNAME=crm-db-username:latest,DB_PASSWORD=crm-db-password:latest
```

## Health Checks

The application exposes health check endpoints via Spring Boot Actuator:

- **Health:** `http://localhost:8080/appinfo/health`
- **Info:** `http://localhost:8080/appinfo/info`
- **Metrics:** `http://localhost:8080/appinfo/metrics`

Configure your load balancer to use the health endpoint.

## Monitoring

The application includes Micrometer for metrics collection. Configure your cloud monitoring:

- **AWS:** CloudWatch integration
- **Azure:** Azure Monitor integration
- **GCP:** Stackdriver integration

## Security Recommendations

1. **Use Secrets Management:**
   - AWS: Secrets Manager or Parameter Store
   - Azure: Key Vault
   - GCP: Secret Manager

2. **Enable SSL/TLS:**
   - Set `server.ssl.enabled=true`
   - Configure SSL certificates

3. **Use IAM Roles:**
   - Avoid hardcoded credentials
   - Use instance profiles (AWS) or managed identities (Azure)

4. **Network Security:**
   - Use security groups/firewall rules
   - Restrict database access to application subnet
   - Use VPC/VNet for isolation

## Troubleshooting

### Issue: Application cannot write files
**Solution:** Ensure persistent volume is mounted and writable:
```bash
# Check mount
df -h | grep /mnt/efs

# Check permissions
ls -la /mnt/efs
```

### Issue: Database connection fails
**Solution:** Verify environment variables and network connectivity:
```bash
# Check environment variables
env | grep DB_

# Test database connection
mysql -h your-rds-endpoint -u username -p
```

### Issue: Out of memory
**Solution:** Increase container memory or adjust JVM settings:
```bash
# Add JVM options
JAVA_OPTS="-Xmx512m -Xms256m"
```

## Build and Package

```bash
# Build the application
mvn clean package

# Run locally with environment variables
export DB_URL="jdbc:mysql://localhost:3306/crm?useSSL=false"
export DB_USERNAME="root"
export DB_PASSWORD="password"
java -jar target/crm-0.0.1-SNAPSHOT.jar
```

## Support

For issues or questions, contact the development team.

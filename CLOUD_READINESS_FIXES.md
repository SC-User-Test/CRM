# Cloud Readiness Fixes - CRM Application

## Overview
This document describes the cloud-native transformations applied to make the CRM application fully compatible with AWS cloud environments.

## Changes Summary

### 1. File System Dependencies → Amazon S3 Storage

#### **ReadDataUtils.java** (Blocker #1 - cr-java-0061)
- **Issue**: Hard-coded file paths with JFileChooser (Swing GUI component)
- **Fix**: Replaced with Amazon S3 SDK for Java v2
- **Changes**:
  - Removed Swing dependencies (JFileChooser, JFrame)
  - Implemented S3-based file reading using `S3Client`
  - Added methods: `readFileFromS3()` and `readFileStreamFromS3()`
  - Uses environment variable `S3_BUCKET_NAME` for bucket configuration

#### **PdfController.java** (Blocker #2 - cr-java-0062)
- **Issue**: Local file system write operations using `FileOutputStream`
- **Fix**: Migrated to Amazon S3 for durable PDF storage
- **Changes**:
  - Replaced `FileOutputStream` with in-memory `ByteArrayOutputStream`
  - Implemented `uploadToS3()` method using S3 `PutObjectRequest`
  - PDFs are now generated in memory and uploaded directly to S3
  - Uses environment variables: `S3_BUCKET_NAME`, `S3_PDF_PREFIX`
  - Added proper error handling and logging for S3 operations

#### **CSVTest.java** (Blocker #3 - cr-java-0063)
- **Issue**: java.io.File usage for CSV data storage
- **Fix**: Migrated to Amazon S3 using AWS SDK for Java v2
- **Changes**:
  - Removed `java.io.File` and local file system dependencies
  - Implemented `processCSVFromS3()` method to read CSV from S3
  - Uses `GetObjectRequest` to download CSV files from S3
  - Configurable via environment variables: `S3_BUCKET_NAME`, `S3_CSV_KEY`

### 2. Clock/Time Dependencies → java.time API with UTC

#### **DateTimeTestController.java** (Blockers #4 & #5 - cr-java-0111)
- **Issue**: Usage of `java.util.Date` which relies on server-local timezone
- **Fix**: Migrated to java.time API with UTC standardization
- **Changes**:
  - Removed `java.util.Date` (line 19)
  - Replaced with `Instant`, `ZonedDateTime`, `LocalDateTime` from java.time
  - All timestamps now use `Clock.systemUTC()` for consistency
  - Standardized on UTC timezone (`ZoneId.of("UTC")`)
  - Added ISO-8601 formatted timestamps for inter-service communication

### 3. Infrastructure Configuration

#### **pom.xml**
Added AWS SDK for Java v2 dependencies:
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.17.100</version>
</dependency>
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>auth</artifactId>
    <version>2.17.100</version>
</dependency>
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>regions</artifactId>
    <version>2.17.100</version>
</dependency>
```

#### **AwsS3Config.java** (New File)
- Created Spring configuration class for S3Client bean
- Uses `DefaultCredentialsProvider` for automatic AWS credential resolution
- Supports multiple credential sources:
  - Environment variables (AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY)
  - EC2 instance profile credentials
  - ECS task role credentials
  - AWS credentials file (~/.aws/credentials)
- Configurable AWS region via `AWS_REGION` environment variable

#### **application.properties**
Added cloud-native configuration properties:
```properties
# AWS S3 Configuration
s3.bucket.name=${S3_BUCKET_NAME:crm-data-bucket}
s3.pdf.prefix=${S3_PDF_PREFIX:pdfs/}
s3.csv.key=${S3_CSV_KEY:data/input.csv}

# AWS Region Configuration
aws.region=${AWS_REGION:us-east-1}
```

## Cloud Deployment Requirements

### Environment Variables
The application requires the following environment variables for AWS deployment:

| Variable | Description | Default Value |
|----------|-------------|---------------|
| `S3_BUCKET_NAME` | S3 bucket for data storage | `crm-data-bucket` |
| `S3_PDF_PREFIX` | S3 prefix for PDF files | `pdfs/` |
| `S3_CSV_KEY` | S3 key for CSV input file | `data/input.csv` |
| `AWS_REGION` | AWS region for S3 operations | `us-east-1` |
| `AWS_ACCESS_KEY_ID` | AWS access key (if not using IAM roles) | - |
| `AWS_SECRET_ACCESS_KEY` | AWS secret key (if not using IAM roles) | - |

### IAM Permissions
The application requires the following IAM permissions:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetObject",
        "s3:PutObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::crm-data-bucket/*",
        "arn:aws:s3:::crm-data-bucket"
      ]
    }
  ]
}
```

### S3 Bucket Setup
1. Create an S3 bucket (e.g., `crm-data-bucket`)
2. Create the following folder structure:
   - `pdfs/` - for generated PDF files
   - `data/` - for CSV input files
3. Configure bucket policies and CORS if needed for web access

## 12-Factor App Compliance

The application now follows 12-factor app principles:

1. **Codebase**: Single codebase tracked in version control
2. **Dependencies**: All dependencies explicitly declared in pom.xml
3. **Config**: Configuration stored in environment variables
4. **Backing Services**: S3 treated as attached resource via environment config
5. **Build, Release, Run**: Strict separation maintained
6. **Processes**: Application is stateless (no local file storage)
7. **Port Binding**: Spring Boot embedded server
8. **Concurrency**: Stateless design enables horizontal scaling
9. **Disposability**: Fast startup, graceful shutdown
10. **Dev/Prod Parity**: Same S3 storage pattern across environments
11. **Logs**: Structured logging to stdout (SLF4J)
12. **Admin Processes**: Can be run as one-off processes

## Testing in Cloud Environment

### Local Testing with LocalStack
```bash
# Start LocalStack for S3 emulation
docker run -d -p 4566:4566 localstack/localstack

# Set environment variables
export AWS_REGION=us-east-1
export S3_BUCKET_NAME=crm-data-bucket
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_ENDPOINT_URL=http://localhost:4566

# Create test bucket
aws --endpoint-url=http://localhost:4566 s3 mb s3://crm-data-bucket

# Run application
mvn spring-boot:run
```

### AWS Deployment
The application is ready for deployment to:
- **AWS Elastic Beanstalk**: Configure environment variables in EB console
- **AWS ECS/Fargate**: Set environment variables in task definition
- **AWS EKS**: Use ConfigMaps and Secrets for configuration
- **AWS Lambda**: Package as Spring Cloud Function

## Benefits of Cloud-Native Transformation

1. **Durability**: Data stored in S3 with 99.999999999% durability
2. **Scalability**: Horizontal scaling without shared file system concerns
3. **Availability**: Multi-AZ S3 storage ensures high availability
4. **Cost Efficiency**: Pay only for storage used, no local disk provisioning
5. **Consistency**: UTC timestamps ensure consistent time handling across regions
6. **Portability**: Works across AWS regions and availability zones
7. **Security**: IAM-based access control for S3 resources
8. **Compliance**: Meets cloud-native architecture standards

## Migration Notes

### Breaking Changes
1. **ReadDataUtils**: No longer supports Swing GUI file chooser - requires S3 key parameter
2. **PdfController**: PDFs are stored in S3, not local file system
3. **CSVTest**: Requires CSV files to be uploaded to S3 before processing
4. **DateTimeTestController**: Returns UTC timestamps instead of server-local time

### Backward Compatibility
- The application maintains all business logic
- API endpoints remain unchanged
- Database schema is unaffected
- User-facing functionality is preserved

## Troubleshooting

### S3 Access Issues
- Verify IAM permissions are correctly configured
- Check S3 bucket name and region match environment variables
- Ensure AWS credentials are available (IAM role or environment variables)

### Time Zone Issues
- All timestamps are now in UTC
- Update frontend/client code to handle UTC timestamps
- Use `ZonedDateTime` for timezone-specific display

## Next Steps

1. **Create S3 buckets** in target AWS account
2. **Configure IAM roles** with required S3 permissions
3. **Set environment variables** in deployment platform
4. **Upload test data** to S3 for CSV processing
5. **Deploy application** to AWS environment
6. **Monitor CloudWatch logs** for S3 operation status

## Support

For issues related to cloud deployment, refer to:
- AWS SDK for Java v2 Documentation: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/
- Spring Boot on AWS: https://spring.io/guides/gs/spring-boot-aws/
- AWS S3 Best Practices: https://docs.aws.amazon.com/AmazonS3/latest/userguide/best-practices.html

# CRM Application - Cloud-Native Migration

## Overview
This CRM application has been migrated to be fully cloud-ready for AWS deployment. All local file system dependencies have been replaced with Amazon S3 object storage, and time handling has been standardized to UTC for distributed cloud environments.

## Cloud Readiness Fixes Applied

### 1. File Storage Migration to Amazon S3

#### **ReadDataUtils.java** (Blocker ID: blocker-1)
- **Issue**: Hard-coded file paths with JFileChooser GUI dependencies
- **Fix**: Replaced with S3-based file reading using AWS SDK for Java v2
- **Changes**:
  - Removed Swing/JFileChooser dependencies (not suitable for cloud/headless environments)
  - Implemented `readFileFromS3()` method to read files from S3 buckets
  - Added `listFilesFromS3()` method to list and filter S3 objects
  - Made class a Spring `@Component` for dependency injection

#### **PdfController.java** (Blocker ID: blocker-2)
- **Issue**: Local file system write operations using `FileOutputStream`
- **Fix**: Migrated to Amazon S3 for durable PDF storage
- **Changes**:
  - Replaced `FileOutputStream` with `ByteArrayOutputStream` for in-memory PDF generation
  - Implemented `generateAndUploadPdfToS3()` method to upload PDFs directly to S3
  - Added S3Client dependency injection
  - PDFs are now stored with proper S3 keys (e.g., `pdfs/filename.pdf`)
  - Enhanced error handling and logging for cloud operations

#### **CSVTest.java** (Blocker ID: blocker-3)
- **Issue**: java.io.File usage for CSV data storage
- **Fix**: Migrated to S3-based CSV reading
- **Changes**:
  - Removed `java.io.File` and `FileReader` dependencies
  - Implemented `readCsvFromS3()` method to read CSV files from S3
  - Converted from standalone main class to Spring `@Component`
  - Maintained OpenCSV parsing logic while reading from S3 InputStream
  - Added support for custom bucket names

### 2. Time/Clock Dependencies Standardization

#### **DateTimeTestController.java** (Blocker IDs: blocker-4, blocker-5)
- **Issue**: Usage of `java.util.Date` which relies on local system timezone
- **Fix**: Migrated to `java.time` API with UTC standardization
- **Changes**:
  - Removed `java.util.Date` usage
  - Implemented UTC-based `Clock` for consistent time handling
  - Added `Instant`, `ZonedDateTime`, and `LocalDateTime` with UTC clock
  - All timestamps now use UTC for storage and inter-service communication
  - Added ISO-8601 formatted timestamps for API compatibility

## AWS Configuration

### Required AWS Services
- **Amazon S3**: Object storage for files, PDFs, and CSV data
- **IAM**: Identity and Access Management for secure credential handling

### Environment Variables

Set these environment variables for cloud deployment:

```bash
# AWS Region
AWS_REGION=us-east-1

# AWS Credentials (for local development only - use IAM roles in production)
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key

# S3 Bucket Configuration
AWS_S3_BUCKET_NAME=crm-storage-bucket
AWS_S3_PDF_PREFIX=pdfs/
AWS_S3_CSV_PREFIX=csv/
```

### IAM Policy Requirements

The application requires the following S3 permissions:

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
        "arn:aws:s3:::crm-storage-bucket",
        "arn:aws:s3:::crm-storage-bucket/*"
      ]
    }
  ]
}
```

### S3 Bucket Setup

1. Create an S3 bucket:
```bash
aws s3 mb s3://crm-storage-bucket --region us-east-1
```

2. Configure bucket for application use:
```bash
# Create folder structure
aws s3api put-object --bucket crm-storage-bucket --key pdfs/
aws s3api put-object --bucket crm-storage-bucket --key csv/
```

## Dependencies Added

### AWS SDK for Java v2
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.17.100</version>
</dependency>
```

## Configuration Files

### application.properties
Cloud-native configuration with environment variable support:
- AWS region configuration
- S3 bucket name and prefixes
- Externalized configuration for different environments

### AwsS3Config.java
Spring configuration class that provides:
- S3Client bean with proper credential chain
- Support for multiple credential sources (environment, IAM roles, credentials file)
- Region configuration

## Deployment Considerations

### Local Development
1. Install AWS CLI and configure credentials:
```bash
aws configure
```

2. Create local S3 bucket or use LocalStack for S3 emulation:
```bash
docker run -d -p 4566:4566 localstack/localstack
```

3. Set environment variables in your IDE or shell

### Cloud Deployment (AWS)

#### EC2 Deployment
- Attach IAM role with S3 permissions to EC2 instance
- No need to configure AWS credentials explicitly
- Set environment variables via user data or configuration management

#### ECS/Fargate Deployment
- Attach IAM role to ECS task definition
- Configure environment variables in task definition
- S3 credentials automatically provided via task role

#### EKS Deployment
- Use IAM Roles for Service Accounts (IRSA)
- Configure service account with S3 permissions
- Set environment variables in Kubernetes deployment manifest

## Migration Benefits

1. **Scalability**: S3 provides unlimited storage capacity
2. **Durability**: 99.999999999% (11 9's) durability for stored objects
3. **Availability**: Multi-AZ redundancy by default
4. **Cost-Effective**: Pay only for storage used, no infrastructure management
5. **Cloud-Native**: Fully compatible with containerized and serverless deployments
6. **Time Consistency**: UTC standardization prevents timezone issues in distributed systems

## Testing

### Unit Testing with S3
Use AWS SDK's S3Mock or LocalStack for testing:

```java
@TestConfiguration
public class TestAwsConfig {
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .endpointOverride(URI.create("http://localhost:4566"))
            .region(Region.US_EAST_1)
            .build();
    }
}
```

### Integration Testing
1. Set up test S3 bucket
2. Configure test environment variables
3. Run integration tests with actual S3 operations

## Troubleshooting

### Common Issues

1. **Credentials Not Found**
   - Ensure AWS credentials are configured
   - Check IAM role attachment for cloud deployments
   - Verify environment variables are set correctly

2. **S3 Bucket Access Denied**
   - Verify IAM policy includes required S3 permissions
   - Check bucket policy and CORS configuration
   - Ensure bucket name is correct

3. **Region Mismatch**
   - Verify AWS_REGION environment variable
   - Ensure S3 bucket exists in the specified region

## Next Steps

1. Set up CI/CD pipeline for automated deployments
2. Configure CloudWatch logging for application monitoring
3. Implement S3 lifecycle policies for cost optimization
4. Add CloudFront CDN for PDF/file distribution
5. Implement S3 event notifications for file processing workflows

## Support

For issues or questions regarding the cloud migration, please contact the development team.

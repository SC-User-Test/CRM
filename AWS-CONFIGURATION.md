# AWS Cloud Readiness Configuration Guide

## Overview
This application has been migrated to use cloud-native AWS services for storage and configuration management.

## AWS Services Used

### 1. Amazon S3 (Simple Storage Service)
- **Purpose**: Durable, scalable object storage for files (PDFs, CSVs, etc.)
- **Replaces**: Local file system operations
- **Benefits**: 
  - Data durability (99.999999999% durability)
  - Scalability and high availability
  - No ephemeral storage issues in containers
  - Cross-region replication support

## Configuration Requirements

### Environment Variables

Set the following environment variables for AWS configuration:

```bash
# Required: S3 Bucket Name
export AWS_S3_BUCKET_NAME=crm-application-storage

# Required: AWS Region
export AWS_REGION=us-east-1

# AWS Credentials (use IAM roles in production)
export AWS_ACCESS_KEY_ID=your-access-key-id
export AWS_SECRET_ACCESS_KEY=your-secret-access-key
```

### IAM Permissions

The application requires the following IAM permissions:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:PutObject",
        "s3:GetObject",
        "s3:DeleteObject",
        "s3:HeadObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::crm-application-storage/*",
        "arn:aws:s3:::crm-application-storage"
      ]
    }
  ]
}
```

### S3 Bucket Setup

1. Create an S3 bucket:
```bash
aws s3 mb s3://crm-application-storage --region us-east-1
```

2. Configure bucket versioning (recommended):
```bash
aws s3api put-bucket-versioning \
  --bucket crm-application-storage \
  --versioning-configuration Status=Enabled
```

3. Configure bucket lifecycle policies (optional):
```bash
aws s3api put-bucket-lifecycle-configuration \
  --bucket crm-application-storage \
  --lifecycle-configuration file://lifecycle-policy.json
```

## Application Changes

### 1. File Storage Migration
- **Before**: Files stored in local file system
- **After**: Files stored in Amazon S3
- **Impact**: All file operations now use S3StorageService

### 2. Time/Date Handling
- **Before**: Used java.util.Date with local timezone
- **After**: Uses java.time API with UTC standardization
- **Impact**: Consistent time handling across distributed environments

### 3. Configuration Management
- **Before**: Hardcoded values
- **After**: Environment variable-based configuration
- **Impact**: Follows 12-factor app principles

## Deployment Considerations

### AWS ECS/EKS Deployment
- Use IAM roles for service accounts (IRSA for EKS)
- Mount environment variables via task definitions
- Use AWS Secrets Manager for sensitive configuration

### AWS Elastic Beanstalk
- Configure environment properties in EB console
- Use IAM instance profile for S3 access

### AWS Lambda (if applicable)
- Configure environment variables in Lambda function
- Attach IAM execution role with S3 permissions

## Testing

### Local Testing
1. Install AWS CLI and configure credentials:
```bash
aws configure
```

2. Create test S3 bucket:
```bash
aws s3 mb s3://crm-application-storage-dev
```

3. Set environment variables:
```bash
export AWS_S3_BUCKET_NAME=crm-application-storage-dev
export AWS_REGION=us-east-1
```

4. Run the application:
```bash
mvn spring-boot:run
```

### Integration Testing
- Use LocalStack for local S3 emulation
- Configure test profiles with separate S3 buckets

## Monitoring and Logging

### CloudWatch Integration
- Application logs include S3 operation details
- Monitor S3 metrics in CloudWatch
- Set up alarms for S3 operation failures

### S3 Access Logging
Enable S3 access logging for audit trails:
```bash
aws s3api put-bucket-logging \
  --bucket crm-application-storage \
  --bucket-logging-status file://logging-config.json
```

## Cost Optimization

1. **S3 Storage Classes**: Use S3 Intelligent-Tiering for automatic cost optimization
2. **Lifecycle Policies**: Archive old files to S3 Glacier
3. **Request Optimization**: Implement caching where appropriate

## Security Best Practices

1. **Encryption**: Enable S3 server-side encryption (SSE-S3 or SSE-KMS)
2. **Access Control**: Use IAM roles, not access keys
3. **Bucket Policies**: Restrict public access
4. **VPC Endpoints**: Use S3 VPC endpoints for private connectivity

## Troubleshooting

### Common Issues

1. **Access Denied Errors**
   - Verify IAM permissions
   - Check bucket policy
   - Ensure correct AWS region

2. **Bucket Not Found**
   - Verify bucket name in environment variables
   - Ensure bucket exists in the specified region

3. **Credentials Not Found**
   - Check AWS credentials configuration
   - Verify IAM role attachment (for EC2/ECS/EKS)

## Migration Checklist

- [x] Replace local file operations with S3
- [x] Update time/date handling to use UTC
- [x] Externalize configuration to environment variables
- [x] Add AWS SDK dependencies
- [x] Create S3StorageService
- [x] Update controllers and utilities
- [ ] Create S3 bucket in AWS
- [ ] Configure IAM roles and policies
- [ ] Set up environment variables
- [ ] Test in cloud environment
- [ ] Configure monitoring and alerts

## Support

For issues or questions:
- Review AWS S3 documentation: https://docs.aws.amazon.com/s3/
- Check application logs for detailed error messages
- Verify AWS service health: https://status.aws.amazon.com/

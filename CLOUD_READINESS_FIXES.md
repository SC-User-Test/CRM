# Cloud Readiness Fixes - FlightBooking Application

## Overview
This document describes the cloud readiness fixes applied to make the FlightBooking (CRM) application fully compatible with AWS cloud deployment.

## Issues Fixed

### 1. Hard-coded File Paths (cr-java-0061)
**File:** `src/main/java/crm/utils/ReadDataUtils.java`
**Issue:** Application used JFileChooser with absolute file paths that don't exist in cloud environments.
**Fix:** 
- Replaced local file system operations with Amazon S3 SDK for Java v2
- Implemented cloud-native file reading from S3 buckets
- Added methods: `readFileFromS3()`, `listFilesInS3()`, `listFilesByExtension()`
- Uses IAM roles for authentication (DefaultCredentialsProvider)
- Configuration via environment variables: `AWS_S3_BUCKET_NAME`, `AWS_REGION`

### 2. Local File System Write Operations (cr-java-0062)
**File:** `src/main/java/crm/controller/PdfController.java`
**Issue:** Application wrote PDF files directly to local file system using FileOutputStream.
**Fix:**
- Replaced FileOutputStream with in-memory ByteArrayOutputStream
- Implemented S3 upload using AWS SDK v2 PutObject API
- PDFs are now stored durably in S3 with proper content-type metadata
- Added S3 key tracking in Pdf entity for retrieval
- Configuration via environment variables: `AWS_S3_BUCKET_NAME`, `AWS_S3_PDF_PREFIX`

### 3. Java.io.File Usage for Data Storage (cr-java-0063)
**File:** `src/main/java/crm/csv/CSVTest.java`
**Issue:** Application used java.io.File API for CSV file operations.
**Fix:**
- Replaced File-based CSV reading with S3 GetObject API
- CSV files are now read directly from S3 using InputStreamReader
- Eliminated all java.io.File dependencies
- Added proper error handling and resource cleanup
- Configuration via environment variables: `AWS_S3_BUCKET_NAME`, `AWS_REGION`

### 4 & 5. Clock/Time Dependencies (cr-java-0111)
**File:** `src/main/java/crm/controller/DateTimeTestController.java`
**Issues:** 
- Line 19: Used `java.util.Date` which has timezone inconsistencies
- Line 20: Used `java.util.Date` which has timezone inconsistencies
**Fix:**
- Replaced all `java.util.Date` usage with `java.time.Instant`
- Standardized on UTC timezone using `Clock.systemUTC()`
- Added `ZonedDateTime` for timezone-aware operations
- All timestamps now use ISO-8601 format for inter-service communication
- Ensures consistent behavior across all cloud regions and containers

## Dependencies Added

Added AWS SDK for Java v2 dependencies to `pom.xml`:

```xml
<!-- AWS SDK for Java v2 - S3 Client -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.26</version>
</dependency>

<!-- AWS SDK v2 Core -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>auth</artifactId>
    <version>2.20.26</version>
</dependency>

<!-- AWS SDK v2 Regions -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>regions</artifactId>
    <version>2.20.26</version>
</dependency>
```

## Configuration Changes

Updated `application.properties` with cloud-native configuration:

```properties
# AWS S3 Configuration
aws.s3.bucket.name=${AWS_S3_BUCKET_NAME:default-crm-bucket}
aws.s3.pdf.prefix=${AWS_S3_PDF_PREFIX:pdfs/}
aws.s3.csv.prefix=${AWS_S3_CSV_PREFIX:csv/}
aws.region=${AWS_REGION:us-east-1}

# Database configuration with environment variables
spring.datasource.url=${DATABASE_URL:jdbc:mysql://localhost:3306/crm?useSSL=false}
spring.datasource.username=${DATABASE_USERNAME:root}
spring.datasource.password=${DATABASE_PASSWORD:password}
```

## Environment Variables Required for Cloud Deployment

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `AWS_S3_BUCKET_NAME` | S3 bucket name for file storage | default-crm-bucket | Yes |
| `AWS_REGION` | AWS region for S3 and other services | us-east-1 | Yes |
| `AWS_S3_PDF_PREFIX` | S3 prefix for PDF files | pdfs/ | No |
| `AWS_S3_CSV_PREFIX` | S3 prefix for CSV files | csv/ | No |
| `DATABASE_URL` | JDBC URL for database connection | jdbc:mysql://localhost:3306/crm | Yes |
| `DATABASE_USERNAME` | Database username | root | Yes |
| `DATABASE_PASSWORD` | Database password | password | Yes |

## AWS IAM Permissions Required

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
        "s3:ListBucket",
        "s3:HeadObject"
      ],
      "Resource": [
        "arn:aws:s3:::${BUCKET_NAME}/*",
        "arn:aws:s3:::${BUCKET_NAME}"
      ]
    }
  ]
}
```

## Cloud Deployment Checklist

- [ ] Create S3 bucket in target AWS region
- [ ] Configure IAM role with S3 permissions
- [ ] Set environment variables in container/ECS task definition
- [ ] Configure RDS or managed database service
- [ ] Update DATABASE_URL to point to cloud database
- [ ] Test S3 connectivity and permissions
- [ ] Verify timezone handling in distributed environment
- [ ] Monitor application logs for S3 operations

## Benefits of These Changes

1. **Durability**: Files stored in S3 with 99.999999999% durability
2. **Scalability**: No local disk space limitations
3. **Availability**: S3 provides high availability across regions
4. **Stateless**: Application containers are now stateless and can scale horizontally
5. **Cloud-Native**: Follows 12-factor app principles
6. **Timezone Consistency**: UTC standardization prevents timezone-related bugs
7. **Security**: Uses IAM roles instead of hardcoded credentials

## Testing Recommendations

1. **S3 Integration Tests**: Test file upload/download operations
2. **Timezone Tests**: Verify UTC timestamps across different regions
3. **Error Handling**: Test S3 connectivity failures and retries
4. **Performance**: Monitor S3 operation latency
5. **Security**: Verify IAM role permissions are correctly configured

## Migration Notes

- Existing local files need to be migrated to S3 before deployment
- Database schema updated to include `s3_key` column in `pdf` table
- Legacy file paths in database should be migrated to S3 keys
- Consider implementing S3 lifecycle policies for cost optimization

## Support

For issues or questions about these cloud readiness fixes, refer to:
- AWS SDK for Java v2 Documentation: https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/
- Amazon S3 Documentation: https://docs.aws.amazon.com/s3/
- Java Time API Documentation: https://docs.oracle.com/javase/8/docs/api/java/time/package-summary.html

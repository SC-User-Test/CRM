# Cloud Readiness Transformation - CRM Application

## Overview
This document describes the cloud readiness transformations applied to the CRM application to make it compatible with Azure cloud deployment.

## Transformations Applied

### 1. File System Dependencies → Azure Blob Storage

#### Issue: Hard-coded File Paths (cr-java-0061)
**File:** `crm/utils/ReadDataUtils.java`
**Problem:** Application used JFileChooser with local file system paths, creating dependencies on fixed directory structures.

**Solution:**
- Replaced local file system operations with Azure Blob Storage SDK
- Implemented cloud-native file reading from Azure Blob Storage
- Added support for listing and filtering blobs by extension
- Configuration via environment variables (`AZURE_STORAGE_CONNECTION_STRING`, `AZURE_STORAGE_CONTAINER_NAME`)

**Key Changes:**
```java
// Before: Local file system
File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");

// After: Azure Blob Storage
InputStream inputStream = readDataUtils.readFileFromBlobStorage("sample.csv");
```

#### Issue: Local File System Write Operations (cr-java-0062)
**File:** `crm/controller/PdfController.java`
**Problem:** Application wrote PDF files directly to local file system using `FileOutputStream`, causing data loss on container restarts.

**Solution:**
- Replaced `FileOutputStream` with in-memory `ByteArrayOutputStream`
- Upload generated PDFs to Azure Blob Storage
- Store blob URL instead of local file path in database
- Automatic container creation if not exists
- Proper error handling and logging

**Key Changes:**
```java
// Before: Local file write
PdfWriter.getInstance(document, new FileOutputStream(fileName));

// After: Azure Blob Storage upload
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
PdfWriter.getInstance(document, outputStream);
// ... generate PDF ...
blobClient.upload(new ByteArrayInputStream(pdfBytes), pdfBytes.length, true);
```

#### Issue: Java.io.File Usage for Data Storage (cr-java-0063)
**File:** `crm/csv/CSVTest.java`
**Problem:** Application used `java.io.File` API for CSV file operations, assuming local file system persistence.

**Solution:**
- Migrated to Azure Blob Storage for CSV file access
- Read CSV files directly from blob storage using `InputStream`
- Added blob listing functionality for discovering available CSV files
- Environment variable configuration for connection string

**Key Changes:**
```java
// Before: Local file system
File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
reader = new CSVReader(new FileReader(document));

// After: Azure Blob Storage
InputStream inputStream = blobClient.openInputStream();
reader = new CSVReader(new InputStreamReader(inputStream));
```

### 2. Clock/Time Dependencies → UTC and Azure Service Bus (cr-java-0111)

#### Issue: Local Timer and Timezone Dependencies
**File:** `crm/controller/DateTimeTestController.java`
**Problem:** Application relied on server-local timezone settings and `java.util.Date`, causing inconsistencies in distributed cloud environments.

**Solution:**
- Replaced all time operations with UTC-based `Instant` and `ZonedDateTime`
- Added configurable application timezone via environment variable
- Implemented timezone-aware conversions
- Added documentation for Azure Service Bus scheduled messages as replacement for `java.util.Timer`
- Created helper methods for calculating scheduled times in UTC

**Key Changes:**
```java
// Before: Server-local time
model.addAttribute("standardDate", new Date());
model.addAttribute("localDateTime", LocalDateTime.now());

// After: UTC-based with timezone awareness
Instant utcInstant = Instant.now();
ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
ZonedDateTime appDateTime = ZonedDateTime.now(ZoneId.of(applicationTimezone));
```

#### New Service: Azure Service Bus Scheduler
**File:** `crm/service/AzureServiceBusSchedulerService.java`
**Purpose:** Replace `java.util.Timer` with distributed, cloud-native scheduling

**Features:**
- Schedule messages for future delivery (replaces `Timer.schedule()`)
- Schedule with delay in seconds
- Cancel scheduled messages
- Message processor for handling scheduled tasks
- Recurring task support (replaces `Timer.scheduleAtFixedRate()`)
- Timezone-agnostic (all times in UTC)
- Distributed execution across multiple instances
- Reliable delivery with retry policies

**Usage Example:**
```java
// Schedule a task for future execution
OffsetDateTime scheduledTime = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1);
Long sequenceNumber = schedulerService.scheduleMessage("task-data", scheduledTime);

// Schedule with delay
schedulerService.scheduleMessageWithDelay("task-data", 3600); // 1 hour delay

// Cancel scheduled task
schedulerService.cancelScheduledMessage(sequenceNumber);
```

## Configuration Changes

### Maven Dependencies (pom.xml)
Added Azure SDK dependencies:
```xml
<!-- Azure Blob Storage -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-storage-blob</artifactId>
    <version>12.14.4</version>
</dependency>

<!-- Azure Service Bus -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-messaging-servicebus</artifactId>
    <version>7.10.0</version>
</dependency>

<!-- Azure Identity -->
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>1.6.0</version>
</dependency>
```

### Application Properties
Added cloud-ready configuration:
```properties
# Azure Blob Storage Configuration
azure.storage.connection-string=${AZURE_STORAGE_CONNECTION_STRING:}
azure.storage.container-name=${AZURE_STORAGE_CONTAINER_NAME:data-files}

# Application Timezone Configuration
app.timezone=${APP_TIMEZONE:UTC}

# Azure Service Bus Configuration
azure.servicebus.connection-string=${AZURE_SERVICEBUS_CONNECTION_STRING:}
azure.servicebus.queue-name=${AZURE_SERVICEBUS_QUEUE_NAME:scheduled-tasks}
azure.servicebus.topic-name=${AZURE_SERVICEBUS_TOPIC_NAME:scheduled-events}
```

## Environment Variables Required

### Azure Blob Storage
- `AZURE_STORAGE_CONNECTION_STRING`: Connection string for Azure Storage Account
- `AZURE_STORAGE_CONTAINER_NAME`: Container name for file storage (default: `data-files`)

### Azure Service Bus
- `AZURE_SERVICEBUS_CONNECTION_STRING`: Connection string for Azure Service Bus namespace
- `AZURE_SERVICEBUS_QUEUE_NAME`: Queue name for scheduled messages (default: `scheduled-tasks`)
- `AZURE_SERVICEBUS_TOPIC_NAME`: Topic name for scheduled events (default: `scheduled-events`)

### Application Configuration
- `APP_TIMEZONE`: Application timezone (default: `UTC`)

## Deployment Checklist

### Before Deployment
1. ✅ Create Azure Storage Account
2. ✅ Create blob containers: `data-files`, `pdf-files`, `csv-files`
3. ✅ Create Azure Service Bus namespace
4. ✅ Create Service Bus queue: `scheduled-tasks`
5. ✅ Configure connection strings as environment variables
6. ✅ Upload existing CSV/data files to Azure Blob Storage
7. ✅ Update database connection string to use Azure Database for MySQL

### After Deployment
1. ✅ Verify blob storage connectivity
2. ✅ Test PDF generation and upload
3. ✅ Test CSV file reading from blob storage
4. ✅ Verify timezone handling in distributed environment
5. ✅ Test scheduled message delivery (if using Service Bus)

## Benefits of Cloud-Ready Architecture

### Scalability
- **Stateless Design**: No local file system dependencies allow horizontal scaling
- **Distributed Storage**: Azure Blob Storage handles concurrent access from multiple instances
- **Distributed Scheduling**: Azure Service Bus enables task scheduling across instances

### Reliability
- **Data Durability**: Files stored in Azure Blob Storage with built-in redundancy
- **No Data Loss**: Container restarts don't affect stored files
- **Retry Policies**: Azure Service Bus provides automatic retry for failed message delivery

### Consistency
- **Timezone Independence**: UTC-based time handling prevents timezone issues
- **Cross-Region Support**: Works consistently across Azure regions
- **Clock Synchronization**: No dependency on server-local clock

### Maintainability
- **Configuration as Code**: All settings via environment variables
- **Centralized Storage**: Single source of truth for files
- **Monitoring**: Azure services provide built-in monitoring and diagnostics

## Migration Notes

### Data Migration
- Existing local files must be uploaded to Azure Blob Storage before deployment
- Update database records that reference local file paths to use blob URLs
- Test file access patterns with Azure Blob Storage before production deployment

### Code Migration
- All file operations now use Azure SDK instead of `java.io.File`
- Timer-based scheduling should be migrated to Azure Service Bus scheduled messages
- Time-sensitive operations should use UTC timestamps

### Testing
- Unit tests should mock Azure SDK clients
- Integration tests require Azure Storage Emulator or actual Azure resources
- Load testing should verify blob storage performance under concurrent access

## Troubleshooting

### Common Issues

**Issue**: "Azure Storage connection string is not configured"
**Solution**: Set `AZURE_STORAGE_CONNECTION_STRING` environment variable

**Issue**: "Failed to read file from Azure Blob Storage"
**Solution**: 
- Verify blob exists in the container
- Check container name configuration
- Verify connection string has read permissions

**Issue**: "Failed to upload PDF to Azure Blob Storage"
**Solution**:
- Verify connection string has write permissions
- Check container exists or service can create it
- Verify blob name doesn't contain invalid characters

**Issue**: Timezone inconsistencies
**Solution**:
- Ensure all instances use UTC for time operations
- Set `APP_TIMEZONE` environment variable consistently
- Use `ZonedDateTime` with explicit timezone for display

## References

- [Azure Blob Storage Documentation](https://docs.microsoft.com/en-us/azure/storage/blobs/)
- [Azure Service Bus Documentation](https://docs.microsoft.com/en-us/azure/service-bus-messaging/)
- [12-Factor App Principles](https://12factor.net/)
- [Spring Boot on Azure](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/)

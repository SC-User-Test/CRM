package crm.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import crm.service.S3StorageService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Cloud-native data reading utility using Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
@Component
@Slf4j
public class ReadDataUtils {

    @Autowired
    private S3StorageService s3StorageService;

    /**
     * Read file from S3 storage
     * @param s3Key S3 object key (path in S3 bucket)
     * @return InputStream of the file content
     */
    public InputStream readFileFromS3(String s3Key) {
        try {
            log.info("Reading file from S3: {}", s3Key);
            byte[] fileContent = s3StorageService.downloadFile(s3Key);
            return new ByteArrayInputStream(fileContent);
        } catch (Exception e) {
            log.error("Failed to read file from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to read file from S3: " + s3Key, e);
        }
    }

    /**
     * Read file content as byte array from S3
     * @param s3Key S3 object key (path in S3 bucket)
     * @return File content as byte array
     */
    public byte[] readFileContentFromS3(String s3Key) {
        try {
            log.info("Reading file content from S3: {}", s3Key);
            return s3StorageService.downloadFile(s3Key);
        } catch (Exception e) {
            log.error("Failed to read file content from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to read file content from S3: " + s3Key, e);
        }
    }

    /**
     * Check if file exists in S3
     * @param s3Key S3 object key (path in S3 bucket)
     * @return true if file exists, false otherwise
     */
    public boolean fileExistsInS3(String s3Key) {
        return s3StorageService.fileExists(s3Key);
    }
}

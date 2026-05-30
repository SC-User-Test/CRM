package crm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * AWS S3 Storage Service for cloud-native file operations.
 * Replaces local file system dependencies with durable S3 storage.
 */
@Service
@Slf4j
public class S3StorageService {

    @Value("${aws.s3.bucket.name:crm-application-storage}")
    private String bucketName;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        try {
            this.s3Client = S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
            log.info("S3 Storage Service initialized with bucket: {} in region: {}", bucketName, awsRegion);
        } catch (Exception e) {
            log.error("Failed to initialize S3 client: {}", e.getMessage());
            throw new RuntimeException("S3 Storage Service initialization failed", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        if (s3Client != null) {
            s3Client.close();
            log.info("S3 client closed");
        }
    }

    /**
     * Upload file content to S3
     * @param key S3 object key (file path)
     * @param content File content as byte array
     * @param contentType MIME type of the content
     * @return S3 object URL
     */
    public String uploadFile(String key, byte[] content, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
            log.info("Successfully uploaded file to S3: {}", key);
            
            return String.format("s3://%s/%s", bucketName, key);
        } catch (S3Exception e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new RuntimeException("S3 upload failed for key: " + key, e);
        }
    }

    /**
     * Download file content from S3
     * @param key S3 object key (file path)
     * @return File content as byte array
     */
    public byte[] downloadFile(String key) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            InputStream inputStream = s3Client.getObject(getObjectRequest);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            log.info("Successfully downloaded file from S3: {}", key);
            return outputStream.toByteArray();
        } catch (S3Exception e) {
            log.error("Failed to download file from S3: {}", e.getMessage());
            throw new RuntimeException("S3 download failed for key: " + key, e);
        } catch (Exception e) {
            log.error("Error reading S3 object: {}", e.getMessage());
            throw new RuntimeException("Failed to read S3 object: " + key, e);
        }
    }

    /**
     * Check if file exists in S3
     * @param key S3 object key (file path)
     * @return true if file exists, false otherwise
     */
    public boolean fileExists(String key) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headObjectRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            log.error("Error checking file existence in S3: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Delete file from S3
     * @param key S3 object key (file path)
     */
    public void deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("Successfully deleted file from S3: {}", key);
        } catch (S3Exception e) {
            log.error("Failed to delete file from S3: {}", e.getMessage());
            throw new RuntimeException("S3 delete failed for key: " + key, e);
        }
    }

    /**
     * Get pre-signed URL for temporary access to S3 object
     * @param key S3 object key (file path)
     * @return Public URL to access the object
     */
    public String getFileUrl(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, awsRegion, key);
    }
}

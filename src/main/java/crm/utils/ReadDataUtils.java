package crm.utils;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Cloud-ready utility for reading data from Google Cloud Storage.
 * Replaces local file system dependencies with GCS operations.
 */
@Component
public class ReadDataUtils {

    @Value("${gcs.bucket.name:default-bucket}")
    private String bucketName;

    private final Storage storage;

    public ReadDataUtils() {
        // Initialize GCS client
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    /**
     * Read file from Google Cloud Storage
     * @param blobName The name/path of the blob in GCS
     * @return InputStream of the file content
     */
    public InputStream readFileFromGCS(String blobName) {
        try {
            Blob blob = storage.get(bucketName, blobName);
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            byte[] content = blob.getContent();
            return new ByteArrayInputStream(content);
        } catch (Exception e) {
            throw new RuntimeException("Error reading file from GCS: " + blobName, e);
        }
    }

    /**
     * Download file from GCS to temporary location for processing
     * @param blobName The name/path of the blob in GCS
     * @return Path to temporary file
     */
    public Path downloadFileFromGCS(String blobName) {
        try {
            Blob blob = storage.get(bucketName, blobName);
            if (blob == null) {
                throw new RuntimeException("File not found in GCS: " + blobName);
            }
            
            // Create temporary file
            Path tempFile = Files.createTempFile("gcs-download-", getFileExtension(blobName));
            
            // Download content to temp file
            byte[] content = blob.getContent();
            Files.write(tempFile, content);
            
            return tempFile;
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file from GCS: " + blobName, e);
        }
    }

    /**
     * List files in GCS bucket with specific extension
     * @param fileExtension The file extension to filter (e.g., "csv", "pdf")
     * @return Iterable of blob names
     */
    public Iterable<Blob> listFilesInGCS(String fileExtension) {
        try {
            return storage.list(bucketName).iterateAll();
        } catch (Exception e) {
            throw new RuntimeException("Error listing files in GCS bucket: " + bucketName, e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "";
    }
}

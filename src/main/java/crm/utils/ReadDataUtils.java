package crm.utils;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading data from Azure Blob Storage.
 * Replaces local file system dependencies with Azure Blob Storage.
 */
@Component
public class ReadDataUtils {

    @Value("${azure.storage.connection-string:#{null}}")
    private String connectionString;

    @Value("${azure.storage.container-name:data-files}")
    private String containerName;

    /**
     * Reads a file from Azure Blob Storage by blob name.
     * 
     * @param blobName The name of the blob to read
     * @return InputStream of the blob content
     * @throws RuntimeException if blob cannot be read
     */
    public InputStream readFileFromBlobStorage(String blobName) {
        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set azure.storage.connection-string in application.properties");
        }

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            return blobClient.openInputStream();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file from Azure Blob Storage: " + blobName, e);
        }
    }

    /**
     * Reads a file from Azure Blob Storage and returns as byte array.
     * 
     * @param blobName The name of the blob to read
     * @return byte array of the blob content
     */
    public byte[] readFileAsByteArray(String blobName) {
        try (InputStream inputStream = readFileFromBlobStorage(blobName);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file as byte array from Azure Blob Storage: " + blobName, e);
        }
    }

    /**
     * Lists all blobs in the configured container with a specific extension.
     * 
     * @param fileExtension The file extension to filter (e.g., "csv", "pdf")
     * @return Iterable of blob names matching the extension
     */
    public Iterable<String> listBlobsByExtension(String fileExtension) {
        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured.");
        }

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            return containerClient.listBlobs()
                    .stream()
                    .map(blobItem -> blobItem.getName())
                    .filter(name -> name.endsWith("." + fileExtension))
                    .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to list blobs from Azure Blob Storage", e);
        }
    }
}

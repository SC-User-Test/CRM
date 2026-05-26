package crm.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Azure Storage Configuration for cloud-native file storage.
 * Configures Azure Blob Storage clients for the application.
 */
@Configuration
@Slf4j
public class AzureStorageConfig {

    @Value("${azure.storage.connection-string:#{null}}")
    private String connectionString;

    @Value("${azure.storage.container-name:crm-files}")
    private String containerName;

    /**
     * Creates a BlobServiceClient bean for Azure Blob Storage operations.
     * 
     * @return BlobServiceClient instance or null if not configured
     */
    @Bean
    public BlobServiceClient blobServiceClient() {
        if (connectionString == null || connectionString.isEmpty()) {
            log.warn("Azure Storage connection string is not configured. " +
                    "File storage operations will not be available. " +
                    "Please set AZURE_STORAGE_CONNECTION_STRING environment variable.");
            return null;
        }

        try {
            BlobServiceClient client = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            
            log.info("Azure Blob Storage client initialized successfully");
            return client;
        } catch (Exception e) {
            log.error("Failed to initialize Azure Blob Storage client", e);
            return null;
        }
    }

    /**
     * Creates a BlobContainerClient bean for the configured container.
     * 
     * @param blobServiceClient The BlobServiceClient instance
     * @return BlobContainerClient instance or null if not configured
     */
    @Bean
    public BlobContainerClient blobContainerClient(BlobServiceClient blobServiceClient) {
        if (blobServiceClient == null) {
            log.warn("BlobServiceClient is not available. Container client will not be created.");
            return null;
        }

        try {
            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
                log.info("Created Azure Blob Storage container: {}", containerName);
            } else {
                log.info("Using existing Azure Blob Storage container: {}", containerName);
            }
            
            return containerClient;
        } catch (Exception e) {
            log.error("Failed to initialize Azure Blob Storage container: {}", containerName, e);
            return null;
        }
    }
}

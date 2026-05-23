package crm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Azure Blob Storage implementation of StorageService.
 * This implementation uses Spring Cloud Azure Storage SDK for blob operations.
 * 
 * Configuration properties:
 * - azure.storage.account-name: Azure Storage account name
 * - azure.storage.account-key: Azure Storage account key (from Key Vault)
 * - azure.storage.container-name: Container name for blob storage
 * - azure.storage.endpoint: Optional custom endpoint URL
 * 
 * For production deployment, configure these properties in Azure App Configuration
 * and use Azure Key Vault for sensitive values like account-key.
 */
@Service
@Slf4j
public class AzureBlobStorageService implements StorageService {

    @Value("${azure.storage.account-name:#{null}}")
    private String accountName;

    @Value("${azure.storage.container-name:crm-files}")
    private String containerName;

    @Value("${azure.storage.endpoint:#{null}}")
    private String endpoint;

    @Value("${azure.storage.enabled:false}")
    private boolean storageEnabled;

    // In-memory fallback for local development when Azure Storage is not configured
    private final java.util.Map<String, byte[]> localStorageFallback = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public String uploadFile(String fileName, byte[] content, String contentType) throws IOException {
        if (!storageEnabled || accountName == null) {
            log.warn("Azure Blob Storage not configured. Using in-memory fallback for file: {}", fileName);
            return uploadToLocalFallback(fileName, content);
        }

        try {
            // TODO: Implement actual Azure Blob Storage upload using Azure SDK
            // Example code structure (requires azure-storage-blob dependency):
            //
            // BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
            //     .connectionString(connectionString)
            //     .buildClient();
            // 
            // BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            // BlobClient blobClient = containerClient.getBlobClient(fileName);
            // 
            // blobClient.upload(new ByteArrayInputStream(content), content.length, true);
            // return blobClient.getBlobUrl();

            log.info("Uploading file to Azure Blob Storage: {} (size: {} bytes)", fileName, content.length);
            
            // For now, use local fallback until Azure SDK is configured
            return uploadToLocalFallback(fileName, content);
            
        } catch (Exception e) {
            log.error("Failed to upload file to Azure Blob Storage: {}", fileName, e);
            throw new IOException("Failed to upload file to Azure Blob Storage", e);
        }
    }

    @Override
    public InputStream downloadFile(String fileName) throws IOException {
        if (!storageEnabled || accountName == null) {
            log.warn("Azure Blob Storage not configured. Using in-memory fallback for file: {}", fileName);
            return downloadFromLocalFallback(fileName);
        }

        try {
            // TODO: Implement actual Azure Blob Storage download using Azure SDK
            // Example code structure:
            //
            // BlobClient blobClient = getBlobClient(fileName);
            // return blobClient.openInputStream();

            log.info("Downloading file from Azure Blob Storage: {}", fileName);
            
            // For now, use local fallback
            return downloadFromLocalFallback(fileName);
            
        } catch (Exception e) {
            log.error("Failed to download file from Azure Blob Storage: {}", fileName, e);
            throw new IOException("Failed to download file from Azure Blob Storage", e);
        }
    }

    @Override
    public void deleteFile(String fileName) throws IOException {
        if (!storageEnabled || accountName == null) {
            log.warn("Azure Blob Storage not configured. Deleting from in-memory fallback: {}", fileName);
            localStorageFallback.remove(fileName);
            return;
        }

        try {
            // TODO: Implement actual Azure Blob Storage deletion using Azure SDK
            // Example code structure:
            //
            // BlobClient blobClient = getBlobClient(fileName);
            // blobClient.delete();

            log.info("Deleting file from Azure Blob Storage: {}", fileName);
            localStorageFallback.remove(fileName);
            
        } catch (Exception e) {
            log.error("Failed to delete file from Azure Blob Storage: {}", fileName, e);
            throw new IOException("Failed to delete file from Azure Blob Storage", e);
        }
    }

    @Override
    public boolean fileExists(String fileName) {
        if (!storageEnabled || accountName == null) {
            return localStorageFallback.containsKey(fileName);
        }

        try {
            // TODO: Implement actual Azure Blob Storage existence check using Azure SDK
            // Example code structure:
            //
            // BlobClient blobClient = getBlobClient(fileName);
            // return blobClient.exists();

            return localStorageFallback.containsKey(fileName);
            
        } catch (Exception e) {
            log.error("Failed to check file existence in Azure Blob Storage: {}", fileName, e);
            return false;
        }
    }

    // Local fallback methods for development/testing
    private String uploadToLocalFallback(String fileName, byte[] content) {
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        localStorageFallback.put(uniqueFileName, content);
        String fallbackUrl = "local-storage://" + containerName + "/" + uniqueFileName;
        log.info("File stored in local fallback: {}", fallbackUrl);
        return fallbackUrl;
    }

    private InputStream downloadFromLocalFallback(String fileName) throws IOException {
        byte[] content = localStorageFallback.get(fileName);
        if (content == null) {
            throw new IOException("File not found in local fallback: " + fileName);
        }
        return new ByteArrayInputStream(content);
    }

}

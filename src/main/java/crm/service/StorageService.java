package crm.service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud storage service interface for Azure Blob Storage operations.
 * Provides abstraction for file upload, download, and deletion operations.
 */
public interface StorageService {

    /**
     * Uploads a file to Azure Blob Storage.
     * 
     * @param fileName the name of the file
     * @param content the file content as byte array
     * @param contentType the MIME type of the file
     * @return the URL of the uploaded blob
     * @throws IOException if upload fails
     */
    String uploadFile(String fileName, byte[] content, String contentType) throws IOException;

    /**
     * Downloads a file from Azure Blob Storage.
     * 
     * @param fileName the name of the file to download
     * @return InputStream of the file content
     * @throws IOException if download fails
     */
    InputStream downloadFile(String fileName) throws IOException;

    /**
     * Deletes a file from Azure Blob Storage.
     * 
     * @param fileName the name of the file to delete
     * @throws IOException if deletion fails
     */
    void deleteFile(String fileName) throws IOException;

    /**
     * Checks if a file exists in Azure Blob Storage.
     * 
     * @param fileName the name of the file
     * @return true if file exists, false otherwise
     */
    boolean fileExists(String fileName);

}

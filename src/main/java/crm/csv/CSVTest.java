package crm.csv;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV Test utility that reads CSV files from Azure Blob Storage.
 * Replaces local file system dependencies with Azure Blob Storage.
 */
public class CSVTest {

    private static final String CONNECTION_STRING = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
    private static final String CONTAINER_NAME = System.getenv("AZURE_STORAGE_CONTAINER_NAME") != null 
            ? System.getenv("AZURE_STORAGE_CONTAINER_NAME") 
            : "crm-files";

    /**
     * Reads a CSV file from Azure Blob Storage and processes it.
     * 
     * @param blobName The name of the CSV blob to read
     */
    public static void readCsvFromAzureBlob(String blobName) {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set AZURE_STORAGE_CONNECTION_STRING environment variable");
        }

        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Connect to Azure Blob Storage
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            if (!blobClient.exists()) {
                throw new IllegalArgumentException("CSV blob does not exist: " + blobName);
            }

            // Read CSV from blob storage
            InputStream inputStream = blobClient.openInputStream();
            reader = new CSVReader(new InputStreamReader(inputStream));
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully processed " + data.size() + " rows from Azure Blob Storage");
            
        } catch (IOException e) {
            System.err.println("Error reading CSV from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing CSV reader: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Lists all CSV files in the Azure Blob Storage container.
     */
    public static void listCsvFilesInAzureBlob() {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set AZURE_STORAGE_CONNECTION_STRING environment variable");
        }

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(CONNECTION_STRING)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
        
        System.out.println("CSV files in Azure Blob Storage container '" + CONTAINER_NAME + "':");
        containerClient.listBlobs().forEach(blobItem -> {
            if (blobItem.getName().endsWith(".csv")) {
                System.out.println("  - " + blobItem.getName());
            }
        });
    }

    public static void main(String[] args) {
        // Example usage: Read a specific CSV file from Azure Blob Storage
        // Replace "sample.csv" with the actual blob name
        if (args.length > 0) {
            String blobName = args[0];
            System.out.println("Reading CSV file from Azure Blob Storage: " + blobName);
            readCsvFromAzureBlob(blobName);
        } else {
            System.out.println("Usage: java crm.csv.CSVTest <blob-name>");
            System.out.println("Example: java crm.csv.CSVTest sample.csv");
            System.out.println("\nListing available CSV files:");
            listCsvFilesInAzureBlob();
        }
    }
}

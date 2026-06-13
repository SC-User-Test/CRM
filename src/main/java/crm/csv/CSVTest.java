package crm.csv;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
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
            : "csv-files";

    /**
     * Reads a CSV file from Azure Blob Storage.
     * 
     * @param blobName The name of the CSV blob to read
     * @return List of CSV rows as Object arrays
     */
    public static List<Object[]> readCsvFromBlobStorage(String blobName) {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set AZURE_STORAGE_CONNECTION_STRING environment variable");
        }

        List<Object[]> data = new ArrayList<>();
        CSVReader reader = null;

        try {
            // Connect to Azure Blob Storage
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            // Read CSV from blob storage
            InputStream inputStream = blobClient.openInputStream();
            reader = new CSVReader(new InputStreamReader(inputStream));

            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                // Example processing: filter specific records
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }

            System.out.println("Successfully read " + data.size() + " rows from Azure Blob Storage: " + blobName);

        } catch (IOException e) {
            System.err.println("Error reading CSV from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    /**
     * Lists all CSV files in the configured Azure Blob Storage container.
     * 
     * @return List of CSV blob names
     */
    public static List<String> listCsvFiles() {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured.");
        }

        List<String> csvFiles = new ArrayList<>();

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);

            containerClient.listBlobs().forEach(blobItem -> {
                if (blobItem.getName().endsWith(".csv")) {
                    csvFiles.add(blobItem.getName());
                }
            });

            System.out.println("Found " + csvFiles.size() + " CSV files in Azure Blob Storage");

        } catch (Exception e) {
            System.err.println("Error listing CSV files from Azure Blob Storage: " + e.getMessage());
            e.printStackTrace();
        }

        return csvFiles;
    }

    public static void main(String[] args) {
        // Example usage: Read a specific CSV file from Azure Blob Storage
        // Replace "sample.csv" with the actual blob name
        String blobName = args.length > 0 ? args[0] : "sample.csv";
        
        System.out.println("Reading CSV file from Azure Blob Storage: " + blobName);
        List<Object[]> data = readCsvFromBlobStorage(blobName);
        
        System.out.println("Total rows read: " + data.size());
        
        // Optionally list all available CSV files
        System.out.println("\nAvailable CSV files in Azure Blob Storage:");
        List<String> csvFiles = listCsvFiles();
        csvFiles.forEach(System.out::println);
    }

}

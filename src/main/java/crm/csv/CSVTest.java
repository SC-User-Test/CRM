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
 * Cloud-ready CSV Test utility that reads CSV files from Azure Blob Storage
 * instead of local file system.
 */
public class CSVTest {

    // Configuration - should be externalized to application.properties in production
    private static final String CONNECTION_STRING = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
    private static final String CONTAINER_NAME = System.getenv("AZURE_STORAGE_CONTAINER_NAME") != null 
            ? System.getenv("AZURE_STORAGE_CONTAINER_NAME") 
            : "csv-files";

    /**
     * Reads a CSV file from Azure Blob Storage.
     * 
     * @param blobName The name of the CSV blob to read
     * @return InputStream of the blob content
     */
    private static InputStream readCsvFromBlobStorage(String blobName) {
        if (CONNECTION_STRING == null || CONNECTION_STRING.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set AZURE_STORAGE_CONNECTION_STRING environment variable");
        }

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(CONNECTION_STRING)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(CONTAINER_NAME);
            BlobClient blobClient = containerClient.getBlobClient(blobName);

            return blobClient.openInputStream();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read CSV file from Azure Blob Storage: " + blobName, e);
        }
    }

    /**
     * Main method demonstrating CSV reading from Azure Blob Storage.
     * 
     * @param args Command line arguments - expects blob name as first argument
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: CSVTest <blob-name>");
            System.err.println("Example: CSVTest data.csv");
            System.err.println("Note: Set AZURE_STORAGE_CONNECTION_STRING environment variable");
            return;
        }

        String blobName = args[0];
        if (!blobName.endsWith(".csv")) {
            blobName += ".csv";
        }

        System.out.println("Reading CSV file from Azure Blob Storage: " + blobName);

        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try (InputStream inputStream = readCsvFromBlobStorage(blobName)) {
            reader = new CSVReader(new InputStreamReader(inputStream));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                
                // Process specific records
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully processed " + data.size() + " rows from Azure Blob Storage");
            
        } catch (IllegalStateException e) {
            System.err.println("Configuration error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
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

}

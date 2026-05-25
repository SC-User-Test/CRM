package crm.csv;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV processing using Google Cloud Storage.
 * Replaces java.io.File operations with GCS operations.
 */
public class CSVTest {

    @Value("${gcs.bucket.name:default-bucket}")
    private static String bucketName = System.getenv().getOrDefault("GCS_BUCKET_NAME", "default-bucket");

    public static void main(String[] args) {
        // Initialize GCS client
        Storage storage = StorageOptions.getDefaultInstance().getService();
        
        // Specify the CSV file path in GCS (e.g., "csv-files/data.csv")
        String blobName = System.getenv().getOrDefault("CSV_FILE_PATH", "csv-files/sample.csv");
        
        try {
            // Read CSV file from Google Cloud Storage
            Blob blob = storage.get(bucketName, blobName);
            if (blob == null) {
                System.err.println("CSV file not found in GCS: " + blobName);
                return;
            }
            
            byte[] content = blob.getContent();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
            
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            List<Object[]> data = new ArrayList<>();
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            reader.close();
            System.out.println("Successfully processed CSV from GCS: " + blobName);
            
        } catch (IOException e) {
            System.err.println("Error reading CSV from GCS: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

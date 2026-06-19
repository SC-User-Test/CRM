package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-native CSV processing utility that reads from Amazon S3.
 * Replaces local file system dependencies with S3 object storage.
 */
@Component
public class CSVTest {

    private final S3Client s3Client;
    
    @Value("${aws.s3.bucket.name:default-bucket}")
    private String bucketName;

    public CSVTest(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Read and process CSV file from S3.
     * 
     * @param s3Key The S3 object key for the CSV file
     * @return List of parsed CSV rows
     */
    public List<Object[]> readCsvFromS3(String s3Key) {
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Get CSV file from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            
            InputStream s3InputStream = s3Client.getObject(getObjectRequest);
            
            // Read CSV using OpenCSV
            try (CSVReader reader = new CSVReader(new InputStreamReader(s3InputStream))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    data.add(line);
                    
                    // Example processing logic
                    if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                        System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading CSV from S3: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }

    /**
     * Read and process CSV file from S3 with custom bucket.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key for the CSV file
     * @return List of parsed CSV rows
     */
    public List<Object[]> readCsvFromS3(String bucketName, String s3Key) {
        List<Object[]> data = new ArrayList<>();
        
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();
            
            InputStream s3InputStream = s3Client.getObject(getObjectRequest);
            
            try (CSVReader reader = new CSVReader(new InputStreamReader(s3InputStream))) {
                String[] line;
                while ((line = reader.readNext()) != null) {
                    data.add(line);
                    
                    if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                        System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading CSV from S3: " + e.getMessage());
            e.printStackTrace();
        }
        
        return data;
    }

    /**
     * Example usage method - demonstrates how to use the S3-based CSV reader.
     * In production, this would be called from a service or controller with the S3 key.
     */
    public void processExample(String s3Key) {
        List<Object[]> data = readCsvFromS3(s3Key);
        System.out.println("Processed " + data.size() + " rows from S3 CSV file: " + s3Key);
    }
}

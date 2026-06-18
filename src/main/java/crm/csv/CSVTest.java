package crm.csv;

import com.opencsv.CSVReader;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-native CSV processing using Amazon S3 for data storage.
 * Replaces local file system dependencies with S3 object storage.
 */
public class CSVTest {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-data-bucket");
    private static final String S3_CSV_KEY = System.getenv().getOrDefault("S3_CSV_KEY", "data/input.csv");

    public static void main(String[] args) {
        // Initialize S3 client
        S3Client s3Client = S3Client.builder().build();
        
        try {
            processCSVFromS3(s3Client, S3_CSV_KEY);
        } catch (IOException e) {
            System.err.println("Error processing CSV from S3: " + e.getMessage());
            e.printStackTrace();
        } finally {
            s3Client.close();
        }
    }

    /**
     * Reads and processes CSV file from Amazon S3.
     * 
     * @param s3Client The S3 client instance
     * @param s3Key The S3 object key for the CSV file
     * @throws IOException if the file cannot be read
     */
    public static void processCSVFromS3(S3Client s3Client, String s3Key) throws IOException {
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            // Download CSV from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            reader = new CSVReader(new InputStreamReader(s3Object));
            
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully processed " + data.size() + " rows from S3: " + s3Key);
            
        } catch (S3Exception e) {
            throw new IOException("Failed to read CSV from S3: " + s3Key, e);
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

package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
 * Cloud-ready CSV processor that reads CSV files from Amazon S3 instead of local file system.
 * This eliminates java.io.File dependencies and makes the application cloud-native.
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
     * Processes a CSV file from Amazon S3.
     * 
     * @param s3Key The S3 object key (path within the bucket) of the CSV file
     * @return List of parsed CSV rows
     * @throws S3Exception if the file cannot be retrieved from S3
     * @throws IOException if CSV parsing fails
     */
    public List<Object[]> processCSVFromS3(String s3Key) throws S3Exception, IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Failed to close CSV reader: " + e.getMessage());
                }
            }
            try {
                s3Object.close();
            } catch (IOException e) {
                System.err.println("Failed to close S3 input stream: " + e.getMessage());
            }
        }
        
        return data;
    }

    /**
     * Processes a CSV file from Amazon S3 with custom bucket name.
     * 
     * @param bucketName The S3 bucket name
     * @param s3Key The S3 object key (path within the bucket) of the CSV file
     * @return List of parsed CSV rows
     * @throws S3Exception if the file cannot be retrieved from S3
     * @throws IOException if CSV parsing fails
     */
    public List<Object[]> processCSVFromS3(String bucketName, String s3Key) throws S3Exception, IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
        
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Failed to close CSV reader: " + e.getMessage());
                }
            }
            try {
                s3Object.close();
            } catch (IOException e) {
                System.err.println("Failed to close S3 input stream: " + e.getMessage());
            }
        }
        
        return data;
    }

}

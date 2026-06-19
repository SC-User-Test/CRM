package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-native CSV processor that reads CSV files from Amazon S3
 * instead of local file system for cloud compatibility.
 */
@Component
public class CSVTest {

    @Value("${aws.s3.bucket.name:default-bucket}")
    private String bucketName;

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    private S3Client s3Client;

    /**
     * Initialize S3 client with default credentials provider.
     * Uses IAM roles in cloud environments (ECS, EKS, Lambda).
     */
    private S3Client getS3Client() {
        if (s3Client == null) {
            s3Client = S3Client.builder()
                    .region(Region.of(awsRegion))
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
        }
        return s3Client;
    }

    /**
     * Process CSV file from S3 instead of local file system.
     * This eliminates java.io.File dependencies and makes the application cloud-ready.
     *
     * @param s3Key The S3 object key (path to CSV file in bucket)
     * @return List of parsed CSV rows
     */
    public List<Object[]> processCsvFromS3(String s3Key) {
        List<Object[]> data = new ArrayList<>();
        CSVReader reader = null;

        try {
            // Read CSV file from S3
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = getS3Client().getObject(getObjectRequest);
            
            // Create CSV reader from S3 input stream
            reader = new CSVReader(new InputStreamReader(s3Object));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                
                // Example processing logic preserved from original
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(line[0] + "\t" + line[1] + "\t" + (line.length > 2 ? line[2] : ""));
                }
            }
            
            System.out.println("Successfully processed " + data.size() + " rows from S3: " + s3Key);
            
        } catch (IOException e) {
            System.err.println("Error processing CSV from S3: " + s3Key);
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
     * Example main method for testing - in production, this would be called by a service.
     * Configuration values should be provided via environment variables or application.properties.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: CSVTest <s3-key>");
            System.err.println("Example: CSVTest uploads/data.csv");
            System.err.println("Note: Configure AWS credentials and bucket name via environment variables:");
            System.err.println("  AWS_REGION, AWS_S3_BUCKET_NAME");
            return;
        }

        CSVTest csvTest = new CSVTest();
        csvTest.bucketName = System.getenv().getOrDefault("AWS_S3_BUCKET_NAME", "default-bucket");
        csvTest.awsRegion = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
        
        String s3Key = args[0];
        List<Object[]> data = csvTest.processCsvFromS3(s3Key);
        
        System.out.println("Total rows processed: " + data.size());
    }

    /**
     * Clean up S3 client resources.
     */
    public void close() {
        if (s3Client != null) {
            s3Client.close();
        }
    }
}

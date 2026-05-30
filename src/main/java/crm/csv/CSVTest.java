package crm.csv;

import com.opencsv.CSVReader;
import crm.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV Test utility with cloud-native S3 storage.
 * Replaces java.io.File operations with Amazon S3 for durable storage.
 */
@Component
@Slf4j
public class CSVTest {

    @Autowired
    private S3StorageService s3StorageService;

    /**
     * Read and process CSV file from S3 storage
     * @param s3Key S3 object key (path in S3 bucket) for the CSV file
     * @return List of CSV rows as Object arrays
     */
    public List<Object[]> readCsvFromS3(String s3Key) {
        CSVReader reader = null;
        List<Object[]> data = new ArrayList<>();
        
        try {
            log.info("Reading CSV file from S3: {}", s3Key);
            
            // Download CSV content from S3
            byte[] csvContent = s3StorageService.downloadFile(s3Key);
            
            // Create CSV reader from S3 content
            reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(csvContent)));
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                data.add(line);
                
                // Example processing logic
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    log.info("Found matching record: {} {} {}", 
                            line.length > 0 ? line[0] : "", 
                            line.length > 1 ? line[1] : "", 
                            line.length > 2 ? line[2] : "");
                }
            }
            
            log.info("Successfully processed {} rows from CSV file in S3", data.size());
            
        } catch (IOException e) {
            log.error("Failed to read CSV file from S3: {}", e.getMessage());
            throw new RuntimeException("Failed to process CSV file from S3: " + s3Key, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.warn("Failed to close CSV reader: {}", e.getMessage());
                }
            }
        }
        
        return data;
    }

    /**
     * Upload CSV data to S3 storage
     * @param s3Key S3 object key (path in S3 bucket)
     * @param csvContent CSV content as byte array
     * @return S3 URL of the uploaded file
     */
    public String uploadCsvToS3(String s3Key, byte[] csvContent) {
        try {
            log.info("Uploading CSV file to S3: {}", s3Key);
            return s3StorageService.uploadFile(s3Key, csvContent, "text/csv");
        } catch (Exception e) {
            log.error("Failed to upload CSV file to S3: {}", e.getMessage());
            throw new RuntimeException("Failed to upload CSV file to S3: " + s3Key, e);
        }
    }

    /**
     * Example main method for testing (should be replaced with proper unit tests)
     * This demonstrates how to use the S3-based CSV processing
     */
    public static void main(String[] args) {
        log.info("CSVTest now uses S3 storage. Configure S3 bucket and use readCsvFromS3() method.");
        log.info("Example: csvTest.readCsvFromS3(\"csv-files/sample.csv\")");
    }
}

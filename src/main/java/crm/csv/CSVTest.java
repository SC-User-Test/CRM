package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV Test utility
 * - Removed Desktop UI (JFileChooser) dependency
 * - Uses classpath resources or environment variable configuration
 * - Proper resource management with try-with-resources
 * - Structured logging instead of System.out.println
 */
@Slf4j
public class CSVTest {

    public static void main(String[] args) {
        // For cloud deployment, CSV file should be:
        // 1. Placed in src/main/resources directory
        // 2. Path specified via environment variable
        // 3. Uploaded to cloud storage (S3) and accessed via API

        String csvPath = System.getenv("CSV_FILE_PATH");
        if (csvPath == null || csvPath.isEmpty()) {
            csvPath = "data/sample.csv"; // Default classpath location
        }

        log.info("Processing CSV file from: {}", csvPath);

        List<Object[]> data = new ArrayList<>();

        // Use try-with-resources for proper resource management
        try (InputStream inputStream = ReadDataUtils.readFileFromClasspath(csvPath);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             CSVReader reader = new CSVReader(inputStreamReader)) {

            String[] line;
            int lineCount = 0;

            while ((line = reader.readNext()) != null) {
                data.add(line);
                lineCount++;

                // Log specific records with structured logging
                if (line.length > 1 && line[1].equals("QUICK SUB")) {
                    log.info("Found QUICK SUB record - Line: {}, Column[0]: {}, Column[1]: {}, Column[2]: {}",
                            lineCount, line[0], line[1], line.length > 2 ? line[2] : "N/A");
                }
            }

            log.info("CSV processing completed. Total lines processed: {}", lineCount);

        } catch (IOException e) {
            log.error("Error reading CSV file: {}, Error: {}", csvPath, e.getMessage(), e);
            throw new RuntimeException("Failed to process CSV file: " + csvPath, e);
        } catch (Exception e) {
            log.error("Unexpected error processing CSV file: {}, Error: {}", csvPath, e.getMessage(), e);
            throw new RuntimeException("Unexpected error processing CSV: " + csvPath, e);
        }
    }

}

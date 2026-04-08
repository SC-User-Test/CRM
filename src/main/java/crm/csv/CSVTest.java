package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV processing utility.
 * Removes static initializers with I/O operations and replaces java.io.File usage
 * with cloud-compatible resource loading patterns.
 * 
 * Compatible with AWS, Azure, and GCP cloud environments.
 */
@Component
public class CSVTest {

    /**
     * CSV storage directory from environment variable.
     * For cloud deployments, this should be a mounted persistent volume or classpath resource.
     * Set via environment variable: CSV_STORAGE_PATH
     */
    @Value("${csv.storage.path:#{systemEnvironment['CSV_STORAGE_PATH'] ?: '/tmp/csv'}}")
    private String csvStoragePath;

    /**
     * Reads CSV file from classpath resources (cloud-compatible).
     * Files should be placed in src/main/resources directory.
     * 
     * @param resourcePath Path relative to classpath (e.g., "data/file.csv")
     * @return List of CSV rows as Object arrays
     * @throws IOException if file cannot be read
     */
    public List<Object[]> readCsvFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("CSV resource not found: " + resourcePath);
        }

        List<Object[]> data = new ArrayList<>();
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream);
             CSVReader csvReader = new CSVReader(reader)) {
            
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                data.add(line);
            }
        }
        return data;
    }

    /**
     * Reads CSV file from persistent storage path (cloud-compatible).
     * Uses environment-configured storage path for cloud deployments.
     * 
     * @param fileName Name of the CSV file
     * @return List of CSV rows as Object arrays
     * @throws IOException if file cannot be read
     */
    public List<Object[]> readCsvFromStorage(String fileName) throws IOException {
        Path filePath = Paths.get(csvStoragePath, fileName);
        
        if (!Files.exists(filePath)) {
            throw new IOException("CSV file not found: " + filePath);
        }

        List<Object[]> data = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(Files.newBufferedReader(filePath))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                data.add(line);
            }
        }
        return data;
    }

    /**
     * Filters CSV data by column value.
     * 
     * @param data CSV data as list of Object arrays
     * @param columnIndex Column index to filter on
     * @param filterValue Value to filter by
     * @return Filtered list of CSV rows
     */
    public List<Object[]> filterByColumn(List<Object[]> data, int columnIndex, String filterValue) {
        List<Object[]> filtered = new ArrayList<>();
        for (Object[] row : data) {
            if (row.length > columnIndex) {
                String[] stringRow = (String[]) row;
                if (stringRow[columnIndex].equals(filterValue)) {
                    filtered.add(row);
                }
            }
        }
        return filtered;
    }

    /**
     * Processes CSV data and prints matching rows.
     * This method replaces the static main method with I/O operations.
     * 
     * @param csvPath Path to CSV file (classpath or storage)
     * @param useClasspath If true, reads from classpath; otherwise from storage
     * @param filterColumn Column index to filter on
     * @param filterValue Value to filter by
     */
    public void processCsvData(String csvPath, boolean useClasspath, int filterColumn, String filterValue) {
        try {
            List<Object[]> data;
            if (useClasspath) {
                data = readCsvFromClasspath(csvPath);
            } else {
                data = readCsvFromStorage(csvPath);
            }

            // Filter and process data
            List<Object[]> filtered = filterByColumn(data, filterColumn, filterValue);
            
            for (Object[] row : filtered) {
                String[] stringRow = (String[]) row;
                if (stringRow.length >= 3) {
                    System.out.println(stringRow[0] + "\t" + stringRow[1] + "\t" + stringRow[2]);
                }
            }
            
            System.out.println("Processed " + data.size() + " rows, found " + filtered.size() + " matches");
            
        } catch (IOException e) {
            System.err.println("Error processing CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example usage method (replaces static main for cloud compatibility).
     * Can be invoked via Spring context or REST endpoint.
     */
    public void exampleUsage() {
        // Example: Read from classpath
        processCsvData("data/sample.csv", true, 1, "QUICK SUB");
        
        // Example: Read from persistent storage
        // processCsvData("sample.csv", false, 1, "QUICK SUB");
    }
}

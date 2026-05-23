package crm.csv;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Cloud-ready CSV processing utility.
 * Uses Spring's ResourceLoader to support multiple storage backends:
 * - Classpath resources (classpath:/data/file.csv)
 * - File system (file:/path/to/file.csv)
 * - Azure Blob Storage (azure-blob://container/file.csv)
 * - HTTP/HTTPS URLs (https://example.com/file.csv)
 * 
 * File paths are externalized via application properties and Azure App Configuration.
 */
@Component
public class CSVTest {

    private final ResourceLoader resourceLoader;

    @Value("${app.csv.default-file:classpath:/data/sample.csv}")
    private String defaultCsvFile;

    @Autowired
    public CSVTest(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Processes a CSV file from the configured location.
     * Supports Azure Blob Storage and other Spring Resource protocols.
     * 
     * @param resourcePath the path to the CSV file (supports Spring Resource protocols)
     * @return List of parsed CSV rows
     * @throws IOException if file cannot be read or parsed
     */
    public List<String[]> processCsvFile(String resourcePath) throws IOException {
        Resource resource = resourceLoader.getResource(resourcePath);
        
        if (!resource.exists()) {
            throw new IOException("CSV file not found: " + resourcePath);
        }

        List<String[]> data = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
                
                // Example business logic: filter specific records
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    System.out.println(String.format("Found QUICK SUB record: %s\t%s\t%s", 
                        line[0], line[1], line.length > 2 ? line[2] : ""));
                }
            }
        }
        
        return data;
    }

    /**
     * Processes the default CSV file configured in application properties.
     * 
     * @return List of parsed CSV rows
     * @throws IOException if file cannot be read or parsed
     */
    public List<String[]> processDefaultCsvFile() throws IOException {
        return processCsvFile(defaultCsvFile);
    }

    /**
     * Legacy main method - deprecated for cloud environments.
     * Use processCsvFile() or processDefaultCsvFile() instead.
     * 
     * @deprecated This method uses GUI file chooser which is not cloud-compatible.
     */
    @Deprecated
    public static void main(String[] args) {
        System.err.println("ERROR: GUI file chooser is not supported in cloud environments.");
        System.err.println("Please use the processCsvFile() method with externalized file paths.");
        System.err.println("Configure file paths in application.properties or Azure App Configuration:");
        System.err.println("  app.csv.default-file=classpath:/data/sample.csv");
        System.err.println("  app.csv.default-file=azure-blob://container/data/sample.csv");
        System.exit(1);
    }

}

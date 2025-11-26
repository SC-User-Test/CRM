package crm.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-ready file utility for reading data from classpath resources or cloud storage.
 * Removed Desktop UI components (JFileChooser) for cloud compatibility.
 *
 * For cloud deployment, files should be:
 * - Placed in src/main/resources directory
 * - Uploaded to cloud storage (S3, Azure Blob, GCS)
 * - Passed via REST API endpoints
 */
public class ReadDataUtils {

    /**
     * Read file from classpath resources (cloud-compatible).
     * @param resourcePath Path relative to classpath (e.g., "data/file.csv")
     * @return InputStream of the resource
     * @throws IOException if resource not found
     */
    public static InputStream readFileFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return resource.getInputStream();
    }

    /**
     * Read file from environment variable path (cloud-compatible).
     * Allows configuration via environment variables for cloud deployment.
     * @param envVarName Environment variable name containing file path
     * @return InputStream of the file
     * @throws IOException if file not found or path not configured
     */
    public static InputStream readFileFromEnvPath(String envVarName) throws IOException {
        String filePath = System.getenv(envVarName);
        if (filePath == null || filePath.isEmpty()) {
            throw new IOException("Environment variable not set: " + envVarName);
        }
        Resource resource = new ClassPathResource(filePath);
        if (!resource.exists()) {
            throw new IOException("File not found at path: " + filePath);
        }
        return resource.getInputStream();
    }

}

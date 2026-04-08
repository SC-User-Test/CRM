package crm.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading data files.
 * Uses classpath resources and environment variables instead of absolute file paths.
 * Compatible with AWS, Azure, and GCP cloud environments.
 */
@Component
public class ReadDataUtils {

    /**
     * Reads a file from classpath resources (cloud-compatible).
     * Files should be placed in src/main/resources directory.
     * 
     * @param resourcePath Path relative to classpath (e.g., "data/file.csv")
     * @return InputStream to read the file
     * @throws IOException if file cannot be found or read
     */
    public static InputStream readFileFromClasspath(String resourcePath) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        if (!resource.exists()) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return resource.getInputStream();
    }

    /**
     * Reads a file from an environment-variable-configured path.
     * This allows different paths per deployment environment.
     * 
     * @param envVarName Environment variable name containing the file path
     * @param defaultPath Default path if environment variable is not set
     * @return File path from environment or default
     */
    public static String getFilePathFromEnvironment(String envVarName, String defaultPath) {
        String path = System.getenv(envVarName);
        if (path == null || path.trim().isEmpty()) {
            return defaultPath;
        }
        return path;
    }

    /**
     * Gets the configured data directory from environment variables.
     * For cloud deployments, this should point to a mounted persistent volume
     * or cloud storage path (e.g., /mnt/efs/data for AWS EFS).
     * 
     * @return Data directory path
     */
    public static String getDataDirectory() {
        return getFilePathFromEnvironment("DATA_DIRECTORY", "/tmp/data");
    }

    /**
     * Gets the configured upload directory from environment variables.
     * For cloud deployments, this should point to a mounted persistent volume
     * or cloud storage path.
     * 
     * @return Upload directory path
     */
    public static String getUploadDirectory() {
        return getFilePathFromEnvironment("UPLOAD_DIRECTORY", "/tmp/uploads");
    }
}

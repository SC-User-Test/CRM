package crm.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Cloud-ready utility for reading data files.
 * Uses Spring's ResourceLoader to support classpath, file system, and cloud storage resources.
 * File paths are externalized via application properties and Azure App Configuration.
 */
@Component
public class ReadDataUtils {

    private final ResourceLoader resourceLoader;
    
    @Value("${app.file.base-path:classpath:/data/}")
    private String basePath;

    public ReadDataUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Reads a file from the configured base path or cloud storage.
     * Supports Azure Blob Storage URLs (e.g., azure-blob://container/path/file.ext)
     * 
     * @param fileName the name of the file to read
     * @return InputStream of the file content
     * @throws IOException if file cannot be read
     */
    public InputStream readFile(String fileName) throws IOException {
        String fullPath = basePath + fileName;
        Resource resource = resourceLoader.getResource(fullPath);
        
        if (!resource.exists()) {
            throw new IOException("File not found: " + fullPath);
        }
        
        return resource.getInputStream();
    }

    /**
     * Reads a file from a specific path (supports environment variable substitution).
     * 
     * @param filePath the full path to the file (can include ${ENV_VAR} placeholders)
     * @return InputStream of the file content
     * @throws IOException if file cannot be read
     */
    public InputStream readFileFromPath(String filePath) throws IOException {
        Resource resource = resourceLoader.getResource(filePath);
        
        if (!resource.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        
        return resource.getInputStream();
    }

    /**
     * Legacy method for backward compatibility - deprecated.
     * Use readFile(String fileName) instead.
     * 
     * @deprecated This method uses Swing components which are not cloud-compatible.
     * Use readFile(String fileName) with externalized configuration instead.
     */
    @Deprecated
    public static File ReadFile(String dialogMessage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "File chooser dialogs are not supported in cloud environments. " +
            "Use readFile(String fileName) with externalized file paths from Azure App Configuration."
        );
    }

}

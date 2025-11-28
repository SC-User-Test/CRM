package crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
public class ReadDataUtils {

    private static final Logger log = LoggerFactory.getLogger(ReadDataUtils.class);
    private final ResourceLoader resourceLoader;

    public ReadDataUtils(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    /**
     * Cloud-native file processing from MultipartFile upload
     * @param file Uploaded file from web request
     * @param allowedExtensions Allowed file extensions
     * @return Temporary file path or null if invalid
     */
    public Path processUploadedFile(MultipartFile file, String... allowedExtensions) {
        if (file == null || file.isEmpty()) {
            log.warn("No file provided for processing at {}", LocalDateTime.now());
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            log.warn("File has no filename at {}", LocalDateTime.now());
            return null;
        }

        // Validate file extension
        String fileExtension = getFileExtension(originalFilename);
        if (!isValidExtension(fileExtension, allowedExtensions)) {
            log.warn("Invalid file extension '{}' for file '{}'. Allowed extensions: {}",
                    fileExtension, originalFilename, Arrays.toString(allowedExtensions));
            return null;
        }

        try {
            // Create temporary file with unique name
            Path tempFile = Files.createTempFile("upload_" + UUID.randomUUID(), "_" + originalFilename);

            // Copy uploaded file to temporary location
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            log.info("File '{}' processed successfully to temporary path: {} at {}",
                    originalFilename, tempFile.toString(), LocalDateTime.now());

            return tempFile;

        } catch (IOException e) {
            log.error("Failed to process uploaded file '{}': {}", originalFilename, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Load resource from classpath for cloud deployment compatibility
     * @param resourcePath Path to resource in classpath
     * @return Resource or null if not found
     */
    public Resource loadClasspathResource(String resourcePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + resourcePath);
            if (resource.exists()) {
                log.info("Loaded classpath resource: {} at {}", resourcePath, LocalDateTime.now());
                return resource;
            } else {
                log.warn("Classpath resource not found: {} at {}", resourcePath, LocalDateTime.now());
                return null;
            }
        } catch (Exception e) {
            log.error("Failed to load classpath resource '{}': {}", resourcePath, e.getMessage(), e);
            return null;
        }
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1).toLowerCase() : "";
    }

    private boolean isValidExtension(String extension, String[] allowedExtensions) {
        return Arrays.stream(allowedExtensions)
                .map(String::toLowerCase)
                .anyMatch(allowed -> allowed.equals(extension));
    }
}

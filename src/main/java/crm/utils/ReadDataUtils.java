package crm.utils;

import java.io.File;

/**
 * DEPRECATED: This class used JFileChooser which is incompatible with containerized/headless environments.
 * For file uploads in containerized applications, use MultipartFile with REST endpoints.
 *
 * Example replacement:
 * @PostMapping("/upload")
 * public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
 *     // Process the uploaded file
 * }
 *
 * This class is kept for backward compatibility but should not be used in container environments.
 */
@Deprecated
public class ReadDataUtils {

    /**
     * @deprecated This method uses JFileChooser (Swing GUI) which cannot run in headless container environments.
     * Replace with web-based file upload using MultipartFile in Spring controllers.
     *
     * This method will throw HeadlessException in containerized environments.
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "JFileChooser is not supported in containerized/headless environments. " +
            "Use web-based file upload with MultipartFile instead. " +
            "See class documentation for migration example."
        );
    }

}

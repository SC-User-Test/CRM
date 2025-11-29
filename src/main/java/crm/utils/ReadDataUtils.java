package crm.utils;

import java.io.File;
import java.nio.file.Paths;

public class ReadDataUtils {

    public static File ReadFile(String dialogMEssage, String unused, String fileExtensionDescription,
                                String... fileExtension) {
        // Replace GUI file chooser with environment variable configuration
        String filePath = System.getenv("CSV_FILE_PATH");
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                System.out.println("Using configured file: " + file.getName());
                return file;
            }
        }
        System.out.println("No valid CSV_FILE_PATH environment variable configured");
        return null;
    }

}

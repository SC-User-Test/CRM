package crm.utils;

import java.io.File;
import java.util.Arrays;

public class ReadDataUtils {

    public static File ReadFile(String dialogMEssage, String filePath, String fileExtensionDescription,
                                String... fileExtension) {
        // For containerized environments, accept file path directly instead of using GUI
        if (filePath == null || filePath.trim().isEmpty()) {
            System.out.println("No file path provided for: " + dialogMEssage);
            return null;
        }

        File file = new File(filePath.trim());
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return null;
        }

        // Validate file extension if provided
        if (fileExtension != null && fileExtension.length > 0) {
            String fileName = file.getName().toLowerCase();
            boolean validExtension = Arrays.stream(fileExtension)
                .anyMatch(ext -> fileName.endsWith("." + ext.toLowerCase()));
            if (!validExtension) {
                System.out.println("Invalid file extension. Expected: " + Arrays.toString(fileExtension));
                return null;
            }
        }

        System.out.println("Selected file: " + file.getName());
        return file;
    }

}

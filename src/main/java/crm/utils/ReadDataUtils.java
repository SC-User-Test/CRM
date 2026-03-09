package crm.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * @deprecated This class uses GUI components (JFileChooser) that are incompatible with headless
 * container environments. Use REST API endpoints for file uploads instead.
 * See CsvUploadController for container-compatible file upload implementation.
 */
@Deprecated
public class ReadDataUtils {

    /**
     * @deprecated Replaced by REST API file upload endpoints. This method will throw
     * HeadlessException in containerized environments. Use MultipartFile upload via
     * HTTP POST instead (see CsvUploadController).
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, JFrame parent, String fileExtensionDescription,
                                String... fileExtension) {
        // This method is deprecated and should not be used in container environments
        // It will fail with HeadlessException when no display is available
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileExtensionDescription, fileExtension);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            return chooser.getSelectedFile();
        }
        return null;
    }

}

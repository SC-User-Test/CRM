package crm.utils;

import java.io.File;

public class ReadDataUtils {

    /**
     * @deprecated This method uses GUI components (JFileChooser) which are not compatible with containerized environments.
     * Use API-based file upload endpoints instead with multipart/form-data requests.
     * This method will throw HeadlessException in container environments.
     */
    @Deprecated
    public static File ReadFile(String dialogMEssage, Object parent, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException(
            "GUI-based file selection is not supported in containerized environments. " +
            "Please use API-based file upload endpoints instead."
        );
    }

}

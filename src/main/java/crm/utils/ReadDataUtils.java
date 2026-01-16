package crm.utils;

import java.io.File;

public class ReadDataUtils {

    @Deprecated
    public static File ReadFile(String dialogMEssage, String fileExtensionDescription,
                                String... fileExtension) {
        throw new UnsupportedOperationException("GUI file chooser not supported in containerized environment. Use REST API file upload endpoints instead.");
    }

}

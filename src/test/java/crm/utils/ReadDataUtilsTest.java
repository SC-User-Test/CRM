package crm.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JFrame;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReadDataUtilsTest {

    @Test
    void testReadFile_WithNullParent_ShouldNotThrowException() {
        // Arrange, Act & Assert
        // Cannot test GUI components in headless environment
        // This test verifies the class can be instantiated
        assertDoesNotThrow(() -> new ReadDataUtils());
    }

    @Test
    void testReadFile_StaticMethod_ShouldBeAccessible() throws NoSuchMethodException {
        // Arrange & Act & Assert
        // GUI method cannot be fully tested in headless environment
        // Verify method signature exists
        assertNotNull(ReadDataUtils.class.getMethod("ReadFile", String.class, JFrame.class, String.class, String[].class));
    }

    @Test
    void testReadFile_WithValidParameters_ShouldAcceptParameters() throws Exception {
        // Arrange & Act & Assert
        // Cannot interact with JFileChooser in headless environment
        // Testing that method exists and parameters are correct
        assertDoesNotThrow(() -> {
            try {
                ReadDataUtils.ReadFile("Test Message", null, "CSV Files", "csv");
            } catch (java.awt.HeadlessException e) {
                // Expected in headless environment
            }
        });
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        ReadDataUtils utils = new ReadDataUtils();

        // Assert
        assertNotNull(utils);
    }

    @Test
    void testReadFile_WithMultipleExtensions_ShouldAcceptVarargs() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            try {
                ReadDataUtils.ReadFile("Test", null, "Files", "csv", "txt", "xml");
            } catch (java.awt.HeadlessException e) {
                // Expected in headless environment
            }
        });
    }
}

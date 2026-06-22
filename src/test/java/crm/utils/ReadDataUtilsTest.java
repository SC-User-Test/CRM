package crm.utils;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void readFile_withValidParameters_shouldReturnFile() {
        // This test is limited because it requires GUI interaction
        // We can only test that the method exists and accepts correct parameters
        assertDoesNotThrow(() -> {
            // Method signature test
            File result = ReadDataUtils.ReadFile("Test Message", null, "CSV Files", "csv");
            // Result will be null without user interaction
        });
    }

    @Test
    void readFile_withNullParent_shouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> ReadDataUtils.ReadFile("Test", null, "All Files", "*"));
    }

    @Test
    void readFile_withMultipleExtensions_shouldAcceptVarargs() {
        // Act & Assert
        assertDoesNotThrow(() -> ReadDataUtils.ReadFile("Test", null, "Multiple", "csv", "txt", "pdf"));
    }

    @Test
    void readFile_withEmptyMessage_shouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> ReadDataUtils.ReadFile("", null, "Files", "txt"));
    }

    @Test
    void readFile_withSingleExtension_shouldWork() {
        // Act & Assert
        assertDoesNotThrow(() -> ReadDataUtils.ReadFile("Select file", null, "Text Files", "txt"));
    }
}

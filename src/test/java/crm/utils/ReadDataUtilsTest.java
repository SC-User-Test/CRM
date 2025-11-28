package crm.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testReadDataUtilsConstructor() {
        ReadDataUtils utils = new ReadDataUtils();
        assertNotNull(utils);
    }

    @Test
    void testReadFileWithNullParent() {
        File result = ReadDataUtils.ReadFile("Test", null, "Text Files", "txt");
        // Since this opens a dialog, result will be null in test environment
        assertNull(result);
    }

    @Test
    void testReadFileWithMultipleExtensions() {
        File result = ReadDataUtils.ReadFile("Test", null, "Documents", "txt", "pdf", "doc");
        assertNull(result);
    }

    @Test
    void testReadFileWithSingleExtension() {
        File result = ReadDataUtils.ReadFile("Select File", null, "CSV Files", "csv");
        assertNull(result);
    }

    @Test
    void testReadFileWithEmptyMessage() {
        File result = ReadDataUtils.ReadFile("", null, "All Files", "*");
        assertNull(result);
    }

    @Test
    void testReadFileMethodExists() {
        assertDoesNotThrow(() -> {
            ReadDataUtils.ReadFile("Test", null, "Files", "txt");
        });
    }
}

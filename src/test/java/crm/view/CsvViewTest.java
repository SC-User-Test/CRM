package crm.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewTest {

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        CsvView csvView = new CsvView();
        // Assert
        assertNotNull(csvView);
    }

    @Test
    void testContentType_isCsv() {
        // Arrange & Act
        CsvView csvView = new CsvView();
        // Assert
        assertEquals("text/csv", csvView.getContentType());
    }

    @Test
    void testGeneratesDownloadContent_returnsTrue() {
        // Arrange
        CsvView csvView = new CsvView();
        // Act
        boolean result = csvView.generatesDownloadContent();
        // Assert
        assertTrue(result);
    }
}

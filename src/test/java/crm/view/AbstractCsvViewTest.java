package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCsvViewTest {

    private AbstractCsvView abstractCsvView;

    @BeforeEach
    void setUp() {
        // Use concrete subclass (CsvView) to test abstract class behavior
        abstractCsvView = new CsvView();
    }

    @Test
    void testConstructor_setsContentType() {
        // Arrange & Act
        AbstractCsvView view = new CsvView();
        // Assert
        assertNotNull(view);
        assertEquals("text/csv", view.getContentType());
    }

    @Test
    void testGeneratesDownloadContent_returnsTrue() {
        // Arrange & Act
        boolean result = abstractCsvView.generatesDownloadContent();
        // Assert
        assertTrue(result);
    }

    @Test
    void testSetUrl_doesNotThrow() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> abstractCsvView.setUrl("http://example.com/csv"));
    }

    @Test
    void testContentType_isCsv() {
        // Arrange & Act
        String contentType = abstractCsvView.getContentType();
        // Assert
        assertEquals("text/csv", contentType);
    }
}

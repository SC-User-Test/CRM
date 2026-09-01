package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPdfViewTest {

    private AbstractPdfView abstractPdfView;

    @BeforeEach
    void setUp() {
        // Use concrete subclass (PdfView) to test abstract class behavior
        abstractPdfView = new PdfView();
    }

    @Test
    void testConstructor_setsContentType() {
        // Arrange & Act
        AbstractPdfView view = new PdfView();
        // Assert
        assertNotNull(view);
        assertEquals("application/pdf", view.getContentType());
    }

    @Test
    void testGeneratesDownloadContent_returnsTrue() {
        // Arrange & Act
        boolean result = abstractPdfView.generatesDownloadContent();
        // Assert
        assertTrue(result);
    }

    @Test
    void testContentType_isPdf() {
        // Arrange & Act
        String contentType = abstractPdfView.getContentType();
        // Assert
        assertEquals("application/pdf", contentType);
    }

    @Test
    void testGetViewerPreferences_returnsNonZero() {
        // Arrange & Act
        int prefs = abstractPdfView.getViewerPreferences();
        // Assert
        assertTrue(prefs > 0);
    }
}

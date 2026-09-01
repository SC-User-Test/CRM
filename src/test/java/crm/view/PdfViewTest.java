package crm.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewTest {

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        PdfView pdfView = new PdfView();
        // Assert
        assertNotNull(pdfView);
    }

    @Test
    void testContentType_isPdf() {
        // Arrange & Act
        PdfView pdfView = new PdfView();
        // Assert
        assertEquals("application/pdf", pdfView.getContentType());
    }

    @Test
    void testGeneratesDownloadContent_returnsTrue() {
        // Arrange
        PdfView pdfView = new PdfView();
        // Act
        boolean result = pdfView.generatesDownloadContent();
        // Assert
        assertTrue(result);
    }
}

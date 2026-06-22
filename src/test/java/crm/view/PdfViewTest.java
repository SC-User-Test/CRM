package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewTest {

    private PdfView pdfView;

    @BeforeEach
    void setUp() {
        pdfView = new PdfView();
    }

    @Test
    void testPdfViewCreation() {
        // Assert
        assertNotNull(pdfView);
    }

    @Test
    void testPdfViewExtendsAbstractPdfView() {
        // Assert
        assertTrue(AbstractPdfView.class.isAssignableFrom(PdfView.class));
    }

    @Test
    void testPdfViewIsInstantiable() {
        // Act
        PdfView view = new PdfView();

        // Assert
        assertNotNull(view);
    }
}

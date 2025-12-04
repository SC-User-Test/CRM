package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfViewTest {

    private PdfView pdfView;

    @BeforeEach
    void setUp() {
        pdfView = new PdfView();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        PdfView view = new PdfView();

        // Assert
        assertNotNull(view);
    }

    @Test
    void testPdfView_ShouldExtendAbstractPdfView() {
        // Arrange & Act & Assert
        assertTrue(pdfView instanceof AbstractPdfView);
    }

    @Test
    void testPdfView_ShouldBeInstantiable() {
        // Arrange & Act
        PdfView view = new PdfView();

        // Assert
        assertNotNull(view);
        assertInstanceOf(PdfView.class, view);
    }
}

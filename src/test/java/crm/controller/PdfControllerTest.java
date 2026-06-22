package crm.controller;

import crm.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Controller;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    private PdfController pdfController;

    @BeforeEach
    void setUp() {
        pdfController = new PdfController(pdfService);
    }

    @Test
    void testPdfControllerCreation() {
        // Assert
        assertNotNull(pdfController);
    }

    @Test
    void testPdfControllerHasControllerAnnotation() {
        // Assert
        assertTrue(PdfController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testPdfControllerConstructor() {
        // Act
        PdfController controller = new PdfController(pdfService);

        // Assert
        assertNotNull(controller);
    }
}

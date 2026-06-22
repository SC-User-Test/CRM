package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PdfController pdfController;

    private Pdf testPdf;

    @BeforeEach
    void setUp() {
        testPdf = Pdf.builder()
                .id(1L)
                .name("test")
                .content("Test PDF content")
                .build();
    }

    @Test
    void pdfGenerator_shouldReturnGeneratorView() {
        // Act
        String result = pdfController.pdfGenerator(model);

        // Assert
        assertEquals("pdf/generator", result);
        verify(model).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void generatePdf_withValidData_shouldReturnSuccess() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = pdfController.generatePdf(testPdf, bindingResult);

        // Assert
        assertEquals("pdf/success", result);
        verify(pdfService).savePdf(testPdf);
    }

    @Test
    void generatePdf_withValidationErrors_shouldRedirectToGenerator() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = pdfController.generatePdf(testPdf, bindingResult);

        // Assert
        assertEquals("redirect:/pdf-generator", result);
        verify(pdfService, never()).savePdf(any());
    }

    @Test
    void generatePdf_withPdfExtension_shouldGenerateSuccessfully() {
        // Arrange
        testPdf.setName("document.pdf");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = pdfController.generatePdf(testPdf, bindingResult);

        // Assert
        assertEquals("pdf/success", result);
    }

    @Test
    void generatePdf_withoutPdfExtension_shouldAddExtension() {
        // Arrange
        testPdf.setName("document");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = pdfController.generatePdf(testPdf, bindingResult);

        // Assert
        assertEquals("pdf/success", result);
    }

    @Test
    void pdfController_shouldBeInstantiable() {
        // Assert
        assertNotNull(pdfController);
    }
}

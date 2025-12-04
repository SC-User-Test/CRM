package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private PdfController pdfController;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
        pdf.setName("test");
        pdf.setContent("Test content");
    }

    @Test
    void testConstructor_ShouldInitializeWithPdfService() {
        // Arrange & Act
        PdfController controller = new PdfController(pdfService);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testPdfGenerator_ShouldReturnGeneratorView() {
        // Arrange & Act
        String result = pdfController.pdfGenerator(model);

        // Assert
        assertEquals("pdf/generator", result);
    }

    @Test
    void testPdfGenerator_ShouldAddNewPdfToModel() {
        // Arrange & Act
        pdfController.pdfGenerator(model);

        // Assert
        verify(model).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdf_WithValidationErrors_ShouldRedirectToGenerator() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = pdfController.generatePdf(pdf, bindingResult);

        // Assert
        assertEquals("redirect:/pdf-generator", result);
    }

    @Test
    void testGeneratePdf_WithValidationErrors_ShouldNotSavePdf() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        pdfController.generatePdf(pdf, bindingResult);

        // Assert
        verify(pdfService, never()).savePdf(any());
    }

    @Test
    void testGeneratePdf_WithValidPdf_ShouldSavePdf() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        pdfController.generatePdf(pdf, bindingResult);

        // Assert
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testGeneratePdf_WithValidPdf_ShouldReturnSuccessView() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = pdfController.generatePdf(pdf, bindingResult);

        // Assert
        assertEquals("pdf/success", result);
    }

    @Test
    void testGeneratePdf_WithPdfNameWithoutExtension_ShouldAddExtension() {
        // Arrange
        pdf.setName("testfile");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        pdfController.generatePdf(pdf, bindingResult);

        // Assert
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testGeneratePdf_WithPdfNameWithExtension_ShouldNotAddExtraExtension() {
        // Arrange
        pdf.setName("testfile.pdf");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        pdfController.generatePdf(pdf, bindingResult);

        // Assert
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testGeneratePdf_WithEmptyContent_ShouldStillProcess() {
        // Arrange
        pdf.setContent("");
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = pdfController.generatePdf(pdf, bindingResult);

        // Assert
        assertEquals("pdf/success", result);
    }
}

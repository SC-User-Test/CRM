package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private PdfController pdfController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfController = new PdfController(pdfService);
    }

    @Test
    public void testPdfControllerCreation() {
        assertNotNull(pdfController);
    }

    @Test
    public void testPdfGenerator() {
        String result = pdfController.pdfGenerator(model);
        assertEquals("pdf/generator", result);
        verify(model, times(1)).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    public void testGeneratePdfSuccess() {
        Pdf pdf = Pdf.builder()
                .name("test-document")
                .content("Test content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", result);
    }

    @Test
    public void testGeneratePdfValidationErrors() {
        Pdf pdf = Pdf.builder()
                .name("test-document")
                .content("Test content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("redirect:/pdf-generator", result);
        verify(pdfService, never()).savePdf(pdf);
    }

    @Test
    public void testGeneratePdfWithExtension() {
        Pdf pdf = Pdf.builder()
                .name("document.pdf")
                .content("Content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", result);
    }

    @Test
    public void testGeneratePdfWithoutExtension() {
        Pdf pdf = Pdf.builder()
                .name("document")
                .content("Content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", result);
    }

    @Test
    public void testPdfGeneratorMultipleCalls() {
        pdfController.pdfGenerator(model);
        pdfController.pdfGenerator(model);
        verify(model, times(2)).addAttribute(eq("pdf"), any(Pdf.class));
    }
}

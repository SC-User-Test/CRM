package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PdfController pdfController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPdfGenerator() {
        String result = pdfController.pdfGenerator(model);

        assertEquals("pdf/generator", result);
        verify(model, times(1)).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdfWithErrors() {
        Pdf pdf = new Pdf();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("redirect:/pdf-generator", result);
        verify(pdfService, never()).savePdf(any());
    }

    @Test
    void testGeneratePdfSuccess() {
        Pdf pdf = new Pdf();
        pdf.setName("TestPdf");
        pdf.setContent("Test Content");
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("pdf/success", result);
        verify(pdfService, times(1)).savePdf(pdf);
    }

    @Test
    void testGenerateSamplePdfMethodExists() {
        assertDoesNotThrow(() -> {
            PdfController.class.getDeclaredMethod("generateSamplePdf", String.class, String.class);
        });
    }
}

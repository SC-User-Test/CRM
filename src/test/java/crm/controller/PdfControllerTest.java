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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PdfControllerTest {

    @InjectMocks
    private PdfController pdfController;

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPdfControllerConstructor() {
        PdfController controller = new PdfController(pdfService);
        assertNotNull(controller);
    }

    @Test
    void testPdfGenerator() {
        String viewName = pdfController.pdfGenerator(model);

        assertEquals("pdf/generator", viewName);
        verify(model).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdfWithValidData() {
        Pdf pdf = Pdf.builder()
                .name("test")
                .content("Test content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("pdf/success", viewName);
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testGeneratePdfWithErrors() {
        Pdf pdf = Pdf.builder()
                .name("test")
                .content("Content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("redirect:/pdf-generator", viewName);
        verify(pdfService, never()).savePdf(any());
    }

    @Test
    void testGeneratePdfWithNullName() {
        Pdf pdf = Pdf.builder()
                .name(null)
                .content("Content")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("pdf/success", viewName);
    }

    @Test
    void testGeneratePdfWithEmptyContent() {
        Pdf pdf = Pdf.builder()
                .name("empty")
                .content("")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("pdf/success", viewName);
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testPdfGeneratorAddsModelAttribute() {
        pdfController.pdfGenerator(model);

        verify(model, times(1)).addAttribute(eq("pdf"), any(Pdf.class));
    }
}

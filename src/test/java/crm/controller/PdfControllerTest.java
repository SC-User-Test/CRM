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

    @InjectMocks
    private PdfController pdfController;

    private Model model;
    private Pdf pdf;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = mock(Model.class);
        bindingResult = mock(BindingResult.class);

        pdf = new Pdf();
        pdf.setName("test");
        pdf.setContent("Test Content");
    }

    @Test
    void testConstructor() {
        PdfController controller = new PdfController(pdfService);
        assertNotNull(controller);
    }

    @Test
    void testPdfGenerator() {
        String result = pdfController.pdfGenerator(model);

        assertEquals("pdf/generator", result);
        verify(model, times(1)).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdf_WithValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = pdfController.generatePdf(pdf, bindingResult);

        assertEquals("redirect:/pdf-generator", result);
        verify(pdfService, never()).savePdf(any());
    }
}

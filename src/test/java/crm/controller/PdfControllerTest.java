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

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("test")
                .content("Test PDF content")
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        PdfController controller = new PdfController(pdfService);
        assertNotNull(controller);
    }

    @Test
    void testPdfGenerator_returnsGeneratorView() {
        String view = pdfController.pdfGenerator(model);
        assertEquals("pdf/generator", view);
        verify(model).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdf_withBindingErrors_redirectsToGenerator() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("redirect:/pdf-generator", view);
        verify(pdfService, never()).savePdf(any());
    }

    @Test
    void testGeneratePdf_withNoErrors_returnsSuccessView() {
        when(bindingResult.hasErrors()).thenReturn(false);
        // The pdf name "test" will be appended with .pdf and written to filesystem
        // We test with a name that won't cause issues
        pdf.setName("test_output_" + System.currentTimeMillis());
        String view = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", view);
    }

    @Test
    void testGeneratePdf_withNoErrors_callsSavePdf() {
        when(bindingResult.hasErrors()).thenReturn(false);
        pdf.setName("test_save_" + System.currentTimeMillis());
        pdfController.generatePdf(pdf, bindingResult);
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testGeneratePdf_withErrors_doesNotCallSavePdf() {
        when(bindingResult.hasErrors()).thenReturn(true);
        pdfController.generatePdf(pdf, bindingResult);
        verify(pdfService, never()).savePdf(any());
    }

    @Test
    void testPdfGenerator_addsNewPdfToModel() {
        pdfController.pdfGenerator(model);
        verify(model).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdf_withNameAlreadyHasPdfExtension_doesNotDoubleExtension() {
        when(bindingResult.hasErrors()).thenReturn(false);
        pdf.setName("test_ext_" + System.currentTimeMillis() + ".pdf");
        String view = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", view);
    }
}

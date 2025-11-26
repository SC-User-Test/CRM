package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pdf = new Pdf();
        pdf.setId(1L);
        pdf.setName("Test PDF");
        pdf.setContent("Test Content");
    }

    @Test
    void testConstructor() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName() {
        when(pdfRepository.findByName("Test PDF")).thenReturn(pdf);

        Pdf result = pdfService.findByName("Test PDF");

        assertNotNull(result);
        assertEquals("Test PDF", result.getName());
        verify(pdfRepository, times(1)).findByName("Test PDF");
    }

    @Test
    void testFindByName_NotFound() {
        when(pdfRepository.findByName("Nonexistent PDF")).thenReturn(null);

        Pdf result = pdfService.findByName("Nonexistent PDF");

        assertNull(result);
        verify(pdfRepository, times(1)).findByName("Nonexistent PDF");
    }

    @Test
    void testSavePdf() {
        pdfService.savePdf(pdf);

        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testSavePdf_WithNullPdf() {
        assertDoesNotThrow(() -> pdfService.savePdf(null));
        verify(pdfRepository, times(1)).save(null);
    }
}

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

    @InjectMocks
    private PdfServiceImpl pdfService;

    @Mock
    private PdfRepository pdfRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPdfServiceImplConstructor() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("Content")
                .build();

        when(pdfRepository.findByName("Test PDF")).thenReturn(pdf);

        Pdf result = pdfService.findByName("Test PDF");

        assertNotNull(result);
        assertEquals("Test PDF", result.getName());
        verify(pdfRepository, times(1)).findByName("Test PDF");
    }

    @Test
    void testFindByNameNotFound() {
        when(pdfRepository.findByName("Nonexistent")).thenReturn(null);

        Pdf result = pdfService.findByName("Nonexistent");

        assertNull(result);
        verify(pdfRepository, times(1)).findByName("Nonexistent");
    }

    @Test
    void testSavePdf() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("New PDF")
                .content("New Content")
                .build();

        pdfService.savePdf(pdf);

        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testSavePdfWithNullContent() {
        Pdf pdf = Pdf.builder()
                .id(2L)
                .name("PDF without content")
                .content(null)
                .build();

        pdfService.savePdf(pdf);

        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testFindByNameMultipleCalls() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("Document")
                .build();

        when(pdfRepository.findByName("Document")).thenReturn(pdf);

        pdfService.findByName("Document");
        pdfService.findByName("Document");

        verify(pdfRepository, times(2)).findByName("Document");
    }
}

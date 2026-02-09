package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    private PdfServiceImpl pdfService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfService = new PdfServiceImpl(pdfRepository);
    }

    @Test
    public void testPdfServiceCreation() {
        assertNotNull(pdfService);
    }

    @Test
    public void testFindByName() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("Sample content")
                .build();

        when(pdfRepository.findByName("test-document")).thenReturn(pdf);

        Pdf result = pdfService.findByName("test-document");
        assertNotNull(result);
        assertEquals("test-document", result.getName());
        verify(pdfRepository, times(1)).findByName("test-document");
    }

    @Test
    public void testFindByNameNotFound() {
        when(pdfRepository.findByName("nonexistent")).thenReturn(null);

        Pdf result = pdfService.findByName("nonexistent");
        assertNull(result);
        verify(pdfRepository, times(1)).findByName("nonexistent");
    }

    @Test
    public void testSavePdf() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("new-document")
                .content("New content")
                .build();

        when(pdfRepository.save(pdf)).thenReturn(pdf);

        pdfService.savePdf(pdf);
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    public void testSavePdfWithNullContent() {
        Pdf pdf = Pdf.builder()
                .id(2L)
                .name("no-content")
                .build();

        when(pdfRepository.save(pdf)).thenReturn(pdf);

        pdfService.savePdf(pdf);
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    public void testFindByNameWithDifferentNames() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("document1")
                .build();

        when(pdfRepository.findByName("document1")).thenReturn(pdf1);

        Pdf result = pdfService.findByName("document1");
        assertNotNull(result);
        assertEquals("document1", result.getName());
        verify(pdfRepository, times(1)).findByName("document1");
    }
}

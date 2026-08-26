package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
        pdf.setId(1L);
        pdf.setName("test-document");
        pdf.setContent("PDF content here");
    }

    @Test
    void testFindByName() {
        when(pdfRepository.findByName("test-document")).thenReturn(pdf);
        Pdf result = pdfService.findByName("test-document");
        assertNotNull(result);
        assertEquals("test-document", result.getName());
        verify(pdfRepository).findByName("test-document");
    }

    @Test
    void testFindByNameNotFound() {
        when(pdfRepository.findByName("nonexistent")).thenReturn(null);
        Pdf result = pdfService.findByName("nonexistent");
        assertNull(result);
    }

    @Test
    void testFindByNameWithPdfExtension() {
        Pdf pdfWithExt = new Pdf();
        pdfWithExt.setName("document.pdf");
        when(pdfRepository.findByName("document.pdf")).thenReturn(pdfWithExt);
        Pdf result = pdfService.findByName("document.pdf");
        assertNotNull(result);
        assertEquals("document.pdf", result.getName());
    }

    @Test
    void testSavePdf() {
        pdfService.savePdf(pdf);
        verify(pdfRepository).save(pdf);
    }

    @Test
    void testSavePdfNewPdf() {
        Pdf newPdf = new Pdf();
        newPdf.setName("new-document");
        newPdf.setContent("New content");
        pdfService.savePdf(newPdf);
        verify(pdfRepository).save(newPdf);
    }

    @Test
    void testSavePdfCallsRepositoryOnce() {
        pdfService.savePdf(pdf);
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testConstructorWithRepository() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByNameNull() {
        when(pdfRepository.findByName(null)).thenReturn(null);
        Pdf result = pdfService.findByName(null);
        assertNull(result);
    }
}

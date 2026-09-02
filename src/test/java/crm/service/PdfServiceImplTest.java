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
        pdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test PDF content")
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName_existingName_returnsPdf() {
        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);
        Pdf result = pdfService.findByName("test.pdf");
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
        verify(pdfRepository).findByName("test.pdf");
    }

    @Test
    void testFindByName_nonExistingName_returnsNull() {
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        Pdf result = pdfService.findByName("nonexistent.pdf");
        assertNull(result);
        verify(pdfRepository).findByName("nonexistent.pdf");
    }

    @Test
    void testFindByName_withNullName_returnsNull() {
        when(pdfRepository.findByName(null)).thenReturn(null);
        Pdf result = pdfService.findByName(null);
        assertNull(result);
        verify(pdfRepository).findByName(null);
    }

    @Test
    void testSavePdf_callsRepositorySave() {
        pdfService.savePdf(pdf);
        verify(pdfRepository).save(pdf);
    }

    @Test
    void testSavePdf_withNewPdf_savesSuccessfully() {
        Pdf newPdf = Pdf.builder().name("new.pdf").content("New content").build();
        pdfService.savePdf(newPdf);
        verify(pdfRepository).save(newPdf);
    }

    @Test
    void testSavePdf_withNullContent_savesSuccessfully() {
        Pdf pdfWithNullContent = Pdf.builder().name("noContent.pdf").build();
        pdfService.savePdf(pdfWithNullContent);
        verify(pdfRepository).save(pdfWithNullContent);
    }

    @Test
    void testFindByName_withEmptyString_returnsNull() {
        when(pdfRepository.findByName("")).thenReturn(null);
        Pdf result = pdfService.findByName("");
        assertNull(result);
        verify(pdfRepository).findByName("");
    }
}

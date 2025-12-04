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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        Pdf pdf = new Pdf();
        pdf.setName("TestPdf");
        when(pdfRepository.findByName("TestPdf")).thenReturn(pdf);

        Pdf result = pdfService.findByName("TestPdf");

        assertNotNull(result);
        assertEquals("TestPdf", result.getName());
        verify(pdfRepository, times(1)).findByName("TestPdf");
    }

    @Test
    void testFindByNameNotFound() {
        when(pdfRepository.findByName("NonExistent")).thenReturn(null);

        Pdf result = pdfService.findByName("NonExistent");

        assertNull(result);
        verify(pdfRepository, times(1)).findByName("NonExistent");
    }

    @Test
    void testSavePdf() {
        Pdf pdf = new Pdf();
        pdf.setName("NewPdf");
        when(pdfRepository.save(pdf)).thenReturn(pdf);

        assertDoesNotThrow(() -> pdfService.savePdf(pdf));
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testSavePdfWithNull() {
        assertDoesNotThrow(() -> pdfService.savePdf(null));
        verify(pdfRepository, times(1)).save(null);
    }

    @Test
    void testFindByNameWithEmptyString() {
        when(pdfRepository.findByName("")).thenReturn(null);

        Pdf result = pdfService.findByName("");

        assertNull(result);
        verify(pdfRepository, times(1)).findByName("");
    }
}

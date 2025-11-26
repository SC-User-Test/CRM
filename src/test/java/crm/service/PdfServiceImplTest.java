package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf pdf;

    @BeforeEach
    public void setUp() {
        pdf = new Pdf();
        pdf.setId(1L);
        pdf.setName("test.pdf");
        pdf.setContent("PDF content");
    }

    @Test
    public void testConstructor() {
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        assertNotNull(service);
    }

    @Test
    public void testFindByName() {
        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);
        Pdf result = pdfService.findByName("test.pdf");
        assertEquals(pdf, result);
        verify(pdfRepository).findByName("test.pdf");
    }

    @Test
    public void testFindByNameNotFound() {
        when(pdfRepository.findByName("notfound.pdf")).thenReturn(null);
        Pdf result = pdfService.findByName("notfound.pdf");
        assertNull(result);
        verify(pdfRepository).findByName("notfound.pdf");
    }

    @Test
    public void testSavePdf() {
        when(pdfRepository.save(pdf)).thenReturn(pdf);
        pdfService.savePdf(pdf);
        verify(pdfRepository).save(pdf);
    }

    @Test
    public void testSavePdfWithNullName() {
        Pdf pdfWithNull = new Pdf();
        pdfWithNull.setName(null);
        when(pdfRepository.save(pdfWithNull)).thenReturn(pdfWithNull);
        pdfService.savePdf(pdfWithNull);
        verify(pdfRepository).save(pdfWithNull);
    }

    @Test
    public void testSavePdfWithEmptyContent() {
        Pdf pdfEmpty = new Pdf();
        pdfEmpty.setName("empty.pdf");
        pdfEmpty.setContent("");
        when(pdfRepository.save(pdfEmpty)).thenReturn(pdfEmpty);
        pdfService.savePdf(pdfEmpty);
        verify(pdfRepository).save(pdfEmpty);
    }

    @Test
    public void testFindByNameWithEmptyString() {
        when(pdfRepository.findByName("")).thenReturn(null);
        Pdf result = pdfService.findByName("");
        assertNull(result);
        verify(pdfRepository).findByName("");
    }

    @Test
    public void testFindByNameMultipleCalls() {
        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);
        pdfService.findByName("test.pdf");
        pdfService.findByName("test.pdf");
        verify(pdfRepository, times(2)).findByName("test.pdf");
    }
}

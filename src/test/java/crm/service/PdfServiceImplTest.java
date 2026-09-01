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
                .name("document.pdf")
                .content("PDF content")
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);
        // Assert
        assertNotNull(service);
    }

    @Test
    void testFindByName_existingName_returnsPdf() {
        // Arrange
        when(pdfRepository.findByName("document.pdf")).thenReturn(pdf);
        // Act
        Pdf result = pdfService.findByName("document.pdf");
        // Assert
        assertNotNull(result);
        assertEquals("document.pdf", result.getName());
        verify(pdfRepository, times(1)).findByName("document.pdf");
    }

    @Test
    void testFindByName_nonExistingName_returnsNull() {
        // Arrange
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        // Act
        Pdf result = pdfService.findByName("nonexistent.pdf");
        // Assert
        assertNull(result);
    }

    @Test
    void testSavePdf_savesSuccessfully() {
        // Arrange
        when(pdfRepository.save(pdf)).thenReturn(pdf);
        // Act
        pdfService.savePdf(pdf);
        // Assert
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testSavePdf_withNewPdf_savesSuccessfully() {
        // Arrange
        Pdf newPdf = Pdf.builder()
                .name("report.pdf")
                .content("Report content")
                .build();
        when(pdfRepository.save(newPdf)).thenReturn(newPdf);
        // Act
        pdfService.savePdf(newPdf);
        // Assert
        verify(pdfRepository, times(1)).save(newPdf);
    }

    @Test
    void testFindByName_withDifferentNames_returnsCorrectPdf() {
        // Arrange
        Pdf pdf2 = Pdf.builder().id(2L).name("report.pdf").build();
        when(pdfRepository.findByName("report.pdf")).thenReturn(pdf2);
        // Act
        Pdf result = pdfService.findByName("report.pdf");
        // Assert
        assertNotNull(result);
        assertEquals("report.pdf", result.getName());
        assertEquals(2L, result.getId());
    }
}

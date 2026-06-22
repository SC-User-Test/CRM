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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf testPdf;

    @BeforeEach
    void setUp() {
        testPdf = new Pdf();
        testPdf.setId(1L);
        testPdf.setName("Test.pdf");
        testPdf.setContent("Test content");
    }

    @Test
    void findByName_shouldReturnPdf() {
        // Arrange
        when(pdfRepository.findByName("Test.pdf")).thenReturn(testPdf);

        // Act
        Pdf result = pdfService.findByName("Test.pdf");

        // Assert
        assertNotNull(result);
        assertEquals("Test.pdf", result.getName());
        verify(pdfRepository).findByName("Test.pdf");
    }

    @Test
    void findByName_withNonExistentName_shouldReturnNull() {
        // Arrange
        when(pdfRepository.findByName("NonExistent.pdf")).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName("NonExistent.pdf");

        // Assert
        assertNull(result);
        verify(pdfRepository).findByName("NonExistent.pdf");
    }

    @Test
    void savePdf_shouldSavePdf() {
        // Arrange
        when(pdfRepository.save(any(Pdf.class))).thenReturn(testPdf);

        // Act
        pdfService.savePdf(testPdf);

        // Assert
        verify(pdfRepository).save(testPdf);
    }

    @Test
    void constructor_shouldInitializeRepository() {
        // Arrange
        PdfRepository repository = mock(PdfRepository.class);

        // Act
        PdfServiceImpl service = new PdfServiceImpl(repository);

        // Assert
        assertNotNull(service);
    }
}

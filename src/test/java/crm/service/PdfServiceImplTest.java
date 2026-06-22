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
import static org.mockito.ArgumentMatchers.*;
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
        testPdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("PDF content")
                .build();
    }

    @Test
    void findByName_withValidName_shouldReturnPdf() {
        // Arrange
        when(pdfRepository.findByName("test.pdf")).thenReturn(testPdf);

        // Act
        Pdf result = pdfService.findByName("test.pdf");

        // Assert
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
        verify(pdfRepository).findByName("test.pdf");
    }

    @Test
    void findByName_withInvalidName_shouldReturnNull() {
        // Arrange
        when(pdfRepository.findByName("invalid.pdf")).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName("invalid.pdf");

        // Assert
        assertNull(result);
        verify(pdfRepository).findByName("invalid.pdf");
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
    void savePdf_withNullPdf_shouldCallRepository() {
        // Arrange
        when(pdfRepository.save(null)).thenReturn(null);

        // Act
        pdfService.savePdf(null);

        // Assert
        verify(pdfRepository).save(null);
    }

    @Test
    void pdfServiceImpl_shouldBeInstantiable() {
        // Assert
        assertNotNull(pdfService);
    }
}

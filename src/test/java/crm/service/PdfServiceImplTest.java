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
        pdf.setContent("Test content");
    }

    @Test
    void testConstructor_ShouldInitializeWithRepository() {
        // Arrange & Act
        PdfServiceImpl service = new PdfServiceImpl(pdfRepository);

        // Assert
        assertNotNull(service);
    }

    @Test
    void testFindByName_ShouldReturnPdf() {
        // Arrange
        when(pdfRepository.findByName("test-document")).thenReturn(pdf);

        // Act
        Pdf result = pdfService.findByName("test-document");

        // Assert
        assertEquals(pdf, result);
        verify(pdfRepository).findByName("test-document");
    }

    @Test
    void testFindByName_WithNullName_ShouldCallRepository() {
        // Arrange
        when(pdfRepository.findByName(null)).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName(null);

        // Assert
        assertNull(result);
        verify(pdfRepository).findByName(null);
    }

    @Test
    void testFindByName_WithNonExistingName_ShouldReturnNull() {
        // Arrange
        when(pdfRepository.findByName("nonexistent")).thenReturn(null);

        // Act
        Pdf result = pdfService.findByName("nonexistent");

        // Assert
        assertNull(result);
        verify(pdfRepository).findByName("nonexistent");
    }

    @Test
    void testSavePdf_ShouldCallRepository() {
        // Arrange & Act
        pdfService.savePdf(pdf);

        // Assert
        verify(pdfRepository).save(pdf);
    }

    @Test
    void testSavePdf_WithNullPdf_ShouldCallRepository() {
        // Arrange & Act
        pdfService.savePdf(null);

        // Assert
        verify(pdfRepository).save(null);
    }

    @Test
    void testSavePdf_WithNewPdf_ShouldSave() {
        // Arrange
        Pdf newPdf = new Pdf();
        newPdf.setName("new-document");
        newPdf.setContent("New content");

        // Act
        pdfService.savePdf(newPdf);

        // Assert
        verify(pdfRepository).save(newPdf);
    }

    @Test
    void testSavePdf_MultipleTimesSamePdf_ShouldCallRepositoryMultipleTimes() {
        // Arrange & Act
        pdfService.savePdf(pdf);
        pdfService.savePdf(pdf);

        // Assert
        verify(pdfRepository, times(2)).save(pdf);
    }
}

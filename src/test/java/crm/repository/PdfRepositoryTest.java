package crm.repository;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PdfRepositoryTest {

    @Autowired
    private PdfRepository pdfRepository;

    @Test
    void findByName_withValidName_shouldReturnPdf() {
        // Arrange
        Pdf pdf = Pdf.builder()
                .name("test.pdf")
                .content("content")
                .build();
        pdfRepository.save(pdf);

        // Act
        Pdf result = pdfRepository.findByName("test.pdf");

        // Assert
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
    }

    @Test
    void findByName_withInvalidName_shouldReturnNull() {
        // Act
        Pdf result = pdfRepository.findByName("invalid.pdf");

        // Assert
        assertNull(result);
    }

    @Test
    void save_shouldPersistPdf() {
        // Arrange
        Pdf pdf = Pdf.builder()
                .name("document.pdf")
                .content("PDF content")
                .build();

        // Act
        Pdf saved = pdfRepository.save(pdf);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void pdfRepository_shouldBeInjected() {
        // Assert
        assertNotNull(pdfRepository);
    }
}

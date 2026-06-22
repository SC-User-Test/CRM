package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    void pdf_shouldBeCreated() {
        // Assert
        assertNotNull(pdf);
    }

    @Test
    void builder_shouldCreatePdfWithAllFields() {
        // Arrange & Act
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("TestPdf")
                .content("Sample PDF content")
                .build();

        // Assert
        assertNotNull(pdf);
        assertEquals(1L, pdf.getId());
        assertEquals("TestPdf", pdf.getName());
        assertEquals("Sample PDF content", pdf.getContent());
    }

    @Test
    void setId_shouldSetIdCorrectly() {
        // Arrange
        Long expectedId = 10L;

        // Act
        pdf.setId(expectedId);

        // Assert
        assertEquals(expectedId, pdf.getId());
    }

    @Test
    void setName_shouldSetNameCorrectly() {
        // Arrange
        String expectedName = "Document.pdf";

        // Act
        pdf.setName(expectedName);

        // Assert
        assertEquals(expectedName, pdf.getName());
    }

    @Test
    void setContent_shouldSetContentCorrectly() {
        // Arrange
        String expectedContent = "This is PDF content";

        // Act
        pdf.setContent(expectedContent);

        // Assert
        assertEquals(expectedContent, pdf.getContent());
    }

    @Test
    void pdf_withNoArgsConstructor_shouldCreateEmptyPdf() {
        // Act
        Pdf emptyPdf = new Pdf();

        // Assert
        assertNotNull(emptyPdf);
    }

    @Test
    void pdf_withAllArgsConstructor_shouldCreateFullPdf() {
        // Act
        Pdf fullPdf = new Pdf(1L, "test.pdf", "content");

        // Assert
        assertNotNull(fullPdf);
        assertEquals(1L, fullPdf.getId());
        assertEquals("test.pdf", fullPdf.getName());
        assertEquals("content", fullPdf.getContent());
    }

    @Test
    void pdf_shouldSupportEqualsAndHashCode() {
        // Arrange
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("doc.pdf")
                .content("content")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("doc.pdf")
                .content("content")
                .build();

        // Assert
        assertEquals(pdf1, pdf2);
        assertEquals(pdf1.hashCode(), pdf2.hashCode());
    }

    @Test
    void pdf_shouldSupportToString() {
        // Arrange
        pdf.setId(1L);
        pdf.setName("report.pdf");
        pdf.setContent("Report content");

        // Act
        String result = pdf.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("report.pdf"));
    }
}

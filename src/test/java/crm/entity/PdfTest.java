package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    void testPdfCreation() {
        // Assert
        assertNotNull(pdf);
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long expectedId = 1L;

        // Act
        pdf.setId(expectedId);

        // Assert
        assertEquals(expectedId, pdf.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        String expectedName = "Document.pdf";

        // Act
        pdf.setName(expectedName);

        // Assert
        assertEquals(expectedName, pdf.getName());
    }

    @Test
    void testSetAndGetContent() {
        // Arrange
        String expectedContent = "PDF content data";

        // Act
        pdf.setContent(expectedContent);

        // Assert
        assertEquals(expectedContent, pdf.getContent());
    }

    @Test
    void testSetAndGetName_withNullValue() {
        // Act
        pdf.setName(null);

        // Assert
        assertNull(pdf.getName());
    }

    @Test
    void testSetAndGetContent_withNullValue() {
        // Act
        pdf.setContent(null);

        // Assert
        assertNull(pdf.getContent());
    }

    @Test
    void testSetAndGetName_withEmptyString() {
        // Arrange
        String emptyName = "";

        // Act
        pdf.setName(emptyName);

        // Assert
        assertEquals(emptyName, pdf.getName());
    }

    @Test
    void testBuilderPattern() {
        // Arrange & Act
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("TestDocument.pdf")
                .content("Test content")
                .build();

        // Assert
        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("TestDocument.pdf", builtPdf.getName());
        assertEquals("Test content", builtPdf.getContent());
    }

    @Test
    void testAllArgsConstructor() {
        // Act
        Pdf pdf = new Pdf(1L, "Document.pdf", "Content");

        // Assert
        assertNotNull(pdf);
        assertEquals(1L, pdf.getId());
        assertEquals("Document.pdf", pdf.getName());
        assertEquals("Content", pdf.getContent());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Pdf pdf = new Pdf();

        // Assert
        assertNotNull(pdf);
        assertNull(pdf.getId());
        assertNull(pdf.getName());
        assertNull(pdf.getContent());
    }

    @Test
    void testPdfEquality() {
        // Arrange
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("Test.pdf")
                .content("Content")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("Test.pdf")
                .content("Content")
                .build();

        // Assert
        assertEquals(pdf1, pdf2);
    }

    @Test
    void testPdfInequality() {
        // Arrange
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("Test1.pdf")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(2L)
                .name("Test2.pdf")
                .build();

        // Assert
        assertNotEquals(pdf1, pdf2);
    }

    @Test
    void testPdfHashCode() {
        // Arrange
        pdf.setId(1L);
        pdf.setName("Test.pdf");

        // Act
        int hashCode = pdf.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void testPdfToString() {
        // Arrange
        pdf.setId(1L);
        pdf.setName("Document.pdf");

        // Act
        String toString = pdf.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Pdf"));
    }

    @Test
    void testEntityAnnotationPresent() {
        // Assert
        assertTrue(Pdf.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void testSetLongName() {
        // Arrange
        String longName = "A".repeat(255) + ".pdf";

        // Act
        pdf.setName(longName);

        // Assert
        assertEquals(longName, pdf.getName());
    }

    @Test
    void testSetLargeContent() {
        // Arrange
        String largeContent = "Content ".repeat(1000);

        // Act
        pdf.setContent(largeContent);

        // Assert
        assertEquals(largeContent, pdf.getContent());
    }

    @Test
    void testSetAndGetId_withNullValue() {
        // Act
        pdf.setId(null);

        // Assert
        assertNull(pdf.getId());
    }

    @Test
    void testBuilderWithPartialFields() {
        // Arrange & Act
        Pdf partialPdf = Pdf.builder()
                .name("Partial.pdf")
                .build();

        // Assert
        assertNotNull(partialPdf);
        assertNull(partialPdf.getId());
        assertEquals("Partial.pdf", partialPdf.getName());
        assertNull(partialPdf.getContent());
    }
}

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
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        Pdf p = new Pdf();
        // Assert
        assertNotNull(p);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        // Arrange
        Long id = 1L;
        String name = "document.pdf";
        String content = "PDF content here";
        // Act
        Pdf p = new Pdf(id, name, content);
        // Assert
        assertNotNull(p);
        assertEquals(id, p.getId());
        assertEquals(name, p.getName());
        assertEquals(content, p.getContent());
    }

    @Test
    void testBuilder_createsPdfWithAllFields() {
        // Arrange & Act
        Pdf p = Pdf.builder()
                .id(1L)
                .name("report.pdf")
                .content("Report content")
                .build();
        // Assert
        assertNotNull(p);
        assertEquals(1L, p.getId());
        assertEquals("report.pdf", p.getName());
        assertEquals("Report content", p.getContent());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        // Arrange
        Long expectedId = 3L;
        // Act
        pdf.setId(expectedId);
        // Assert
        assertEquals(expectedId, pdf.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        // Arrange
        String expectedName = "invoice.pdf";
        // Act
        pdf.setName(expectedName);
        // Assert
        assertEquals(expectedName, pdf.getName());
    }

    @Test
    void testSetAndGetContent_returnsCorrectContent() {
        // Arrange
        String expectedContent = "This is the PDF content.";
        // Act
        pdf.setContent(expectedContent);
        // Assert
        assertEquals(expectedContent, pdf.getContent());
    }

    @Test
    void testSetId_withNull_returnsNull() {
        // Arrange & Act
        pdf.setId(null);
        // Assert
        assertNull(pdf.getId());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        // Arrange & Act
        pdf.setName(null);
        // Assert
        assertNull(pdf.getName());
    }

    @Test
    void testSetContent_withNull_returnsNull() {
        // Arrange & Act
        pdf.setContent(null);
        // Assert
        assertNull(pdf.getContent());
    }

    @Test
    void testEquals_equalPdfs_returnsTrue() {
        // Arrange
        Pdf p1 = Pdf.builder().id(1L).name("doc.pdf").build();
        Pdf p2 = Pdf.builder().id(1L).name("doc.pdf").build();
        // Act & Assert
        assertEquals(p1, p2);
    }

    @Test
    void testEquals_differentPdfs_returnsFalse() {
        // Arrange
        Pdf p1 = Pdf.builder().id(1L).name("doc1.pdf").build();
        Pdf p2 = Pdf.builder().id(2L).name("doc2.pdf").build();
        // Act & Assert
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode_equalPdfs_sameHashCode() {
        // Arrange
        Pdf p1 = Pdf.builder().id(1L).name("doc.pdf").build();
        Pdf p2 = Pdf.builder().id(1L).name("doc.pdf").build();
        // Act & Assert
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString_notNull() {
        // Arrange
        pdf.setId(1L);
        pdf.setName("test.pdf");
        // Act
        String result = pdf.toString();
        // Assert
        assertNotNull(result);
    }
}

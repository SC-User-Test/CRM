package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
        pdf.setId(1L);
        pdf.setName("test-document");
        pdf.setContent("This is test content");
    }

    @Test
    void testNoArgsConstructor_ShouldCreateInstance() {
        // Arrange & Act
        Pdf newPdf = new Pdf();

        // Assert
        assertNotNull(newPdf);
    }

    @Test
    void testAllArgsConstructor_ShouldCreateInstanceWithAllFields() {
        // Arrange & Act
        Pdf newPdf = new Pdf(2L, "new-document", "New content");

        // Assert
        assertNotNull(newPdf);
        assertEquals(2L, newPdf.getId());
        assertEquals("new-document", newPdf.getName());
        assertEquals("New content", newPdf.getContent());
    }

    @Test
    void testBuilder_ShouldCreateInstanceWithBuilder() {
        // Arrange & Act
        Pdf newPdf = Pdf.builder()
                .id(3L)
                .name("builder-document")
                .content("Builder content")
                .build();

        // Assert
        assertNotNull(newPdf);
        assertEquals(3L, newPdf.getId());
        assertEquals("builder-document", newPdf.getName());
        assertEquals("Builder content", newPdf.getContent());
    }

    @Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Pdf newPdf = new Pdf();

        // Act
        newPdf.setId(5L);
        newPdf.setName("setter-document");
        newPdf.setContent("Setter content");

        // Assert
        assertEquals(5L, newPdf.getId());
        assertEquals("setter-document", newPdf.getName());
        assertEquals("Setter content", newPdf.getContent());
    }

    @Test
    void testSetId_ShouldUpdateId() {
        // Arrange & Act
        pdf.setId(99L);

        // Assert
        assertEquals(99L, pdf.getId());
    }

    @Test
    void testSetName_ShouldUpdateName() {
        // Arrange & Act
        pdf.setName("updated-document");

        // Assert
        assertEquals("updated-document", pdf.getName());
    }

    @Test
    void testSetContent_ShouldUpdateContent() {
        // Arrange & Act
        pdf.setContent("Updated content");

        // Assert
        assertEquals("Updated content", pdf.getContent());
    }

    @Test
    void testGetId_ShouldReturnCorrectId() {
        // Arrange & Act
        Long id = pdf.getId();

        // Assert
        assertEquals(1L, id);
    }

    @Test
    void testGetName_ShouldReturnCorrectName() {
        // Arrange & Act
        String name = pdf.getName();

        // Assert
        assertEquals("test-document", name);
    }

    @Test
    void testGetContent_ShouldReturnCorrectContent() {
        // Arrange & Act
        String content = pdf.getContent();

        // Assert
        assertEquals("This is test content", content);
    }

    @Test
    void testSetId_WithNullValue_ShouldAcceptNull() {
        // Arrange & Act
        pdf.setId(null);

        // Assert
        assertNull(pdf.getId());
    }

    @Test
    void testSetName_WithNullValue_ShouldAcceptNull() {
        // Arrange & Act
        pdf.setName(null);

        // Assert
        assertNull(pdf.getName());
    }

    @Test
    void testSetContent_WithNullValue_ShouldAcceptNull() {
        // Arrange & Act
        pdf.setContent(null);

        // Assert
        assertNull(pdf.getContent());
    }

    @Test
    void testSetName_WithEmptyString_ShouldAcceptEmptyString() {
        // Arrange & Act
        pdf.setName("");

        // Assert
        assertEquals("", pdf.getName());
    }

    @Test
    void testSetContent_WithEmptyString_ShouldAcceptEmptyString() {
        // Arrange & Act
        pdf.setContent("");

        // Assert
        assertEquals("", pdf.getContent());
    }

    @Test
    void testSetName_WithLongString_ShouldAcceptLongString() {
        // Arrange
        String longName = "document-" + "A".repeat(250);

        // Act
        pdf.setName(longName);

        // Assert
        assertEquals(longName, pdf.getName());
    }

    @Test
    void testSetContent_WithLongString_ShouldAcceptLongString() {
        // Arrange
        String longContent = "Content ".repeat(1000);

        // Act
        pdf.setContent(longContent);

        // Assert
        assertEquals(longContent, pdf.getContent());
    }

    @Test
    void testEquals_WithSameId_ShouldBeEqual() {
        // Arrange
        Pdf pdf1 = new Pdf();
        pdf1.setId(1L);
        pdf1.setName("doc1");

        Pdf pdf2 = new Pdf();
        pdf2.setId(1L);
        pdf2.setName("doc2");

        // Act & Assert
        assertEquals(pdf1, pdf2);
    }

    @Test
    void testHashCode_WithSameId_ShouldHaveSameHashCode() {
        // Arrange
        Pdf pdf1 = new Pdf();
        pdf1.setId(1L);
        pdf1.setName("doc1");

        Pdf pdf2 = new Pdf();
        pdf2.setId(1L);
        pdf2.setName("doc2");

        // Act & Assert
        assertEquals(pdf1.hashCode(), pdf2.hashCode());
    }
}

package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
        pdf.setId(1L);
        pdf.setName("test-document");
        pdf.setContent("This is the PDF content");
    }

    @Test
    void testDefaultConstructor() {
        Pdf p = new Pdf();
        assertNotNull(p);
    }

    @Test
    void testAllArgsConstructor() {
        Pdf p = new Pdf(1L, "my-pdf", "content here");
        assertNotNull(p);
        assertEquals("my-pdf", p.getName());
        assertEquals("content here", p.getContent());
    }

    @Test
    void testBuilderPattern() {
        Pdf p = Pdf.builder()
                .id(2L)
                .name("builder-pdf")
                .content("builder content")
                .build();
        assertNotNull(p);
        assertEquals("builder-pdf", p.getName());
        assertEquals("builder content", p.getContent());
    }

    @Test
    void testGetId() {
        assertEquals(1L, pdf.getId());
    }

    @Test
    void testSetAndGetName() {
        pdf.setName("new-document");
        assertEquals("new-document", pdf.getName());
    }

    @Test
    void testSetAndGetContent() {
        pdf.setContent("New content");
        assertEquals("New content", pdf.getContent());
    }

    @Test
    void testSetNameNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetContentNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testSetIdNull() {
        pdf.setId(null);
        assertNull(pdf.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        Pdf p1 = Pdf.builder().id(1L).name("doc").content("content").build();
        Pdf p2 = Pdf.builder().id(1L).name("doc").content("content").build();
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNotEquals() {
        Pdf p1 = Pdf.builder().id(1L).name("doc1").build();
        Pdf p2 = Pdf.builder().id(2L).name("doc2").build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testToString() {
        String str = pdf.toString();
        assertNotNull(str);
        assertTrue(str.contains("test-document"));
    }

    @Test
    void testNameWithPdfExtension() {
        pdf.setName("document.pdf");
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    void testLargeContent() {
        String largeContent = "A".repeat(10000);
        pdf.setContent(largeContent);
        assertEquals(largeContent, pdf.getContent());
    }
}

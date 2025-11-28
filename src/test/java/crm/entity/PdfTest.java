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
    void testPdfConstructor() {
        assertNotNull(pdf);
    }

    @Test
    void testPdfBuilder() {
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("PDF content")
                .build();

        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("Test PDF", builtPdf.getName());
        assertEquals("PDF content", builtPdf.getContent());
    }

    @Test
    void testSetAndGetId() {
        pdf.setId(1L);
        assertEquals(1L, pdf.getId());
    }

    @Test
    void testSetAndGetName() {
        pdf.setName("Document");
        assertEquals("Document", pdf.getName());
    }

    @Test
    void testSetAndGetContent() {
        pdf.setContent("This is the content");
        assertEquals("This is the content", pdf.getContent());
    }

    @Test
    void testSetAndGetNameWithNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetAndGetContentWithNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testSetAndGetNameWithEmptyString() {
        pdf.setName("");
        assertEquals("", pdf.getName());
    }

    @Test
    void testSetAndGetContentWithEmptyString() {
        pdf.setContent("");
        assertEquals("", pdf.getContent());
    }

    @Test
    void testPdfWithLongContent() {
        String longContent = "A".repeat(10000);
        pdf.setContent(longContent);
        assertEquals(longContent, pdf.getContent());
    }

    @Test
    void testPdfEqualsAndHashCode() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("Test")
                .content("Content")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("Test")
                .content("Content")
                .build();

        assertEquals(pdf1, pdf2);
        assertEquals(pdf1.hashCode(), pdf2.hashCode());
    }

    @Test
    void testPdfToString() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("Test content")
                .build();

        String toString = pdf.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test PDF"));
    }
}

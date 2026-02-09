package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PdfTest {

    private Pdf pdf;

    @BeforeEach
    public void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("test-document")
                .content("Sample PDF content")
                .build();
    }

    @Test
    public void testPdfCreation() {
        assertNotNull(pdf);
    }

    @Test
    public void testPdfBuilder() {
        Pdf newPdf = Pdf.builder()
                .id(2L)
                .name("new-document")
                .build();
        assertNotNull(newPdf);
        assertEquals(2L, newPdf.getId());
        assertEquals("new-document", newPdf.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(1L, pdf.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("test-document", pdf.getName());
    }

    @Test
    public void testGetContent() {
        assertEquals("Sample PDF content", pdf.getContent());
    }

    @Test
    public void testSetId() {
        pdf.setId(10L);
        assertEquals(10L, pdf.getId());
    }

    @Test
    public void testSetName() {
        pdf.setName("updated-document");
        assertEquals("updated-document", pdf.getName());
    }

    @Test
    public void testSetContent() {
        pdf.setContent("Updated content");
        assertEquals("Updated content", pdf.getContent());
    }

    @Test
    public void testPdfWithNullContent() {
        Pdf pdfNoContent = Pdf.builder()
                .id(3L)
                .name("no-content")
                .build();
        assertNull(pdfNoContent.getContent());
    }

    @Test
    public void testPdfEquality() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("test")
                .content("content")
                .build();
        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("test")
                .content("content")
                .build();
        assertEquals(pdf1, pdf2);
    }

    @Test
    public void testPdfToString() {
        String result = pdf.toString();
        assertNotNull(result);
        assertTrue(result.contains("test-document"));
    }
}

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
    void testPdfBuilder() {
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("TestPdf")
                .content("Test content")
                .build();

        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("TestPdf", builtPdf.getName());
        assertEquals("Test content", builtPdf.getContent());
    }

    @Test
    void testPdfSettersAndGetters() {
        pdf.setId(2L);
        pdf.setName("Sample PDF");
        pdf.setContent("Sample content here");

        assertEquals(2L, pdf.getId());
        assertEquals("Sample PDF", pdf.getName());
        assertEquals("Sample content here", pdf.getContent());
    }

    @Test
    void testPdfNoArgsConstructor() {
        Pdf newPdf = new Pdf();
        assertNotNull(newPdf);
        assertNull(newPdf.getId());
        assertNull(newPdf.getName());
        assertNull(newPdf.getContent());
    }

    @Test
    void testPdfAllArgsConstructor() {
        Pdf allArgsPdf = new Pdf(3L, "AllArgs PDF", "All args content");

        assertEquals(3L, allArgsPdf.getId());
        assertEquals("AllArgs PDF", allArgsPdf.getName());
        assertEquals("All args content", allArgsPdf.getContent());
    }

    @Test
    void testPdfWithNullValues() {
        pdf.setId(null);
        pdf.setName(null);
        pdf.setContent(null);

        assertNull(pdf.getId());
        assertNull(pdf.getName());
        assertNull(pdf.getContent());
    }

    @Test
    void testPdfNameValidation() {
        pdf.setName("AB");
        assertEquals("AB", pdf.getName());
        assertEquals(2, pdf.getName().length());
    }

    @Test
    void testPdfTransientContent() {
        pdf.setContent("This is transient content");
        assertEquals("This is transient content", pdf.getContent());
    }

    @Test
    void testPdfWithLongName() {
        String longName = "ThisIsAVeryLongPdfNameThatExceedsNormalLength";
        pdf.setName(longName);
        assertEquals(longName, pdf.getName());
    }

    @Test
    void testPdfWithEmptyContent() {
        pdf.setContent("");
        assertEquals("", pdf.getContent());
    }
}

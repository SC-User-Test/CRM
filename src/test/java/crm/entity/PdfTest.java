package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("PDF Content")
                .build();
    }

    @Test
    void testConstructor() {
        Pdf newPdf = new Pdf();
        assertNotNull(newPdf);
    }

    @Test
    void testBuilder() {
        assertNotNull(pdf);
        assertEquals("Test PDF", pdf.getName());
        assertEquals("PDF Content", pdf.getContent());
    }

    @Test
    void testGetters() {
        assertEquals(1L, pdf.getId());
        assertEquals("Test PDF", pdf.getName());
        assertEquals("PDF Content", pdf.getContent());
    }

    @Test
    void testSetters() {
        Pdf newPdf = new Pdf();
        newPdf.setId(2L);
        newPdf.setName("New PDF");
        newPdf.setContent("New Content");

        assertEquals(2L, newPdf.getId());
        assertEquals("New PDF", newPdf.getName());
        assertEquals("New Content", newPdf.getContent());
    }

    @Test
    void testSetId() {
        pdf.setId(3L);
        assertEquals(3L, pdf.getId());
    }

    @Test
    void testSetName() {
        pdf.setName("Updated PDF");
        assertEquals("Updated PDF", pdf.getName());
    }

    @Test
    void testSetContent() {
        pdf.setContent("Updated Content");
        assertEquals("Updated Content", pdf.getContent());
    }

    @Test
    void testSetName_WithNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetContent_WithNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }
}

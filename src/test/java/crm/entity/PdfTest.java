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
        assertNotNull(pdf);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        Pdf p = new Pdf(1L, "report.pdf", "PDF content here");
        assertNotNull(p);
        assertEquals(1L, p.getId());
        assertEquals("report.pdf", p.getName());
        assertEquals("PDF content here", p.getContent());
    }

    @Test
    void testBuilder_createsPdfWithAllFields() {
        Pdf p = Pdf.builder()
                .id(2L)
                .name("invoice.pdf")
                .content("Invoice content")
                .build();

        assertEquals(2L, p.getId());
        assertEquals("invoice.pdf", p.getName());
        assertEquals("Invoice content", p.getContent());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        pdf.setId(10L);
        assertEquals(10L, pdf.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        pdf.setName("document.pdf");
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    void testSetAndGetContent_returnsCorrectContent() {
        pdf.setContent("This is the PDF content");
        assertEquals("This is the PDF content", pdf.getContent());
    }

    @Test
    void testSetId_withNull_returnsNull() {
        pdf.setId(null);
        assertNull(pdf.getId());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetContent_withNull_returnsNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testSetName_withExtension_returnsNameWithExtension() {
        pdf.setName("report.pdf");
        assertTrue(pdf.getName().endsWith(".pdf"));
    }

    @Test
    void testSetName_withoutExtension_returnsNameWithoutExtension() {
        pdf.setName("report");
        assertEquals("report", pdf.getName());
    }

    @Test
    void testEquals_equalPdfs_returnsTrue() {
        Pdf p1 = Pdf.builder().id(1L).name("doc.pdf").build();
        Pdf p2 = Pdf.builder().id(1L).name("doc.pdf").build();
        assertEquals(p1, p2);
    }

    @Test
    void testEquals_differentPdfs_returnsFalse() {
        Pdf p1 = Pdf.builder().id(1L).name("doc1.pdf").build();
        Pdf p2 = Pdf.builder().id(2L).name("doc2.pdf").build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode_equalPdfs_sameHashCode() {
        Pdf p1 = Pdf.builder().id(1L).name("doc.pdf").build();
        Pdf p2 = Pdf.builder().id(1L).name("doc.pdf").build();
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString_notNull() {
        pdf.setId(1L);
        pdf.setName("test.pdf");
        assertNotNull(pdf.toString());
    }
}

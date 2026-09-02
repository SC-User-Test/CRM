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
        Pdf p = new Pdf(1L, "TestPdf", "Some content");
        assertNotNull(p);
        assertEquals(1L, p.getId());
        assertEquals("TestPdf", p.getName());
        assertEquals("Some content", p.getContent());
    }

    @Test
    void testBuilder_createsPdfWithValues() {
        Pdf p = Pdf.builder()
                .id(2L)
                .name("BuilderPdf")
                .content("Builder content")
                .build();

        assertNotNull(p);
        assertEquals(2L, p.getId());
        assertEquals("BuilderPdf", p.getName());
        assertEquals("Builder content", p.getContent());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        pdf.setId(10L);
        assertEquals(10L, pdf.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        pdf.setName("MyDocument");
        assertEquals("MyDocument", pdf.getName());
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
    void testSetName_withEmptyString_returnsEmpty() {
        pdf.setName("");
        assertEquals("", pdf.getName());
    }

    @Test
    void testEquals_equalPdfs_returnsTrue() {
        Pdf p1 = Pdf.builder().id(1L).name("Doc1").build();
        Pdf p2 = Pdf.builder().id(1L).name("Doc1").build();
        assertEquals(p1, p2);
    }

    @Test
    void testEquals_differentPdfs_returnsFalse() {
        Pdf p1 = Pdf.builder().id(1L).name("Doc1").build();
        Pdf p2 = Pdf.builder().id(2L).name("Doc2").build();
        assertNotEquals(p1, p2);
    }

    @Test
    void testHashCode_equalPdfs_sameHashCode() {
        Pdf p1 = Pdf.builder().id(1L).name("Doc1").build();
        Pdf p2 = Pdf.builder().id(1L).name("Doc1").build();
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString_notNull() {
        pdf.setId(1L);
        pdf.setName("TestPdf");
        assertNotNull(pdf.toString());
    }

    @Test
    void testSetId_withLargeValue_returnsCorrectId() {
        pdf.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, pdf.getId());
    }
}

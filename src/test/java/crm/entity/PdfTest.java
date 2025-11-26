package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PdfTest {

    private Pdf pdf;

    @BeforeEach
    public void setUp() {
        pdf = new Pdf();
    }

    @Test
    public void testConstructor() {
        Pdf newPdf = new Pdf();
        assertNotNull(newPdf);
    }

    @Test
    public void testBuilderPattern() {
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("PDF content here")
                .build();

        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("test.pdf", builtPdf.getName());
        assertEquals("PDF content here", builtPdf.getContent());
    }

    @Test
    public void testSetAndGetId() {
        pdf.setId(1L);
        assertEquals(1L, pdf.getId());
    }

    @Test
    public void testSetAndGetName() {
        pdf.setName("document.pdf");
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    public void testSetAndGetContent() {
        pdf.setContent("This is the PDF content");
        assertEquals("This is the PDF content", pdf.getContent());
    }

    @Test
    public void testNullValues() {
        pdf.setId(null);
        pdf.setName(null);
        pdf.setContent(null);

        assertNull(pdf.getId());
        assertNull(pdf.getName());
        assertNull(pdf.getContent());
    }

    @Test
    public void testEmptyName() {
        pdf.setName("");
        assertEquals("", pdf.getName());
    }

    @Test
    public void testEmptyContent() {
        pdf.setContent("");
        assertEquals("", pdf.getContent());
    }

    @Test
    public void testLongContent() {
        StringBuilder longContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longContent.append("Line ").append(i).append("\n");
        }
        pdf.setContent(longContent.toString());
        assertEquals(longContent.toString(), pdf.getContent());
    }

    @Test
    public void testNameWithExtension() {
        pdf.setName("report.pdf");
        assertTrue(pdf.getName().endsWith(".pdf"));
    }

    @Test
    public void testNameWithoutExtension() {
        pdf.setName("report");
        assertFalse(pdf.getName().endsWith(".pdf"));
    }

    @Test
    public void testAllArgsConstructor() {
        Pdf newPdf = new Pdf(2L, "invoice.pdf", "Invoice content");
        assertNotNull(newPdf);
        assertEquals(2L, newPdf.getId());
        assertEquals("invoice.pdf", newPdf.getName());
        assertEquals("Invoice content", newPdf.getContent());
    }

    @Test
    public void testNoArgsConstructor() {
        Pdf emptyPdf = new Pdf();
        assertNotNull(emptyPdf);
        assertNull(emptyPdf.getId());
        assertNull(emptyPdf.getName());
        assertNull(emptyPdf.getContent());
    }
}

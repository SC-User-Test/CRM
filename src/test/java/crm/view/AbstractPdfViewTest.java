package crm.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractPdfViewTest {

    private TestAbstractPdfView abstractPdfView;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        abstractPdfView = new TestAbstractPdfView();
    }

    @Test
    void testConstructorSetsContentType() {
        assertEquals("application/pdf", abstractPdfView.getContentType());
    }

    @Test
    void testGeneratesDownloadContent() {
        assertTrue(abstractPdfView.generatesDownloadContent());
    }

    @Test
    void testGetViewerPreferences() {
        int preferences = abstractPdfView.getViewerPreferences();
        assertEquals(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage, preferences);
    }

    @Test
    void testPrepareWriter() throws Exception {
        Map<String, Object> model = new HashMap<>();
        Document document = new Document();
        PdfWriter writer = mock(PdfWriter.class);

        assertDoesNotThrow(() -> abstractPdfView.prepareWriter(model, writer, request));
        verify(writer).setViewerPreferences(anyInt());
    }

    @Test
    void testBuildPdfMetadata() {
        Map<String, Object> model = new HashMap<>();
        Document document = new Document();

        assertDoesNotThrow(() -> abstractPdfView.buildPdfMetadata(model, document, request));
    }

    private static class TestAbstractPdfView extends AbstractPdfView {
        @Override
        protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        }
    }
}

package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    void testResolveViewNameReturnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = pdfViewResolver.resolveViewName("testView", Locale.JAPANESE);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewNameWithEmptyViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewNameAlwaysReturnsNewInstance() throws Exception {
        View view1 = pdfViewResolver.resolveViewName("view1", Locale.ENGLISH);
        View view2 = pdfViewResolver.resolveViewName("view2", Locale.ENGLISH);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotSame(view1, view2);
    }
}

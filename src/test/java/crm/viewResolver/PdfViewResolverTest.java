package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver = new PdfViewResolver();

    @Test
    void testResolveViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    void testResolveViewNameReturnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = pdfViewResolver.resolveViewName("test", Locale.FRENCH);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    void testDefaultConstructor() {
        PdfViewResolver resolver = new PdfViewResolver();
        assertNotNull(resolver);
    }
}

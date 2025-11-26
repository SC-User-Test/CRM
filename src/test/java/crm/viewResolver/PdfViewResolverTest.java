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
    void testConstructor() {
        assertNotNull(pdfViewResolver);
    }

    @Test
    void testResolveViewName_ReturnsView() throws Exception {
        View view = pdfViewResolver.resolveViewName("test", Locale.getDefault());

        assertNotNull(view);
    }

    @Test
    void testResolveViewName_ReturnsPdfView() throws Exception {
        View view = pdfViewResolver.resolveViewName("test", Locale.getDefault());

        assertTrue(view instanceof PdfView);
    }

    @Test
    void testResolveViewName_WithNullViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName(null, Locale.getDefault());

        assertNotNull(view);
    }

    @Test
    void testResolveViewName_WithDifferentLocales() throws Exception {
        View view1 = pdfViewResolver.resolveViewName("test", Locale.US);
        View view2 = pdfViewResolver.resolveViewName("test", Locale.FRANCE);

        assertNotNull(view1);
        assertNotNull(view2);
    }
}

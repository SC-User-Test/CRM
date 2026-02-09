package crm.viewResolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    public void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    public void testPdfViewResolverCreation() {
        assertNotNull(pdfViewResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocale() throws Exception {
        View view1 = pdfViewResolver.resolveViewName("view1", Locale.US);
        View view2 = pdfViewResolver.resolveViewName("view2", Locale.UK);
        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameMultipleTimes() throws Exception {
        View view1 = pdfViewResolver.resolveViewName("test1", Locale.ENGLISH);
        View view2 = pdfViewResolver.resolveViewName("test2", Locale.ENGLISH);
        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameWithEmptyViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
    }
}

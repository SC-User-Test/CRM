package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PdfViewResolverTest {

    private PdfViewResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new PdfViewResolver();
    }

    @Test
    public void testConstructor() {
        PdfViewResolver newResolver = new PdfViewResolver();
        assertNotNull(newResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = resolver.resolveViewName("testView", Locale.US);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    public void testResolveViewNameReturnsPdfView() throws Exception {
        View view = resolver.resolveViewName("pdf", Locale.getDefault());
        assertNotNull(view);
        assertEquals(PdfView.class, view.getClass());
    }

    @Test
    public void testResolveViewNameWithNullViewName() throws Exception {
        View view = resolver.resolveViewName(null, Locale.US);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithEmptyString() throws Exception {
        View view = resolver.resolveViewName("", Locale.US);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocales() throws Exception {
        View view1 = resolver.resolveViewName("test", Locale.US);
        View view2 = resolver.resolveViewName("test", Locale.CHINA);
        View view3 = resolver.resolveViewName("test", Locale.ITALY);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
    }

    @Test
    public void testResolveViewNameAlwaysReturnsPdfView() throws Exception {
        View view = resolver.resolveViewName("anyName", Locale.KOREA);
        assertTrue(view instanceof PdfView);
    }
}

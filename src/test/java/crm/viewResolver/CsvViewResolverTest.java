package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CsvViewResolverTest {

    private CsvViewResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new CsvViewResolver();
    }

    @Test
    public void testConstructor() {
        CsvViewResolver newResolver = new CsvViewResolver();
        assertNotNull(newResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = resolver.resolveViewName("testView", Locale.US);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    public void testResolveViewNameReturnsCSVView() throws Exception {
        View view = resolver.resolveViewName("csv", Locale.getDefault());
        assertNotNull(view);
        assertEquals(CsvView.class, view.getClass());
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
        View view2 = resolver.resolveViewName("test", Locale.UK);
        View view3 = resolver.resolveViewName("test", Locale.FRANCE);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
    }

    @Test
    public void testResolveViewNameAlwaysReturnsCsvView() throws Exception {
        View view = resolver.resolveViewName("anyName", Locale.CANADA);
        assertTrue(view instanceof CsvView);
    }
}

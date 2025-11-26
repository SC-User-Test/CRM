package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExcelViewResolverTest {

    private ExcelViewResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new ExcelViewResolver();
    }

    @Test
    public void testConstructor() {
        ExcelViewResolver newResolver = new ExcelViewResolver();
        assertNotNull(newResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = resolver.resolveViewName("testView", Locale.US);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    public void testResolveViewNameReturnsExcelView() throws Exception {
        View view = resolver.resolveViewName("excel", Locale.getDefault());
        assertNotNull(view);
        assertEquals(ExcelView.class, view.getClass());
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
        View view3 = resolver.resolveViewName("test", Locale.GERMANY);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
    }

    @Test
    public void testResolveViewNameAlwaysReturnsExcelView() throws Exception {
        View view = resolver.resolveViewName("anyName", Locale.JAPAN);
        assertTrue(view instanceof ExcelView);
    }
}

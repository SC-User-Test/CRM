package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    void testResolveViewNameReturnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = csvViewResolver.resolveViewName("testView", Locale.GERMAN);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithEmptyViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameAlwaysReturnsNewInstance() throws Exception {
        View view1 = csvViewResolver.resolveViewName("view1", Locale.ENGLISH);
        View view2 = csvViewResolver.resolveViewName("view2", Locale.ENGLISH);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotSame(view1, view2);
    }
}

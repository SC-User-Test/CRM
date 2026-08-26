package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver = new CsvViewResolver();

    @Test
    void testResolveViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    void testResolveViewNameReturnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = csvViewResolver.resolveViewName("test", Locale.ITALIAN);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    void testDefaultConstructor() {
        CsvViewResolver resolver = new CsvViewResolver();
        assertNotNull(resolver);
    }
}

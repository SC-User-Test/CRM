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
    void testDefaultConstructor_createsInstance() {
        assertNotNull(csvViewResolver);
    }

    @Test
    void testResolveViewName_returnsView() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("users", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("test", Locale.FRENCH);
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }
}

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
    void testConstructor() {
        assertNotNull(csvViewResolver);
    }

    @Test
    void testResolveViewName_ReturnsView() throws Exception {
        View view = csvViewResolver.resolveViewName("test", Locale.getDefault());

        assertNotNull(view);
    }

    @Test
    void testResolveViewName_ReturnsCsvView() throws Exception {
        View view = csvViewResolver.resolveViewName("test", Locale.getDefault());

        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewName_WithNullViewName() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.getDefault());

        assertNotNull(view);
    }

    @Test
    void testResolveViewName_WithDifferentLocales() throws Exception {
        View view1 = csvViewResolver.resolveViewName("test", Locale.US);
        View view2 = csvViewResolver.resolveViewName("test", Locale.FRANCE);

        assertNotNull(view1);
        assertNotNull(view2);
    }
}

package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver = new ExcelViewResolver();

    @Test
    void testResolveViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    void testResolveViewNameReturnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = excelViewResolver.resolveViewName("test", Locale.GERMAN);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    void testDefaultConstructor() {
        ExcelViewResolver resolver = new ExcelViewResolver();
        assertNotNull(resolver);
    }
}

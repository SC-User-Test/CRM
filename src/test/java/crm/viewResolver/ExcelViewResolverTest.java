package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    void testResolveViewNameReturnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = excelViewResolver.resolveViewName("testView", Locale.FRENCH);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithEmptyViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewNameAlwaysReturnsNewInstance() throws Exception {
        View view1 = excelViewResolver.resolveViewName("view1", Locale.ENGLISH);
        View view2 = excelViewResolver.resolveViewName("view2", Locale.ENGLISH);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotSame(view1, view2);
    }
}

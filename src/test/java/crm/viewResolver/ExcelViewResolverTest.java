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
    void testDefaultConstructor_createsInstance() {
        assertNotNull(excelViewResolver);
    }

    @Test
    void testResolveViewName_returnsView() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("anyView", Locale.getDefault());
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("users", Locale.ENGLISH);
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("test", Locale.GERMAN);
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }
}

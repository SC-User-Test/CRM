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
    void testConstructor() {
        assertNotNull(excelViewResolver);
    }

    @Test
    void testResolveViewName_ReturnsView() throws Exception {
        View view = excelViewResolver.resolveViewName("test", Locale.getDefault());

        assertNotNull(view);
    }

    @Test
    void testResolveViewName_ReturnsExcelView() throws Exception {
        View view = excelViewResolver.resolveViewName("test", Locale.getDefault());

        assertTrue(view instanceof ExcelView);
    }

    @Test
    void testResolveViewName_WithNullViewName() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.getDefault());

        assertNotNull(view);
    }

    @Test
    void testResolveViewName_WithDifferentLocales() throws Exception {
        View view1 = excelViewResolver.resolveViewName("test", Locale.US);
        View view2 = excelViewResolver.resolveViewName("test", Locale.FRANCE);

        assertNotNull(view1);
        assertNotNull(view2);
    }
}

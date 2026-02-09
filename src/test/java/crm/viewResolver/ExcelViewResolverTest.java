package crm.viewResolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    public void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    public void testExcelViewResolverCreation() {
        assertNotNull(excelViewResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocale() throws Exception {
        View view1 = excelViewResolver.resolveViewName("view1", Locale.US);
        View view2 = excelViewResolver.resolveViewName("view2", Locale.UK);
        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameMultipleTimes() throws Exception {
        View view1 = excelViewResolver.resolveViewName("test1", Locale.ENGLISH);
        View view2 = excelViewResolver.resolveViewName("test2", Locale.ENGLISH);
        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameWithEmptyViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
    }
}

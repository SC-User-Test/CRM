package crm.viewResolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    public void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    public void testCsvViewResolverCreation() {
        assertNotNull(csvViewResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocale() throws Exception {
        View view1 = csvViewResolver.resolveViewName("view1", Locale.US);
        View view2 = csvViewResolver.resolveViewName("view2", Locale.UK);
        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameMultipleTimes() throws Exception {
        View view1 = csvViewResolver.resolveViewName("test1", Locale.ENGLISH);
        View view2 = csvViewResolver.resolveViewName("test2", Locale.ENGLISH);
        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameWithEmptyViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
    }
}

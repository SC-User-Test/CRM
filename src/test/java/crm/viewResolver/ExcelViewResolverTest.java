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
    void testConstructor_createsInstance() {
        // Arrange & Act
        ExcelViewResolver resolver = new ExcelViewResolver();
        // Assert
        assertNotNull(resolver);
    }

    @Test
    void testResolveViewName_returnsNonNullView() throws Exception {
        // Arrange & Act
        View view = excelViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        // Assert
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsExcelView() throws Exception {
        // Arrange & Act
        View view = excelViewResolver.resolveViewName("download", Locale.ENGLISH);
        // Assert
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsExcelView() throws Exception {
        // Arrange & Act
        View view = excelViewResolver.resolveViewName("download", Locale.GERMAN);
        // Assert
        assertNotNull(view);
        assertInstanceOf(ExcelView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsExcelView() throws Exception {
        // Arrange & Act
        View view = excelViewResolver.resolveViewName(null, Locale.ENGLISH);
        // Assert
        assertNotNull(view);
    }
}

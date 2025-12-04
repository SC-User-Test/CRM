package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        ExcelViewResolver resolver = new ExcelViewResolver();

        // Assert
        assertNotNull(resolver);
    }

    @Test
    void testResolveViewName_ShouldReturnExcelView() throws Exception {
        // Arrange & Act
        View result = excelViewResolver.resolveViewName("test", Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertInstanceOf(ExcelView.class, result);
    }

    @Test
    void testResolveViewName_WithNullViewName_ShouldReturnExcelView() throws Exception {
        // Arrange & Act
        View result = excelViewResolver.resolveViewName(null, Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertInstanceOf(ExcelView.class, result);
    }

    @Test
    void testResolveViewName_WithDifferentLocale_ShouldReturnExcelView() throws Exception {
        // Arrange & Act
        View result = excelViewResolver.resolveViewName("test", Locale.GERMAN);

        // Assert
        assertNotNull(result);
        assertInstanceOf(ExcelView.class, result);
    }

    @Test
    void testResolveViewName_MultipleCallsShouldReturnNewInstances() throws Exception {
        // Arrange & Act
        View result1 = excelViewResolver.resolveViewName("test1", Locale.getDefault());
        View result2 = excelViewResolver.resolveViewName("test2", Locale.getDefault());

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }
}

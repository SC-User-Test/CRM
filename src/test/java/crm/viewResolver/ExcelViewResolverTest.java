package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    void testExcelViewResolverCreation() {
        // Assert
        assertNotNull(excelViewResolver);
    }

    @Test
    void testExcelViewResolverImplementsViewResolver() {
        // Assert
        assertTrue(ViewResolver.class.isAssignableFrom(ExcelViewResolver.class));
    }

    @Test
    void testExcelViewResolverIsInstantiable() {
        // Act
        ExcelViewResolver resolver = new ExcelViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ViewResolver.class, resolver);
    }
}

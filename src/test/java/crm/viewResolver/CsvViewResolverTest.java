package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    void testCsvViewResolverCreation() {
        // Assert
        assertNotNull(csvViewResolver);
    }

    @Test
    void testCsvViewResolverImplementsViewResolver() {
        // Assert
        assertTrue(ViewResolver.class.isAssignableFrom(CsvViewResolver.class));
    }

    @Test
    void testCsvViewResolverIsInstantiable() {
        // Act
        CsvViewResolver resolver = new CsvViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ViewResolver.class, resolver);
    }
}

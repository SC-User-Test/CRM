package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    void resolveViewName_shouldReturnCsvView() throws Exception {
        // Act
        View result = csvViewResolver.resolveViewName("test", Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void resolveViewName_withDifferentLocale_shouldReturnCsvView() throws Exception {
        // Act
        View result = csvViewResolver.resolveViewName("test", Locale.ITALIAN);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void resolveViewName_withNullViewName_shouldReturnCsvView() throws Exception {
        // Act
        View result = csvViewResolver.resolveViewName(null, Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void csvViewResolver_shouldBeInstantiable() {
        // Assert
        assertNotNull(csvViewResolver);
    }
}

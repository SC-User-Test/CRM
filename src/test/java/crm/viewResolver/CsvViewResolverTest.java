package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testConstructor_createsInstance() {
        // Arrange & Act
        CsvViewResolver resolver = new CsvViewResolver();
        // Assert
        assertNotNull(resolver);
    }

    @Test
    void testResolveViewName_returnsNonNullView() throws Exception {
        // Arrange & Act
        View view = csvViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        // Assert
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsCsvView() throws Exception {
        // Arrange & Act
        View view = csvViewResolver.resolveViewName("download", Locale.ENGLISH);
        // Assert
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsCsvView() throws Exception {
        // Arrange & Act
        View view = csvViewResolver.resolveViewName("download", Locale.FRENCH);
        // Assert
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsCsvView() throws Exception {
        // Arrange & Act
        View view = csvViewResolver.resolveViewName(null, Locale.ENGLISH);
        // Assert
        assertNotNull(view);
    }
}

package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        CsvViewResolver resolver = new CsvViewResolver();

        // Assert
        assertNotNull(resolver);
    }

    @Test
    void testResolveViewName_ShouldReturnCsvView() throws Exception {
        // Arrange & Act
        View result = csvViewResolver.resolveViewName("test", Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertInstanceOf(CsvView.class, result);
    }

    @Test
    void testResolveViewName_WithNullViewName_ShouldReturnCsvView() throws Exception {
        // Arrange & Act
        View result = csvViewResolver.resolveViewName(null, Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertInstanceOf(CsvView.class, result);
    }

    @Test
    void testResolveViewName_WithDifferentLocale_ShouldReturnCsvView() throws Exception {
        // Arrange & Act
        View result = csvViewResolver.resolveViewName("test", Locale.FRENCH);

        // Assert
        assertNotNull(result);
        assertInstanceOf(CsvView.class, result);
    }

    @Test
    void testResolveViewName_MultipleCallsShouldReturnNewInstances() throws Exception {
        // Arrange & Act
        View result1 = csvViewResolver.resolveViewName("test1", Locale.getDefault());
        View result2 = csvViewResolver.resolveViewName("test2", Locale.getDefault());

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }
}

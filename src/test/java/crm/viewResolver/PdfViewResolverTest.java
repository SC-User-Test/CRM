package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        PdfViewResolver resolver = new PdfViewResolver();

        // Assert
        assertNotNull(resolver);
    }

    @Test
    void testResolveViewName_ShouldReturnPdfView() throws Exception {
        // Arrange & Act
        View result = pdfViewResolver.resolveViewName("test", Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertInstanceOf(PdfView.class, result);
    }

    @Test
    void testResolveViewName_WithNullViewName_ShouldReturnPdfView() throws Exception {
        // Arrange & Act
        View result = pdfViewResolver.resolveViewName(null, Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertInstanceOf(PdfView.class, result);
    }

    @Test
    void testResolveViewName_WithDifferentLocale_ShouldReturnPdfView() throws Exception {
        // Arrange & Act
        View result = pdfViewResolver.resolveViewName("test", Locale.ITALIAN);

        // Assert
        assertNotNull(result);
        assertInstanceOf(PdfView.class, result);
    }

    @Test
    void testResolveViewName_MultipleCallsShouldReturnNewInstances() throws Exception {
        // Arrange & Act
        View result1 = pdfViewResolver.resolveViewName("test1", Locale.getDefault());
        View result2 = pdfViewResolver.resolveViewName("test2", Locale.getDefault());

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2);
    }
}

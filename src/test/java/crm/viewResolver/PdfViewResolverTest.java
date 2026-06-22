package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    void testPdfViewResolverCreation() {
        // Assert
        assertNotNull(pdfViewResolver);
    }

    @Test
    void testPdfViewResolverImplementsViewResolver() {
        // Assert
        assertTrue(ViewResolver.class.isAssignableFrom(PdfViewResolver.class));
    }

    @Test
    void testPdfViewResolverIsInstantiable() {
        // Act
        PdfViewResolver resolver = new PdfViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ViewResolver.class, resolver);
    }
}

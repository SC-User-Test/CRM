package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        PdfViewResolver resolver = new PdfViewResolver();
        // Assert
        assertNotNull(resolver);
    }

    @Test
    void testResolveViewName_returnsNonNullView() throws Exception {
        // Arrange & Act
        View view = pdfViewResolver.resolveViewName("anyView", Locale.ENGLISH);
        // Assert
        assertNotNull(view);
    }

    @Test
    void testResolveViewName_returnsPdfView() throws Exception {
        // Arrange & Act
        View view = pdfViewResolver.resolveViewName("download", Locale.ENGLISH);
        // Assert
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_withDifferentLocale_returnsPdfView() throws Exception {
        // Arrange & Act
        View view = pdfViewResolver.resolveViewName("download", Locale.ITALIAN);
        // Assert
        assertNotNull(view);
        assertInstanceOf(PdfView.class, view);
    }

    @Test
    void testResolveViewName_withNullViewName_returnsPdfView() throws Exception {
        // Arrange & Act
        View view = pdfViewResolver.resolveViewName(null, Locale.ENGLISH);
        // Assert
        assertNotNull(view);
    }
}

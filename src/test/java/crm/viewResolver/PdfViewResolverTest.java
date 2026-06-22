package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    void resolveViewName_shouldReturnPdfView() throws Exception {
        // Act
        View result = pdfViewResolver.resolveViewName("test", Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof PdfView);
    }

    @Test
    void resolveViewName_withDifferentLocale_shouldReturnPdfView() throws Exception {
        // Act
        View result = pdfViewResolver.resolveViewName("test", Locale.FRENCH);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof PdfView);
    }

    @Test
    void resolveViewName_withNullViewName_shouldReturnPdfView() throws Exception {
        // Act
        View result = pdfViewResolver.resolveViewName(null, Locale.getDefault());

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof PdfView);
    }

    @Test
    void pdfViewResolver_shouldBeInstantiable() {
        // Assert
        assertNotNull(pdfViewResolver);
    }
}

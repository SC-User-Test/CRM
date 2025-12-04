package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.jupiter.api.Assertions.*;

class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testExcelViewResolver() {
        ViewResolver resolver = webAppConfig.excelViewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof ExcelViewResolver);
    }

    @Test
    void testCsvViewResolver() {
        ViewResolver resolver = webAppConfig.csvViewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof CsvViewResolver);
    }

    @Test
    void testPdfViewResolver() {
        ViewResolver resolver = webAppConfig.pdfViewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof PdfViewResolver);
    }

    @Test
    void testTemplateResolver() {
        assertDoesNotThrow(() -> {
            webAppConfig.templateResolver();
        });
    }

    @Test
    void testTemplateEngine() {
        assertDoesNotThrow(() -> {
            webAppConfig.templateEngine();
        });
    }

    @Test
    void testViewResolver() {
        assertDoesNotThrow(() -> {
            webAppConfig.viewResolver();
        });
    }

    @Test
    void testWebAppConfigIsAnnotatedWithConfiguration() {
        assertTrue(webAppConfig.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }
}

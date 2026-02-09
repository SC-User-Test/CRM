package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;

public class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @BeforeEach
    public void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    public void testWebAppConfigCreation() {
        assertNotNull(webAppConfig);
    }

    @Test
    public void testTemplateResolver() {
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testTemplateEngine() {
        ClassLoaderTemplateResolver templateResolver = webAppConfig.templateResolver();
        SpringTemplateEngine engine = webAppConfig.templateEngine(templateResolver);
        assertNotNull(engine);
    }

    @Test
    public void testViewResolver() {
        ViewResolver viewResolver = webAppConfig.viewResolver();
        assertNotNull(viewResolver);
    }

    @Test
    public void testExcelViewResolver() {
        ViewResolver excelResolver = webAppConfig.excelViewResolver();
        assertNotNull(excelResolver);
        assertTrue(excelResolver instanceof ExcelViewResolver);
    }

    @Test
    public void testCsvViewResolver() {
        ViewResolver csvResolver = webAppConfig.csvViewResolver();
        assertNotNull(csvResolver);
        assertTrue(csvResolver instanceof CsvViewResolver);
    }

    @Test
    public void testPdfViewResolver() {
        ViewResolver pdfResolver = webAppConfig.pdfViewResolver();
        assertNotNull(pdfResolver);
        assertTrue(pdfResolver instanceof PdfViewResolver);
    }

    @Test
    public void testTemplateResolverConfiguration() {
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();
        assertNotNull(resolver);
        assertFalse(resolver.isCacheable());
    }

    @Test
    public void testMultipleViewResolverCalls() {
        ViewResolver resolver1 = webAppConfig.excelViewResolver();
        ViewResolver resolver2 = webAppConfig.csvViewResolver();
        ViewResolver resolver3 = webAppConfig.pdfViewResolver();
        assertNotNull(resolver1);
        assertNotNull(resolver2);
        assertNotNull(resolver3);
    }
}

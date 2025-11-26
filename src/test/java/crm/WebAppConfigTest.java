package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testConstructor() {
        assertNotNull(webAppConfig);
    }

    @Test
    void testAddViewControllers() {
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
        when(registry.addViewController(anyString())).thenReturn(mock(org.springframework.web.servlet.config.annotation.ViewControllerRegistration.class));

        assertDoesNotThrow(() -> webAppConfig.addViewControllers(registry));
    }

    @Test
    void testConfigureContentNegotiation() {
        ContentNegotiationConfigurer configurer = mock(ContentNegotiationConfigurer.class);
        when(configurer.favorParameter(anyBoolean())).thenReturn(configurer);
        when(configurer.parameterName(anyString())).thenReturn(configurer);
        when(configurer.ignoreAcceptHeader(anyBoolean())).thenReturn(configurer);
        when(configurer.defaultContentType(any())).thenReturn(configurer);
        when(configurer.mediaTypes(anyMap())).thenReturn(configurer);

        assertDoesNotThrow(() -> webAppConfig.configureContentNegotiation(configurer));
    }

    @Test
    void testContentNegotiatingViewResolver() {
        ContentNegotiationManager manager = mock(ContentNegotiationManager.class);

        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);

        assertNotNull(resolver);
    }

    @Test
    void testTemplateResolver() {
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        assertNotNull(resolver);
    }

    @Test
    void testTemplateEngine() {
        SpringTemplateEngine engine = webAppConfig.templateEngine();

        assertNotNull(engine);
    }

    @Test
    void testViewResolver() {
        ViewResolver resolver = webAppConfig.viewResolver();

        assertNotNull(resolver);
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
}

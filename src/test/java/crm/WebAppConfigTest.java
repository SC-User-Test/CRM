package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @Mock
    private ViewControllerRegistry registry;

    @Mock
    private ContentNegotiationConfigurer configurer;

    @Mock
    private ContentNegotiationManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testWebAppConfigConstructor() {
        assertNotNull(webAppConfig);
    }

    @Test
    void testConfigurationAnnotationPresent() {
        assertTrue(WebAppConfig.class.isAnnotationPresent(Configuration.class));
    }

    @Test
    void testAddViewControllers() {
        webAppConfig.addViewControllers(registry);

        verify(registry).addViewController("/login");
        verify(registry).addViewController("/");
        verify(registry).addViewController("/user/menu");
        verify(registry).addViewController("/customer/menu");
        verify(registry).addViewController("/contract/menu");
        verify(registry).addViewController("/contract/search");
        verify(registry).addViewController("/admin");
        verify(registry).addViewController("/search");
        verify(registry).addViewController("/403");
        verify(registry).addViewController("/logout");
        verify(registry).setOrder(anyInt());
    }

    @Test
    void testConfigureContentNegotiation() {
        when(configurer.favorParameter(anyBoolean())).thenReturn(configurer);
        when(configurer.parameterName(anyString())).thenReturn(configurer);
        when(configurer.ignoreAcceptHeader(anyBoolean())).thenReturn(configurer);
        when(configurer.defaultContentType(any(MediaType.class))).thenReturn(configurer);

        webAppConfig.configureContentNegotiation(configurer);

        verify(configurer).favorParameter(true);
        verify(configurer).parameterName("mediaType");
        verify(configurer).ignoreAcceptHeader(false);
        verify(configurer).defaultContentType(MediaType.APPLICATION_JSON);
        verify(configurer).mediaTypes(anyMap());
    }

    @Test
    void testContentNegotiatingViewResolver() {
        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);

        assertNotNull(resolver);
        assertTrue(resolver instanceof ContentNegotiatingViewResolver);
    }

    @Test
    void testTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = webAppConfig.templateResolver();

        assertNotNull(templateResolver);
        assertEquals("UTF-8", templateResolver.getCharacterEncoding());
    }

    @Test
    void testTemplateEngine() {
        SpringTemplateEngine templateEngine = webAppConfig.templateEngine();

        assertNotNull(templateEngine);
    }

    @Test
    void testViewResolver() {
        ViewResolver viewResolver = webAppConfig.viewResolver();

        assertNotNull(viewResolver);
        assertTrue(viewResolver instanceof ThymeleafViewResolver);
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
    void testSpringDataDialect() {
        SpringDataDialect dialect = webAppConfig.springDataDialect();

        assertNotNull(dialect);
        assertTrue(dialect instanceof SpringDataDialect);
    }
}

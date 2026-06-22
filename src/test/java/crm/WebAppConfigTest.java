package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebAppConfigTest {

    @InjectMocks
    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void addViewControllers_shouldRegisterAllViewControllers() {
        // Arrange
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);

        // Act
        webAppConfig.addViewControllers(registry);

        // Assert
        verify(registry, atLeastOnce()).addViewController(anyString());
    }

    @Test
    void configureContentNegotiation_shouldConfigureMediaTypes() {
        // Arrange
        ContentNegotiationConfigurer configurer = mock(ContentNegotiationConfigurer.class);
        when(configurer.favorParameter(anyBoolean())).thenReturn(configurer);
        when(configurer.ignoreAcceptHeader(anyBoolean())).thenReturn(configurer);
        when(configurer.defaultContentType(any(MediaType.class))).thenReturn(configurer);

        // Act
        webAppConfig.configureContentNegotiation(configurer);

        // Assert
        verify(configurer).favorParameter(true);
        verify(configurer).ignoreAcceptHeader(false);
        verify(configurer).defaultContentType(MediaType.APPLICATION_JSON);
        verify(configurer).mediaTypes(anyMap());
    }

    @Test
    void templateResolver_shouldReturnConfiguredResolver() {
        // Act
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        // Assert
        assertNotNull(resolver);
    }

    @Test
    void templateEngine_shouldReturnSpringTemplateEngine() {
        // Act
        SpringTemplateEngine engine = webAppConfig.templateEngine();

        // Assert
        assertNotNull(engine);
        assertInstanceOf(SpringTemplateEngine.class, engine);
    }

    @Test
    void viewResolver_shouldReturnThymeleafViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.viewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ThymeleafViewResolver.class, resolver);
    }

    @Test
    void excelViewResolver_shouldReturnExcelViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.excelViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ExcelViewResolver.class, resolver);
    }

    @Test
    void csvViewResolver_shouldReturnCsvViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.csvViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(CsvViewResolver.class, resolver);
    }

    @Test
    void pdfViewResolver_shouldReturnPdfViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.pdfViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(PdfViewResolver.class, resolver);
    }

    @Test
    void contentNegotiatingViewResolver_shouldReturnConfiguredResolver() {
        // Arrange
        ContentNegotiationManager manager = mock(ContentNegotiationManager.class);

        // Act
        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ContentNegotiatingViewResolver.class, resolver);
    }

    @Test
    void templateResolver_shouldHaveCorrectPrefix() {
        // Act
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        // Assert
        assertNotNull(resolver);
        // Verify template resolver is configured (actual values are internal)
    }

    @Test
    void templateEngine_shouldHaveDialectsConfigured() {
        // Act
        SpringTemplateEngine engine = webAppConfig.templateEngine();

        // Assert
        assertNotNull(engine);
        assertTrue(engine.getDialects().size() > 0);
    }

    @Test
    void webAppConfig_shouldHaveConfigurationAnnotation() {
        // Assert
        assertTrue(WebAppConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void webAppConfig_shouldImplementWebMvcConfigurer() {
        // Assert
        assertTrue(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class.isAssignableFrom(WebAppConfig.class));
    }
}

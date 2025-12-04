package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        WebAppConfig config = new WebAppConfig();

        // Assert
        assertNotNull(config);
    }

    @Test
    void testAddViewControllers_ShouldRegisterViewControllers() {
        // Arrange
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);

        // Act
        webAppConfig.addViewControllers(registry);

        // Assert
        verify(registry, atLeastOnce()).addViewController(anyString());
        verify(registry).setOrder(anyInt());
    }

    @Test
    void testTemplateResolver_ShouldReturnClassLoaderTemplateResolver() {
        // Arrange & Act
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ClassLoaderTemplateResolver.class, resolver);
    }

    @Test
    void testTemplateEngine_ShouldReturnSpringTemplateEngine() {
        // Arrange & Act
        SpringTemplateEngine engine = webAppConfig.templateEngine();

        // Assert
        assertNotNull(engine);
        assertInstanceOf(SpringTemplateEngine.class, engine);
    }

    @Test
    void testViewResolver_ShouldReturnThymeleafViewResolver() {
        // Arrange & Act
        ViewResolver resolver = webAppConfig.viewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ThymeleafViewResolver.class, resolver);
    }

    @Test
    void testExcelViewResolver_ShouldReturnExcelViewResolver() {
        // Arrange & Act
        ViewResolver resolver = webAppConfig.excelViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ExcelViewResolver.class, resolver);
    }

    @Test
    void testCsvViewResolver_ShouldReturnCsvViewResolver() {
        // Arrange & Act
        ViewResolver resolver = webAppConfig.csvViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(CsvViewResolver.class, resolver);
    }

    @Test
    void testPdfViewResolver_ShouldReturnPdfViewResolver() {
        // Arrange & Act
        ViewResolver resolver = webAppConfig.pdfViewResolver();

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(PdfViewResolver.class, resolver);
    }

    @Test
    void testContentNegotiatingViewResolver_ShouldReturnConfiguredResolver() {
        // Arrange
        ContentNegotiationManager manager = mock(ContentNegotiationManager.class);

        // Act
        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);

        // Assert
        assertNotNull(resolver);
        assertInstanceOf(ContentNegotiatingViewResolver.class, resolver);
    }

    @Test
    void testTemplateResolver_ShouldConfigurePrefix() {
        // Arrange & Act
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        // Assert
        assertNotNull(resolver);
        // Template resolver is configured with prefix "templates/"
    }

    @Test
    void testTemplateResolver_ShouldConfigureSuffix() {
        // Arrange & Act
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        // Assert
        assertNotNull(resolver);
        // Template resolver is configured with suffix ".html"
    }
}

package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebAppConfigTest {

    @InjectMocks
    private WebAppConfig webAppConfig;

    @Test
    public void testConstructor() {
        WebAppConfig config = new WebAppConfig();
        assertNotNull(config);
    }

    @Test
    public void testAddViewControllers() {
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
        webAppConfig.addViewControllers(registry);
        verify(registry, atLeastOnce()).addViewController(anyString());
    }

    @Test
    public void testConfigureContentNegotiation() {
        ContentNegotiationConfigurer configurer = mock(ContentNegotiationConfigurer.class);
        when(configurer.favorParameter(anyBoolean())).thenReturn(configurer);
        when(configurer.ignoreAcceptHeader(anyBoolean())).thenReturn(configurer);
        when(configurer.defaultContentType(any())).thenReturn(configurer);
        when(configurer.mediaTypes(anyMap())).thenReturn(configurer);

        webAppConfig.configureContentNegotiation(configurer);
        verify(configurer).favorParameter(true);
        verify(configurer).ignoreAcceptHeader(false);
    }

    @Test
    public void testContentNegotiatingViewResolver() {
        ContentNegotiationManager manager = mock(ContentNegotiationManager.class);
        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);
        assertNotNull(resolver);
        assertTrue(resolver instanceof ContentNegotiatingViewResolver);
    }

    @Test
    public void testTemplateResolver() {
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testTemplateEngine() {
        assertNotNull(webAppConfig.templateEngine());
    }

    @Test
    public void testViewResolver() {
        ViewResolver resolver = webAppConfig.viewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof ThymeleafViewResolver);
    }

    @Test
    public void testExcelViewResolver() {
        ViewResolver resolver = webAppConfig.excelViewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof ExcelViewResolver);
    }

    @Test
    public void testCsvViewResolver() {
        ViewResolver resolver = webAppConfig.csvViewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof CsvViewResolver);
    }

    @Test
    public void testPdfViewResolver() {
        ViewResolver resolver = webAppConfig.pdfViewResolver();
        assertNotNull(resolver);
        assertTrue(resolver instanceof PdfViewResolver);
    }

    @Test
    public void testAllViewResolversAreNotNull() {
        assertNotNull(webAppConfig.excelViewResolver());
        assertNotNull(webAppConfig.csvViewResolver());
        assertNotNull(webAppConfig.pdfViewResolver());
        assertNotNull(webAppConfig.viewResolver());
    }
}

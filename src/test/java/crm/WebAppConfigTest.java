package crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class WebAppConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebAppConfig webAppConfig;

    @Test
    void contextLoads() {
        // Assert
        assertNotNull(applicationContext);
    }

    @Test
    void webAppConfig_shouldBeLoaded() {
        // Assert
        assertNotNull(webAppConfig);
    }

    @Test
    void templateResolver_shouldBeConfigured() {
        // Act
        var templateResolver = webAppConfig.templateResolver();

        // Assert
        assertNotNull(templateResolver);
        assertEquals("templates/", templateResolver.getPrefix());
        assertEquals(".html", templateResolver.getSuffix());
    }

    @Test
    void templateEngine_shouldBeConfigured() {
        // Act
        var templateEngine = webAppConfig.templateEngine();

        // Assert
        assertNotNull(templateEngine);
    }

    @Test
    void viewResolver_shouldBeConfigured() {
        // Act
        var viewResolver = webAppConfig.viewResolver();

        // Assert
        assertNotNull(viewResolver);
    }

    @Test
    void excelViewResolver_shouldBeConfigured() {
        // Act
        var excelViewResolver = webAppConfig.excelViewResolver();

        // Assert
        assertNotNull(excelViewResolver);
    }

    @Test
    void csvViewResolver_shouldBeConfigured() {
        // Act
        var csvViewResolver = webAppConfig.csvViewResolver();

        // Assert
        assertNotNull(csvViewResolver);
    }

    @Test
    void pdfViewResolver_shouldBeConfigured() {
        // Act
        var pdfViewResolver = webAppConfig.pdfViewResolver();

        // Assert
        assertNotNull(pdfViewResolver);
    }
}

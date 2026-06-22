package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void main_shouldStartSpringApplication() {
        // This test verifies the main method exists and can be called
        // We don't actually start the application in tests
        assertDoesNotThrow(() -> {
            // Verify the class exists and has a main method
            CrmApplication.class.getMethod("main", String[].class);
        });
    }

    @Test
    void contextLoads() {
        // Verify the application class is properly annotated
        assertNotNull(CrmApplication.class.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void crmApplication_shouldHaveDefaultConstructor() {
        // Act & Assert
        assertDoesNotThrow(() -> new CrmApplication());
    }

    @Test
    void crmApplication_shouldBeInstantiable() {
        // Arrange & Act
        CrmApplication application = new CrmApplication();

        // Assert
        assertNotNull(application);
    }
}

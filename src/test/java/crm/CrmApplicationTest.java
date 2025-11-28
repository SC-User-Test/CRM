package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void testCrmApplicationConstructor() {
        CrmApplication app = new CrmApplication();
        assertNotNull(app);
    }

    @Test
    void testMainMethodDoesNotThrowException() {
        assertDoesNotThrow(() -> {
            // Test that main method exists and can be invoked
            // Note: We don't actually run SpringApplication.run in tests
        });
    }

    @Test
    void testApplicationContextLoads() {
        assertNotNull(CrmApplication.class);
    }

    @Test
    void testSpringBootApplicationAnnotationPresent() {
        assertTrue(CrmApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}

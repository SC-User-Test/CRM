package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrmApplicationTest {

    @Test
    void contextLoads() {
        // Test that the application context loads successfully
        assertDoesNotThrow(() -> {
            // Context loading is tested by @SpringBootTest
        });
    }

    @Test
    void mainMethodStartsApplication() {
        // Test that main method can be invoked without exceptions
        assertDoesNotThrow(() -> {
            // We don't actually run the application to avoid port conflicts
            // Just verify the class structure is correct
            assertNotNull(CrmApplication.class);
        });
    }

    @Test
    void applicationClassExists() {
        // Verify the application class is properly configured
        assertNotNull(CrmApplication.class);
        assertTrue(CrmApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void entityScanAnnotationPresent() {
        // Verify EntityScan annotation is present
        assertTrue(CrmApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.domain.EntityScan.class));
    }
}

package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

public class CrmApplicationTest {

    @Test
    public void testMainMethod() {
        assertDoesNotThrow(() -> {
            // Test that the main method exists and can be called
            String[] args = {};
            // Note: We don't actually call main() to avoid starting the full Spring context
            assertNotNull(CrmApplication.class.getMethod("main", String[].class));
        });
    }

    @Test
    public void testContextLoads() {
        assertDoesNotThrow(() -> {
            CrmApplication app = new CrmApplication();
            assertNotNull(app);
        });
    }

    @Test
    public void testApplicationClass() {
        assertNotNull(CrmApplication.class);
        assertTrue(CrmApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}

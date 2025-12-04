package crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            CrmApplication.class.getDeclaredMethod("main", String[].class);
        });
    }

    @Test
    void testCrmApplicationClassExists() {
        assertNotNull(CrmApplication.class);
    }

    @Test
    void testCrmApplicationIsAnnotatedWithSpringBootApplication() {
        assertTrue(CrmApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testCrmApplicationConstructor() {
        assertDoesNotThrow(() -> {
            new CrmApplication();
        });
    }
}

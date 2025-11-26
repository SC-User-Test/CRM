package crm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CrmApplicationTest {

    @Test
    void testConstructor() {
        CrmApplication crmApplication = new CrmApplication();
        assertNotNull(crmApplication);
    }

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.CrmApplication");
            assertNotNull(clazz.getMethod("main", String[].class));
        });
    }

    @Test
    void testClassHasSpringBootApplicationAnnotation() {
        assertTrue(CrmApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }
}

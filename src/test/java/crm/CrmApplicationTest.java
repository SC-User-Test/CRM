package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrmApplicationTest {

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        CrmApplication application = new CrmApplication();

        // Assert
        assertNotNull(application);
    }

    @Test
    void testMain_ShouldHaveMainMethod() throws Exception {
        // Arrange & Act
        var mainMethod = CrmApplication.class.getMethod("main", String[].class);

        // Assert
        assertNotNull(mainMethod);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void testCrmApplication_ShouldHaveSpringBootApplicationAnnotation() {
        // Arrange & Act
        boolean hasAnnotation = CrmApplication.class.isAnnotationPresent(
                org.springframework.boot.autoconfigure.SpringBootApplication.class
        );

        // Assert
        assertTrue(hasAnnotation);
    }

    @Test
    void testCrmApplication_ShouldBePublicClass() {
        // Arrange & Act
        int modifiers = CrmApplication.class.getModifiers();

        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }

    @Test
    void testMain_ShouldBePublicStatic() throws Exception {
        // Arrange & Act
        var mainMethod = CrmApplication.class.getMethod("main", String[].class);
        int modifiers = mainMethod.getModifiers();

        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
        assertTrue(java.lang.reflect.Modifier.isStatic(modifiers));
    }

    @Test
    void testCrmApplication_ShouldHaveDefaultConstructor() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> new CrmApplication());
    }
}

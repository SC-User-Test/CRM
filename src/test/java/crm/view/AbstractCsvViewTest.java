package crm.view;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.view.AbstractView;

import static org.junit.jupiter.api.Assertions.*;

class AbstractCsvViewTest {

    @Test
    void testAbstractCsvViewClassExists() {
        // Assert
        assertNotNull(AbstractCsvView.class);
    }

    @Test
    void testAbstractCsvViewExtendsAbstractView() {
        // Assert
        assertTrue(AbstractView.class.isAssignableFrom(AbstractCsvView.class));
    }

    @Test
    void testAbstractCsvViewIsAbstract() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractCsvView.class.getModifiers()));
    }
}

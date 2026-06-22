package crm.view;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.view.AbstractView;

import static org.junit.jupiter.api.Assertions.*;

class AbstractPdfViewTest {

    @Test
    void testAbstractPdfViewClassExists() {
        // Assert
        assertNotNull(AbstractPdfView.class);
    }

    @Test
    void testAbstractPdfViewExtendsAbstractView() {
        // Assert
        assertTrue(AbstractView.class.isAssignableFrom(AbstractPdfView.class));
    }

    @Test
    void testAbstractPdfViewIsAbstract() {
        // Assert
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractPdfView.class.getModifiers()));
    }
}

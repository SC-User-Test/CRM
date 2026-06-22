package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.view.AbstractView;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewTest {

    private CsvView csvView;

    @BeforeEach
    void setUp() {
        csvView = new CsvView();
    }

    @Test
    void testCsvViewCreation() {
        // Assert
        assertNotNull(csvView);
    }

    @Test
    void testCsvViewExtendsAbstractCsvView() {
        // Assert
        assertTrue(AbstractCsvView.class.isAssignableFrom(CsvView.class));
    }

    @Test
    void testCsvViewIsInstantiable() {
        // Act
        CsvView view = new CsvView();

        // Assert
        assertNotNull(view);
    }
}

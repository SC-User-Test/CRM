package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CsvViewTest {

    private CsvView csvView;

    @BeforeEach
    void setUp() {
        csvView = new CsvView();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        CsvView view = new CsvView();

        // Assert
        assertNotNull(view);
    }

    @Test
    void testCsvView_ShouldExtendAbstractCsvView() {
        // Arrange & Act & Assert
        assertTrue(csvView instanceof AbstractCsvView);
    }

    @Test
    void testCsvView_ShouldBeInstantiable() {
        // Arrange & Act
        CsvView view = new CsvView();

        // Assert
        assertNotNull(view);
        assertInstanceOf(CsvView.class, view);
    }
}

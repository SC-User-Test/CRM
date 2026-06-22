package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewTest {

    private ExcelView excelView;

    @BeforeEach
    void setUp() {
        excelView = new ExcelView();
    }

    @Test
    void testExcelViewCreation() {
        // Assert
        assertNotNull(excelView);
    }

    @Test
    void testExcelViewIsInstantiable() {
        // Act
        ExcelView view = new ExcelView();

        // Assert
        assertNotNull(view);
    }
}

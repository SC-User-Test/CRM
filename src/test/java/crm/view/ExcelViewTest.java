package crm.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewTest {

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        ExcelView excelView = new ExcelView();
        // Assert
        assertNotNull(excelView);
    }
}

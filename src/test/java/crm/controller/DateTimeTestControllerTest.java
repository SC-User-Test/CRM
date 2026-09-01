package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateTimeTestControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private DateTimeTestController dateTimeTestController;

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        DateTimeTestController controller = new DateTimeTestController();
        // Assert
        assertNotNull(controller);
    }

    @Test
    void testDateTimeTest_returnsDateTestView() {
        // Arrange & Act
        String viewName = dateTimeTestController.dateTimeTest(model);
        // Assert
        assertEquals("date/test", viewName);
    }

    @Test
    void testDateTimeTest_addsStandardDateToModel() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);
        // Assert
        verify(model).addAttribute(eq("standardDate"), any());
    }

    @Test
    void testDateTimeTest_addsLocalDateTimeToModel() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);
        // Assert
        verify(model).addAttribute(eq("localDateTime"), any());
    }

    @Test
    void testDateTimeTest_addsLocalDateToModel() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);
        // Assert
        verify(model).addAttribute(eq("localDate"), any());
    }

    @Test
    void testDateTimeTest_addsTimestampToModel() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);
        // Assert
        verify(model).addAttribute(eq("timestamp"), any());
    }

    @Test
    void testDateTimeTest_addsAllFourAttributesToModel() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);
        // Assert
        verify(model, times(4)).addAttribute(anyString(), any());
    }
}

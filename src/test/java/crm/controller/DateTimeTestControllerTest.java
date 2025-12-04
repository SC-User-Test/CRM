package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateTimeTestControllerTest {

    @InjectMocks
    private DateTimeTestController dateTimeTestController;

    private Model model;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
    }

    @Test
    void testDateTimeTest_ShouldReturnCorrectView() {
        // Arrange & Act
        String result = dateTimeTestController.dateTimeTest(model);

        // Assert
        assertEquals("date/test", result);
    }

    @Test
    void testDateTimeTest_ShouldAddStandardDateAttribute() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);

        // Assert
        verify(model).addAttribute(eq("standardDate"), any());
    }

    @Test
    void testDateTimeTest_ShouldAddLocalDateTimeAttribute() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);

        // Assert
        verify(model).addAttribute(eq("localDateTime"), any());
    }

    @Test
    void testDateTimeTest_ShouldAddLocalDateAttribute() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);

        // Assert
        verify(model).addAttribute(eq("localDate"), any());
    }

    @Test
    void testDateTimeTest_ShouldAddTimestampAttribute() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);

        // Assert
        verify(model).addAttribute(eq("timestamp"), any());
    }

    @Test
    void testDateTimeTest_ShouldAddAllFourAttributes() {
        // Arrange & Act
        dateTimeTestController.dateTimeTest(model);

        // Assert
        verify(model, times(4)).addAttribute(anyString(), any());
    }

    @Test
    void testDateTimeTest_WithNullModel_ShouldThrowException() {
        // Arrange, Act & Assert
        assertThrows(NullPointerException.class, () -> {
            dateTimeTestController.dateTimeTest(null);
        });
    }
}

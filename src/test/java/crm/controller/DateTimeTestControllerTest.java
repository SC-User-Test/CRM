package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateTimeTestControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private DateTimeTestController dateTimeTestController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void dateTimeTest_shouldAddAllDateTypesToModel() {
        // Act
        String result = dateTimeTestController.dateTimeTest(model);

        // Assert
        verify(model).addAttribute(eq("standardDate"), any(Date.class));
        verify(model).addAttribute(eq("localDateTime"), any(LocalDateTime.class));
        verify(model).addAttribute(eq("localDate"), any(LocalDate.class));
        verify(model).addAttribute(eq("timestamp"), any(Instant.class));
        assertEquals("date/test", result);
    }

    @Test
    void dateTimeTest_shouldReturnCorrectViewName() {
        // Act
        String result = dateTimeTestController.dateTimeTest(model);

        // Assert
        assertEquals("date/test", result);
    }

    @Test
    void dateTimeTestController_shouldBeInstantiable() {
        // Assert
        assertNotNull(dateTimeTestController);
    }
}

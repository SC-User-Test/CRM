package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateTimeTestControllerTest {

    @InjectMocks
    private DateTimeTestController dateTimeTestController;

    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = mock(Model.class);
    }

    @Test
    void testDateTimeTest_ReturnsCorrectView() {
        String result = dateTimeTestController.dateTimeTest(model);

        assertEquals("date/test", result);
    }

    @Test
    void testDateTimeTest_AddsAttributesToModel() {
        dateTimeTestController.dateTimeTest(model);

        verify(model, times(1)).addAttribute(eq("standardDate"), any());
        verify(model, times(1)).addAttribute(eq("localDateTime"), any());
        verify(model, times(1)).addAttribute(eq("localDate"), any());
        verify(model, times(1)).addAttribute(eq("timestamp"), any());
    }

    @Test
    void testDateTimeTest_ModelNotNull() {
        assertDoesNotThrow(() -> dateTimeTestController.dateTimeTest(model));
    }
}

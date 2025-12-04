package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateTimeTestControllerTest {

    private DateTimeTestController dateTimeTestController;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dateTimeTestController = new DateTimeTestController();
    }

    @Test
    void testDateTimeTest() {
        String result = dateTimeTestController.dateTimeTest(model);

        assertEquals("date/test", result);
        verify(model, times(1)).addAttribute(eq("standardDate"), any());
        verify(model, times(1)).addAttribute(eq("localDateTime"), any());
        verify(model, times(1)).addAttribute(eq("localDate"), any());
        verify(model, times(1)).addAttribute(eq("timestamp"), any());
    }

    @Test
    void testDateTimeTestAddsAttributes() {
        dateTimeTestController.dateTimeTest(model);

        verify(model, atLeastOnce()).addAttribute(anyString(), any());
    }

    @Test
    void testDateTimeTestReturnsCorrectView() {
        String viewName = dateTimeTestController.dateTimeTest(model);
        assertNotNull(viewName);
        assertTrue(viewName.contains("date"));
    }
}

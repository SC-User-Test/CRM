package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DateTimeTestControllerTest {

    @InjectMocks
    private DateTimeTestController controller;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = mock(Model.class);
    }

    @Test
    public void testDateTimeTest() {
        String result = controller.dateTimeTest(model);

        assertEquals("date/test", result);
        verify(model, times(4)).addAttribute(anyString(), any());
    }

    @Test
    public void testDateTimeTestAddsStandardDate() {
        controller.dateTimeTest(model);
        verify(model).addAttribute(eq("standardDate"), any(Date.class));
    }

    @Test
    public void testDateTimeTestAddsLocalDateTime() {
        controller.dateTimeTest(model);
        verify(model).addAttribute(eq("localDateTime"), any(LocalDateTime.class));
    }

    @Test
    public void testDateTimeTestAddsLocalDate() {
        controller.dateTimeTest(model);
        verify(model).addAttribute(eq("localDate"), any(LocalDate.class));
    }

    @Test
    public void testDateTimeTestAddsTimestamp() {
        controller.dateTimeTest(model);
        verify(model).addAttribute(eq("timestamp"), any(Instant.class));
    }

    @Test
    public void testDateTimeTestReturnsCorrectView() {
        String viewName = controller.dateTimeTest(model);
        assertNotNull(viewName);
        assertFalse(viewName.isEmpty());
        assertTrue(viewName.contains("date"));
    }
}

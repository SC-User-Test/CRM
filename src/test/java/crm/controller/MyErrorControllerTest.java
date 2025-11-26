package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MyErrorControllerTest {

    @InjectMocks
    private MyErrorController errorController;

    @Test
    public void testConstructor() {
        MyErrorController controller = new MyErrorController();
        assertNotNull(controller);
    }

    @Test
    public void testError() {
        String result = errorController.error();
        assertEquals("Error handling", result);
    }

    @Test
    public void testErrorReturnsNonNull() {
        String result = errorController.error();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testErrorMessageContent() {
        String result = errorController.error();
        assertTrue(result.contains("Error"));
    }
}

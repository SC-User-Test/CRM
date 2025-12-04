package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController myErrorController;

    @BeforeEach
    void setUp() {
        myErrorController = new MyErrorController();
    }

    @Test
    void testError() {
        String result = myErrorController.error();
        assertNotNull(result);
        assertEquals("Error handling", result);
    }

    @Test
    void testErrorReturnsString() {
        String result = myErrorController.error();
        assertTrue(result instanceof String);
    }

    @Test
    void testErrorNotEmpty() {
        String result = myErrorController.error();
        assertFalse(result.isEmpty());
    }

    @Test
    void testErrorMethod() {
        assertDoesNotThrow(() -> myErrorController.error());
    }
}

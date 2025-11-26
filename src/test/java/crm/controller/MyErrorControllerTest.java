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
    void testConstructor() {
        assertNotNull(myErrorController);
    }

    @Test
    void testError_ReturnsErrorHandling() {
        String result = myErrorController.error();

        assertEquals("Error handling", result);
    }

    @Test
    void testError_NotNull() {
        String result = myErrorController.error();

        assertNotNull(result);
    }

    @Test
    void testError_NotEmpty() {
        String result = myErrorController.error();

        assertFalse(result.isEmpty());
    }
}

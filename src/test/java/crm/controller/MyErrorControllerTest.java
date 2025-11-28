package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController errorController;

    @BeforeEach
    void setUp() {
        errorController = new MyErrorController();
    }

    @Test
    void testMyErrorControllerConstructor() {
        assertNotNull(errorController);
    }

    @Test
    void testError() {
        String result = errorController.error();
        assertEquals("Error handling", result);
    }

    @Test
    void testErrorReturnsNonNull() {
        String result = errorController.error();
        assertNotNull(result);
    }

    @Test
    void testErrorReturnsExpectedMessage() {
        String result = errorController.error();
        assertTrue(result.contains("Error"));
    }
}

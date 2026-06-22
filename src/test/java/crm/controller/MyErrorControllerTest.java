package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController errorController;

    @BeforeEach
    void setUp() {
        errorController = new MyErrorController();
    }

    @Test
    void error_shouldReturnErrorMessage() {
        // Act
        String result = errorController.error();

        // Assert
        assertNotNull(result);
        assertEquals("Error handling", result);
    }

    @Test
    void myErrorController_shouldBeInstantiable() {
        // Assert
        assertNotNull(errorController);
    }

    @Test
    void myErrorController_shouldImplementErrorController() {
        // Assert
        assertTrue(errorController instanceof org.springframework.boot.web.servlet.error.ErrorController);
    }
}

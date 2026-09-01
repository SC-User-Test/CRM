package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MyErrorControllerTest {

    @InjectMocks
    private MyErrorController myErrorController;

    @Test
    void testError_returnsErrorHandlingString() {
        // Arrange & Act
        String result = myErrorController.error();
        // Assert
        assertEquals("Error handling", result);
    }

    @Test
    void testError_returnsNonNullString() {
        // Arrange & Act
        String result = myErrorController.error();
        // Assert
        assertNotNull(result);
    }

    @Test
    void testError_returnsNonEmptyString() {
        // Arrange & Act
        String result = myErrorController.error();
        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        MyErrorController controller = new MyErrorController();
        // Assert
        assertNotNull(controller);
    }
}

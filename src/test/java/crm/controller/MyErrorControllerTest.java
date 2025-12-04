package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MyErrorControllerTest {

    @InjectMocks
    private MyErrorController myErrorController;

    @BeforeEach
    void setUp() {
        myErrorController = new MyErrorController();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        MyErrorController controller = new MyErrorController();

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testError_ShouldReturnErrorMessage() {
        // Arrange & Act
        String result = myErrorController.error();

        // Assert
        assertEquals("Error handling", result);
    }

    @Test
    void testError_ShouldReturnNonNullValue() {
        // Arrange & Act
        String result = myErrorController.error();

        // Assert
        assertNotNull(result);
    }

    @Test
    void testError_ShouldReturnNonEmptyString() {
        // Arrange & Act
        String result = myErrorController.error();

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void testError_CalledMultipleTimes_ShouldReturnSameValue() {
        // Arrange & Act
        String result1 = myErrorController.error();
        String result2 = myErrorController.error();

        // Assert
        assertEquals(result1, result2);
    }
}

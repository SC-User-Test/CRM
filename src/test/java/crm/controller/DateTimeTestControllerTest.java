package crm.controller;

import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTestControllerTest {

    @Test
    void testDateTimeTestControllerHasControllerAnnotation() {
        // Assert
        assertTrue(DateTimeTestController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testDateTimeTestControllerIsInstantiable() {
        // Act
        DateTimeTestController controller = new DateTimeTestController();

        // Assert
        assertNotNull(controller);
    }
}

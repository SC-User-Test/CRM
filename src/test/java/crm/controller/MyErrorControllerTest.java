package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;

import static org.junit.jupiter.api.Assertions.*;

class MyErrorControllerTest {

    private MyErrorController myErrorController;

    @BeforeEach
    void setUp() {
        myErrorController = new MyErrorController();
    }

    @Test
    void testMyErrorControllerCreation() {
        // Assert
        assertNotNull(myErrorController);
    }

    @Test
    void testMyErrorControllerHasControllerAnnotation() {
        // Assert
        assertTrue(MyErrorController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testMyErrorControllerIsInstantiable() {
        // Act
        MyErrorController controller = new MyErrorController();

        // Assert
        assertNotNull(controller);
    }
}

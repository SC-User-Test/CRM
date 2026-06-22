package crm.controller;

import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Controller;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    private CSVController csvController;

    @BeforeEach
    void setUp() {
        csvController = new CSVController(customerService);
    }

    @Test
    void testCSVControllerCreation() {
        // Assert
        assertNotNull(csvController);
    }

    @Test
    void testCSVControllerHasControllerAnnotation() {
        // Assert
        assertTrue(CSVController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testCSVControllerConstructor() {
        // Act
        CSVController controller = new CSVController(customerService);

        // Assert
        assertNotNull(controller);
    }
}

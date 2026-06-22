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
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService);
    }

    @Test
    void testCustomerControllerCreation() {
        // Assert
        assertNotNull(customerController);
    }

    @Test
    void testCustomerControllerHasControllerAnnotation() {
        // Assert
        assertTrue(CustomerController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testCustomerControllerConstructor() {
        // Act
        CustomerController controller = new CustomerController(customerService);

        // Assert
        assertNotNull(controller);
    }
}

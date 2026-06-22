package crm.controller;

import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Controller;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    private ContractController contractController;

    @BeforeEach
    void setUp() {
        contractController = new ContractController(contractService, customerService, userService);
    }

    @Test
    void testContractControllerCreation() {
        // Assert
        assertNotNull(contractController);
    }

    @Test
    void testContractControllerHasControllerAnnotation() {
        // Assert
        assertTrue(ContractController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testContractControllerConstructor() {
        // Act
        ContractController controller = new ContractController(contractService, customerService, userService);

        // Assert
        assertNotNull(controller);
    }
}

package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Controller;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void testUserControllerCreation() {
        // Assert
        assertNotNull(userController);
    }

    @Test
    void testUserControllerHasControllerAnnotation() {
        // Assert
        assertTrue(UserController.class.isAnnotationPresent(Controller.class));
    }

    @Test
    void testUserControllerConstructor() {
        // Act
        UserController controller = new UserController(userService);

        // Assert
        assertNotNull(controller);
    }
}

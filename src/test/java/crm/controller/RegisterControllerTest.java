package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void testConstructor_ShouldInitializeWithUserService() {
        // Arrange & Act
        RegisterController controller = new RegisterController(userService);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowRegistrationPage_ShouldReturnRegisterView() {
        // Arrange & Act
        String result = registerController.showRegistrationPage(model, user);

        // Assert
        assertEquals("register", result);
    }

    @Test
    void testShowRegistrationPage_ShouldAddUserToModel() {
        // Arrange & Act
        registerController.showRegistrationPage(model, user);

        // Assert
        verify(model).addAttribute("user", user);
    }

    @Test
    void testProcessRegistrationForm_WithExistingUser_ShouldReturnRegisterView() {
        // Arrange
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        String result = registerController.processRegistrationForm(model, user, bindingResult);

        // Assert
        assertEquals("register", result);
    }

    @Test
    void testProcessRegistrationForm_WithExistingUser_ShouldAddErrorMessage() {
        // Arrange
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        registerController.processRegistrationForm(model, user, bindingResult);

        // Assert
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
    }

    @Test
    void testProcessRegistrationForm_WithExistingUser_ShouldRejectEmail() {
        // Arrange
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        registerController.processRegistrationForm(model, user, bindingResult);

        // Assert
        verify(bindingResult).reject("email");
    }

    @Test
    void testProcessRegistrationForm_WithValidationErrors_ShouldRedirectToRegister() {
        // Arrange
        when(userService.findByUsername(user.getUsername())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = registerController.processRegistrationForm(model, user, bindingResult);

        // Assert
        assertEquals("redirect:/register", result);
    }

    @Test
    void testProcessRegistrationForm_WithValidUser_ShouldSaveUser() {
        // Arrange
        when(userService.findByUsername(user.getUsername())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        registerController.processRegistrationForm(model, user, bindingResult);

        // Assert
        verify(userService).saveUser(user);
    }

    @Test
    void testProcessRegistrationForm_WithValidUser_ShouldReturnSuccessView() {
        // Arrange
        when(userService.findByUsername(user.getUsername())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = registerController.processRegistrationForm(model, user, bindingResult);

        // Assert
        assertEquals("success", result);
    }

    @Test
    void testProcessRegistrationForm_WithNullUsername_ShouldHandleGracefully() {
        // Arrange
        User userWithNullUsername = new User();
        userWithNullUsername.setUsername(null);
        when(userService.findByUsername(null)).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = registerController.processRegistrationForm(model, userWithNullUsername, bindingResult);

        // Assert
        assertEquals("success", result);
    }
}

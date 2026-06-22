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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RegisterController registerController;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

    @Test
    void showRegistrationPage_shouldReturnRegisterView() {
        // Act
        String viewName = registerController.showRegistrationPage(model, testUser);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute("user", testUser);
    }

    @Test
    void processRegistrationForm_withNewUser_shouldReturnSuccess() {
        // Arrange
        when(userService.findByUsername(anyString())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("success", viewName);
        verify(userService).saveUser(testUser);
    }

    @Test
    void processRegistrationForm_withExistingUser_shouldReturnRegisterWithError() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        String viewName = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void processRegistrationForm_withValidationErrors_shouldRedirectToRegister() {
        // Arrange
        when(userService.findByUsername(anyString())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("redirect:/register", viewName);
        verify(userService, never()).saveUser(any(User.class));
    }

    @Test
    void constructor_shouldInitializeUserService() {
        // Arrange
        UserService service = mock(UserService.class);

        // Act
        RegisterController controller = new RegisterController(service);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void processRegistrationForm_withNullUsername_shouldCheckForExistingUser() {
        // Arrange
        testUser.setUsername(null);
        when(userService.findByUsername(null)).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("success", viewName);
        verify(userService).findByUsername(null);
    }
}

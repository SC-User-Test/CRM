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
import static org.mockito.ArgumentMatchers.*;
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

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("newuser")
                .email("new@example.com")
                .firstName("New")
                .lastName("User")
                .password("password")
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        RegisterController controller = new RegisterController(userService);
        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowRegistrationPage_returnsRegisterView() {
        // Arrange & Act
        String viewName = registerController.showRegistrationPage(model, user);
        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testProcessRegistrationForm_newUser_noErrors_returnsSuccess() {
        // Arrange
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        // Act
        String viewName = registerController.processRegistrationForm(model, user, bindingResult);
        // Assert
        assertEquals("success", viewName);
        verify(userService).saveUser(user);
    }

    @Test
    void testProcessRegistrationForm_existingUser_returnsRegisterWithMessage() {
        // Arrange
        User existingUser = User.builder().id(2L).username("newuser").build();
        when(userService.findByUsername("newuser")).thenReturn(existingUser);
        // Act
        String viewName = registerController.processRegistrationForm(model, user, bindingResult);
        // Assert
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testProcessRegistrationForm_newUser_withErrors_redirectsToRegister() {
        // Arrange
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = registerController.processRegistrationForm(model, user, bindingResult);
        // Assert
        assertEquals("redirect:/register", viewName);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testShowRegistrationPage_withNullUser_returnsRegisterView() {
        // Arrange
        User emptyUser = new User();
        // Act
        String viewName = registerController.showRegistrationPage(model, emptyUser);
        // Assert
        assertEquals("register", viewName);
    }

    @Test
    void testProcessRegistrationForm_alreadyRegisteredMessage_isCorrect() {
        // Arrange
        User existingUser = User.builder().id(2L).username("newuser").build();
        when(userService.findByUsername("newuser")).thenReturn(existingUser);
        // Act
        registerController.processRegistrationForm(model, user, bindingResult);
        // Assert
        verify(model).addAttribute("alreadyRegisteredMessage",
                "Oops!  There is already a user registered with the email provided.");
    }
}

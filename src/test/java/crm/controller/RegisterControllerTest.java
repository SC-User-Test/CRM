package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("newuser")
                .email("new@example.com")
                .password("password")
                .build();
    }

    @Test
    void showRegistrationPage_shouldReturnRegisterView() {
        // Act
        String result = registerController.showRegistrationPage(model, testUser);

        // Assert
        assertEquals("register", result);
        verify(model).addAttribute("user", testUser);
    }

    @Test
    void processRegistrationForm_withValidData_shouldReturnSuccess() {
        // Arrange
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("success", result);
        verify(userService).saveUser(testUser);
    }

    @Test
    void processRegistrationForm_withExistingUsername_shouldReturnRegister() {
        // Arrange
        User existingUser = User.builder().username("newuser").build();
        when(userService.findByUsername("newuser")).thenReturn(existingUser);

        // Act
        String result = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("register", result);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
        verify(userService, never()).saveUser(any());
    }

    @Test
    void processRegistrationForm_withValidationErrors_shouldRedirectToRegister() {
        // Arrange
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = registerController.processRegistrationForm(model, testUser, bindingResult);

        // Assert
        assertEquals("redirect:/register", result);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void registerController_shouldBeInstantiable() {
        // Assert
        assertNotNull(registerController);
    }
}

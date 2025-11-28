package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterControllerConstructor() {
        RegisterController controller = new RegisterController(userService);
        assertNotNull(controller);
    }

    @Test
    void testShowRegistrationPage() {
        User user = new User();

        String viewName = registerController.showRegistrationPage(model, user);

        assertEquals("register", viewName);
        verify(model).addAttribute("user", user);
    }

    @Test
    void testProcessRegistrationFormWithNewUser() {
        User user = User.builder()
                .username("newuser")
                .email("newuser@example.com")
                .password("password123")
                .build();

        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = registerController.processRegistrationForm(model, user, bindingResult);

        assertEquals("success", viewName);
        verify(userService).saveUser(user);
        verify(userService).findByUsername("newuser");
    }

    @Test
    void testProcessRegistrationFormWithExistingUser() {
        User user = User.builder()
                .username("existinguser")
                .email("existing@example.com")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .username("existinguser")
                .build();

        when(userService.findByUsername("existinguser")).thenReturn(existingUser);

        String viewName = registerController.processRegistrationForm(model, user, bindingResult);

        assertEquals("register", viewName);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
        verify(bindingResult).reject("email");
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testProcessRegistrationFormWithValidationErrors() {
        User user = User.builder()
                .username("testuser")
                .build();

        when(userService.findByUsername("testuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = registerController.processRegistrationForm(model, user, bindingResult);

        assertEquals("redirect:/register", viewName);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testProcessRegistrationFormErrorMessage() {
        User user = User.builder()
                .username("duplicate")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .username("duplicate")
                .build();

        when(userService.findByUsername("duplicate")).thenReturn(existingUser);

        registerController.processRegistrationForm(model, user, bindingResult);

        verify(model).addAttribute(eq("alreadyRegisteredMessage"),
                eq("Oops!  There is already a user registered with the email provided."));
    }

    @Test
    void testProcessRegistrationFormVerifyServiceCalls() {
        User user = User.builder()
                .username("verify")
                .email("verify@example.com")
                .password("password")
                .build();

        when(userService.findByUsername("verify")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        registerController.processRegistrationForm(model, user, bindingResult);

        verify(userService, times(1)).findByUsername("verify");
        verify(userService, times(1)).saveUser(user);
    }
}

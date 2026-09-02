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
                .email("new@test.com")
                .firstName("New")
                .lastName("User")
                .password("password")
                .enabled(1)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        RegisterController controller = new RegisterController(userService);
        assertNotNull(controller);
    }

    @Test
    void testShowRegistrationPage_returnsRegisterView() {
        String view = registerController.showRegistrationPage(model, user);
        assertEquals("register", view);
        verify(model).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testProcessRegistrationForm_userAlreadyExists_returnsRegisterView() {
        when(userService.findByUsername("newuser")).thenReturn(user);
        String view = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("register", view);
        verify(model).addAttribute(eq("alreadyRegisteredMessage"), anyString());
    }

    @Test
    void testProcessRegistrationForm_userAlreadyExists_rejectsEmail() {
        when(userService.findByUsername("newuser")).thenReturn(user);
        registerController.processRegistrationForm(model, user, bindingResult);
        verify(bindingResult).reject("email");
    }

    @Test
    void testProcessRegistrationForm_withBindingErrors_redirectsToRegister() {
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("redirect:/register", view);
    }

    @Test
    void testProcessRegistrationForm_successfulRegistration_returnsSuccessView() {
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = registerController.processRegistrationForm(model, user, bindingResult);
        assertEquals("success", view);
        verify(userService).saveUser(user);
    }

    @Test
    void testProcessRegistrationForm_successfulRegistration_callsSaveUser() {
        when(userService.findByUsername("newuser")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);
        registerController.processRegistrationForm(model, user, bindingResult);
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void testProcessRegistrationForm_userAlreadyExists_doesNotSaveUser() {
        when(userService.findByUsername("newuser")).thenReturn(user);
        registerController.processRegistrationForm(model, user, bindingResult);
        verify(userService, never()).saveUser(any());
    }

    @Test
    void testShowRegistrationPage_withNullUser_returnsRegisterView() {
        String view = registerController.showRegistrationPage(model, null);
        assertEquals("register", view);
    }
}

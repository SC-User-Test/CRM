package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private Model model;
    private User user;
    private UserDetails userDetails;
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = mock(Model.class);
        userDetails = mock(UserDetails.class);
        bindingResult = mock(BindingResult.class);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
    }

    @Test
    void testConstructor() {
        UserController controller = new UserController(userService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllUsers() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Collections.singletonList(user));

        String result = userController.showAllUsers(model, userDetails);

        assertEquals("user/list", result);
        verify(model, times(1)).addAttribute("currentUser", user);
        verify(model, times(1)).addAttribute(eq("users"), any());
    }

    @Test
    void testShowFormEditUser() {
        when(userService.showUser(1L)).thenReturn(user);

        String result = userController.showFormEditUser(model, 1L);

        assertEquals("user/edit", result);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    void testProcessRequestEditUser_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = userController.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).editUser(user);
    }

    @Test
    void testProcessRequestEditUser_WithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = userController.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/edit/1", result);
        verify(userService, never()).editUser(user);
    }

    @Test
    void testDeleteUser() {
        when(userService.showUser(1L)).thenReturn(user);

        String result = userController.deleteUser(1L);

        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).deleteUser(user);
    }
}

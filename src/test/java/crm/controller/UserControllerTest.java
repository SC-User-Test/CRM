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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails currentUser;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAllUsers() {
        List<User> users = new ArrayList<>();
        when(currentUser.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(new User());
        when(userService.listAllUsers()).thenReturn(users);

        String result = userController.showAllUsers(model, currentUser);

        assertEquals("user/list", result);
        verify(model, times(1)).addAttribute(eq("currentUser"), any());
        verify(model, times(1)).addAttribute("users", users);
    }

    @Test
    void testShowFormEditUser() {
        User user = new User();
        user.setId(1L);
        when(userService.showUser(1L)).thenReturn(user);

        String result = userController.showFormEditUser(model, 1L);

        assertEquals("user/edit", result);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    void testProcessRequestEditUserWithErrors() {
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = userController.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/edit/1", result);
    }

    @Test
    void testProcessRequestEditUserSuccess() {
        User user = new User();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = userController.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).editUser(user);
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        when(userService.showUser(1L)).thenReturn(user);

        String result = userController.deleteUser(1L);

        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).deleteUser(user);
    }
}

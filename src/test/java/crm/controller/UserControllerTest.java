package crm.controller;

import crm.entity.Role;
import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails currentUserDetails;

    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        when(currentUserDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    public void testUserControllerCreation() {
        assertNotNull(userController);
    }

    @Test
    public void testShowAllUsers() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        User currentUser = User.builder()
                .id(1L)
                .username("testuser")
                .role(role)
                .build();

        List<User> users = Arrays.asList(
                User.builder().id(1L).username("user1").build(),
                User.builder().id(2L).username("user2").build()
        );

        when(userService.findByUsername("testuser")).thenReturn(currentUser);
        when(userService.listAllUsers()).thenReturn(users);

        String result = userController.showAllUsers(model, currentUserDetails);
        assertEquals("user/list", result);
        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, times(1)).listAllUsers();
        verify(model, times(1)).addAttribute("currentUser", currentUser);
        verify(model, times(1)).addAttribute("users", users);
    }

    @Test
    public void testShowFormEditUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();
        when(userService.showUser(1L)).thenReturn(user);

        String result = userController.showFormEditUser(model, 1L);
        assertEquals("user/edit", result);
        verify(userService, times(1)).showUser(1L);
        verify(model, times(1)).addAttribute("user", user);
    }

    @Test
    public void testProcessRequestEditUserSuccess() {
        User user = User.builder()
                .id(1L)
                .username("updateduser")
                .email("updated@example.com")
                .password("newpassword")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = userController.processRequestEditUser(1L, user, bindingResult);
        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).editUser(user);
    }

    @Test
    public void testProcessRequestEditUserValidationErrors() {
        User user = User.builder()
                .id(1L)
                .username("u")
                .build();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = userController.processRequestEditUser(1L, user, bindingResult);
        assertEquals("redirect:/user/edit/1", result);
        verify(userService, never()).editUser(user);
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .enabled(1)
                .build();
        when(userService.showUser(1L)).thenReturn(user);

        String result = userController.deleteUser(1L);
        assertEquals("redirect:/user/list", result);
        verify(userService, times(1)).showUser(1L);
        verify(userService, times(1)).deleteUser(user);
    }

    @Test
    public void testDeleteUserMultiple() {
        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username("user2").build();

        when(userService.showUser(1L)).thenReturn(user1);
        when(userService.showUser(2L)).thenReturn(user2);

        userController.deleteUser(1L);
        userController.deleteUser(2L);

        verify(userService, times(1)).deleteUser(user1);
        verify(userService, times(1)).deleteUser(user2);
    }

    @Test
    public void testShowFormEditUserNotFound() {
        when(userService.showUser(999L)).thenReturn(null);

        String result = userController.showFormEditUser(model, 999L);
        assertEquals("user/edit", result);
        verify(userService, times(1)).showUser(999L);
    }
}

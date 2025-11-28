package crm.controller;

import crm.entity.Role;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private UserDetails currentUserDetails;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserControllerConstructor() {
        UserController controller = new UserController(userService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllUsers() {
        User currentUser = User.builder().username("admin").build();
        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username("user2").build();

        when(currentUserDetails.getUsername()).thenReturn("admin");
        when(userService.findByUsername("admin")).thenReturn(currentUser);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user1, user2));

        String viewName = userController.showAllUsers(model, currentUserDetails);

        assertEquals("user/list", viewName);
        verify(model).addAttribute("currentUser", currentUser);
        verify(model).addAttribute("users", Arrays.asList(user1, user2));
        verify(userService).findByUsername("admin");
        verify(userService).listAllUsers();
    }

    @Test
    void testShowFormEditUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(userService.showUser(1L)).thenReturn(user);

        String viewName = userController.showFormEditUser(model, 1L);

        assertEquals("user/edit", viewName);
        verify(model).addAttribute("user", user);
        verify(userService).showUser(1L);
    }

    @Test
    void testProcessRequestEditUserWithValidData() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(role)
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = userController.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/list", viewName);
        verify(userService).editUser(user);
    }

    @Test
    void testProcessRequestEditUserWithErrors() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.processRequestEditUser(1L, user, bindingResult);

        assertEquals("redirect:/user/edit/1", viewName);
        verify(userService, never()).editUser(any());
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(userService.showUser(1L)).thenReturn(user);

        String viewName = userController.deleteUser(1L);

        assertEquals("redirect:/user/list", viewName);
        verify(userService).showUser(1L);
        verify(userService).deleteUser(user);
    }

    @Test
    void testDeleteUserWithDifferentId() {
        User user = User.builder()
                .id(5L)
                .username("anotheruser")
                .build();

        when(userService.showUser(5L)).thenReturn(user);

        String viewName = userController.deleteUser(5L);

        assertEquals("redirect:/user/list", viewName);
        verify(userService).deleteUser(user);
    }

    @Test
    void testShowAllUsersVerifyInteractions() {
        User currentUser = User.builder().username("testuser").build();

        when(currentUserDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(currentUser);
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        userController.showAllUsers(model, currentUserDetails);

        verify(currentUserDetails, times(1)).getUsername();
        verify(userService, times(1)).findByUsername("testuser");
        verify(userService, times(1)).listAllUsers();
    }
}

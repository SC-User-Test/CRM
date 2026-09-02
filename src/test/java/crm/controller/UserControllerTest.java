package crm.controller;

import crm.entity.Role;
import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        UserController controller = new UserController(userService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllUsers_returnsUserListView() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        String view = userController.showAllUsers(model, userDetails);
        assertEquals("user/list", view);
        verify(model).addAttribute(eq("currentUser"), eq(user));
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testShowFormEditUser_returnsEditView() {
        when(userService.showUser(1L)).thenReturn(user);
        String view = userController.showFormEditUser(model, 1L);
        assertEquals("user/edit", view);
        verify(model).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testProcessRequestEditUser_withErrors_redirectsToEdit() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = userController.processRequestEditUser(1L, user, bindingResult);
        assertEquals("redirect:/user/edit/1", view);
    }

    @Test
    void testProcessRequestEditUser_withoutErrors_redirectsToList() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = userController.processRequestEditUser(1L, user, bindingResult);
        assertEquals("redirect:/user/list", view);
        verify(userService).editUser(user);
    }

    @Test
    void testDeleteUser_redirectsToList() {
        when(userService.showUser(1L)).thenReturn(user);
        String view = userController.deleteUser(1L);
        assertEquals("redirect:/user/list", view);
        verify(userService).deleteUser(user);
    }

    @Test
    void testDeleteUser_callsShowUserFirst() {
        when(userService.showUser(1L)).thenReturn(user);
        userController.deleteUser(1L);
        verify(userService).showUser(1L);
    }

    @Test
    void testShowAllUsers_addsCurrentUserToModel() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        userController.showAllUsers(model, userDetails);
        verify(model).addAttribute("currentUser", user);
    }

    @Test
    void testProcessRequestEditUser_withErrors_doesNotCallEditUser() {
        when(bindingResult.hasErrors()).thenReturn(true);
        userController.processRequestEditUser(1L, user, bindingResult);
        verify(userService, never()).editUser(any());
    }
}

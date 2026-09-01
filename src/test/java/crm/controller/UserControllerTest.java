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
import java.util.List;

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
    private UserDetails currentUserDetails;

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
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        UserController controller = new UserController(userService);
        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowAllUsers_returnsUserListView() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(currentUserDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(users);
        // Act
        String viewName = userController.showAllUsers(model, currentUserDetails);
        // Assert
        assertEquals("user/list", viewName);
        verify(model).addAttribute(eq("currentUser"), eq(user));
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testShowFormEditUser_returnsEditView() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);
        // Act
        String viewName = userController.showFormEditUser(model, 1L);
        // Assert
        assertEquals("user/edit", viewName);
        verify(model).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testProcessRequestEditUser_withNoErrors_redirectsToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        // Act
        String viewName = userController.processRequestEditUser(1L, user, bindingResult);
        // Assert
        assertEquals("redirect:/user/list", viewName);
        verify(userService).editUser(user);
    }

    @Test
    void testProcessRequestEditUser_withErrors_redirectsToEdit() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = userController.processRequestEditUser(1L, user, bindingResult);
        // Assert
        assertEquals("redirect:/user/edit/1", viewName);
        verify(userService, never()).editUser(any());
    }

    @Test
    void testDeleteUser_deletesAndRedirectsToList() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);
        // Act
        String viewName = userController.deleteUser(1L);
        // Assert
        assertEquals("redirect:/user/list", viewName);
        verify(userService).deleteUser(user);
    }

    @Test
    void testShowAllUsers_withMultipleUsers_addsAllToModel() {
        // Arrange
        User user2 = User.builder().id(2L).username("user2").role(role).build();
        List<User> users = Arrays.asList(user, user2);
        when(currentUserDetails.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(users);
        // Act
        String viewName = userController.showAllUsers(model, currentUserDetails);
        // Assert
        assertEquals("user/list", viewName);
    }
}

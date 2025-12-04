package crm.controller;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private UserDetails currentUser;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
    }

    @Test
    void testConstructor_ShouldInitializeWithUserService() {
        // Arrange & Act
        UserController controller = new UserController(userService);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowAllUsers_ShouldReturnUserListView() {
        // Arrange
        when(currentUser.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        // Act
        String result = userController.showAllUsers(model, currentUser);

        // Assert
        assertEquals("user/list", result);
    }

    @Test
    void testShowAllUsers_ShouldAddCurrentUserToModel() {
        // Arrange
        when(currentUser.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));

        // Act
        userController.showAllUsers(model, currentUser);

        // Assert
        verify(model).addAttribute("currentUser", user);
    }

    @Test
    void testShowAllUsers_ShouldAddUsersListToModel() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(currentUser.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(userService.listAllUsers()).thenReturn(users);

        // Act
        userController.showAllUsers(model, currentUser);

        // Assert
        verify(model).addAttribute("users", users);
    }

    @Test
    void testShowFormEditUser_ShouldReturnEditView() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);

        // Act
        String result = userController.showFormEditUser(model, 1L);

        // Assert
        assertEquals("user/edit", result);
    }

    @Test
    void testShowFormEditUser_ShouldAddUserToModel() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);

        // Act
        userController.showFormEditUser(model, 1L);

        // Assert
        verify(model).addAttribute("user", user);
    }

    @Test
    void testProcessRequestEditUser_WithValidationErrors_ShouldRedirectToEdit() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = userController.processRequestEditUser(1L, user, bindingResult);

        // Assert
        assertEquals("redirect:/user/edit/1", result);
    }

    @Test
    void testProcessRequestEditUser_WithoutErrors_ShouldCallEditUser() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        userController.processRequestEditUser(1L, user, bindingResult);

        // Assert
        verify(userService).editUser(user);
    }

    @Test
    void testProcessRequestEditUser_WithoutErrors_ShouldRedirectToUserList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = userController.processRequestEditUser(1L, user, bindingResult);

        // Assert
        assertEquals("redirect:/user/list", result);
    }

    @Test
    void testDeleteUser_ShouldCallShowUserService() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);

        // Act
        userController.deleteUser(1L);

        // Assert
        verify(userService).showUser(1L);
    }

    @Test
    void testDeleteUser_ShouldCallDeleteUserService() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);

        // Act
        userController.deleteUser(1L);

        // Assert
        verify(userService).deleteUser(user);
    }

    @Test
    void testDeleteUser_ShouldRedirectToUserList() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(user);

        // Act
        String result = userController.deleteUser(1L);

        // Assert
        assertEquals("redirect:/user/list", result);
    }

    @Test
    void testDeleteUser_WithNullId_ShouldCallServiceWithNull() {
        // Arrange
        when(userService.showUser(null)).thenReturn(null);

        // Act
        userController.deleteUser(null);

        // Assert
        verify(userService).deleteUser(null);
    }
}

package crm.controller;

import crm.entity.User;
import crm.entity.Role;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    private UserDetails currentUser;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(testRole)
                .build();
    }

    @Test
    void showAllUsers_shouldReturnUserListView() {
        // Arrange
        when(currentUser.getUsername()).thenReturn("testuser");
        when(userService.findByUsername("testuser")).thenReturn(testUser);
        when(userService.listAllUsers()).thenReturn(Arrays.asList(testUser));

        // Act
        String result = userController.showAllUsers(model, currentUser);

        // Assert
        assertEquals("user/list", result);
        verify(model).addAttribute(eq("currentUser"), any());
        verify(model).addAttribute(eq("users"), any());
        verify(userService).listAllUsers();
    }

    @Test
    void showFormEditUser_shouldReturnEditView() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(testUser);

        // Act
        String result = userController.showFormEditUser(model, 1L);

        // Assert
        assertEquals("user/edit", result);
        verify(model).addAttribute("user", testUser);
        verify(userService).showUser(1L);
    }

    @Test
    void processRequestEditUser_withValidData_shouldRedirectToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = userController.processRequestEditUser(1L, testUser, bindingResult);

        // Assert
        assertEquals("redirect:/user/list", result);
        verify(userService).editUser(testUser);
    }

    @Test
    void processRequestEditUser_withErrors_shouldRedirectToEditForm() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = userController.processRequestEditUser(1L, testUser, bindingResult);

        // Assert
        assertEquals("redirect:/user/edit/1", result);
        verify(userService, never()).editUser(any());
    }

    @Test
    void deleteUser_shouldDeleteAndRedirect() {
        // Arrange
        when(userService.showUser(1L)).thenReturn(testUser);

        // Act
        String result = userController.deleteUser(1L);

        // Assert
        assertEquals("redirect:/user/list", result);
        verify(userService).showUser(1L);
        verify(userService).deleteUser(testUser);
    }

    @Test
    void userController_shouldBeInstantiable() {
        // Assert
        assertNotNull(userController);
    }
}

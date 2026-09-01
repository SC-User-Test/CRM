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
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private Export export;

    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(role)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        Export exportController = new Export(userService);
        // Assert
        assertNotNull(exportController);
    }

    @Test
    void testDownload_addsUsersToModel() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userService.listAllUsers()).thenReturn(users);
        // Act
        String viewName = export.download(model);
        // Assert
        verify(model).addAttribute(eq("users"), any());
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_returnsEmptyString() {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Arrays.asList());
        // Act
        String viewName = export.download(model);
        // Assert
        assertEquals("", viewName);
    }
}

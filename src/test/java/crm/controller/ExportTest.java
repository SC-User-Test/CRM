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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private Export export;

    private Model model;

    @BeforeEach
    void setUp() {
        model = mock(Model.class);
    }

    @Test
    void testConstructor_ShouldInitializeWithUserService() {
        // Arrange & Act
        Export newExport = new Export(userService);

        // Assert
        assertNotNull(newExport);
    }

    @Test
    void testConstructor_WithNullUserService_ShouldAccept() {
        // Arrange, Act & Assert
        assertDoesNotThrow(() -> new Export(null));
    }

    @Test
    void testDownload_ShouldReturnEmptyString() {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        // Act
        String result = export.download(model);

        // Assert
        assertEquals("", result);
    }

    @Test
    void testDownload_ShouldCallUserServiceListAllUsers() {
        // Arrange
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.listAllUsers()).thenReturn(users);

        // Act
        export.download(model);

        // Assert
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_ShouldAddUsersToModel() {
        // Arrange
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.listAllUsers()).thenReturn(users);

        // Act
        export.download(model);

        // Assert
        verify(model).addAttribute("users", users);
    }

    @Test
    void testDownload_WithEmptyUserList_ShouldAddEmptyListToModel() {
        // Arrange
        List<User> emptyList = Collections.emptyList();
        when(userService.listAllUsers()).thenReturn(emptyList);

        // Act
        export.download(model);

        // Assert
        verify(model).addAttribute("users", emptyList);
    }

    @Test
    void testDownload_WithNullModel_ShouldThrowException() {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            export.download(null);
        });
    }
}

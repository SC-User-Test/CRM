package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private Export export;

    @BeforeEach
    void setUp() {
    }

    @Test
    void download_shouldAddUsersToModel() {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        // Act
        String result = export.download(model);

        // Assert
        verify(userService).listAllUsers();
        verify(model).addAttribute(eq("users"), any());
        assertEquals("", result);
    }

    @Test
    void export_shouldBeInstantiable() {
        // Assert
        assertNotNull(export);
    }
}

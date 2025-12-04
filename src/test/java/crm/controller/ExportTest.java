package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private Export exportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDownload() {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setUsername("testuser");
        users.add(user);

        when(userService.listAllUsers()).thenReturn(users);

        String result = exportController.download(model);

        assertEquals("", result);
        verify(userService, times(1)).listAllUsers();
        verify(model, times(1)).addAttribute("users", users);
    }

    @Test
    void testDownloadWithEmptyUsers() {
        when(userService.listAllUsers()).thenReturn(new ArrayList<>());

        String result = exportController.download(model);

        assertEquals("", result);
        verify(model, times(1)).addAttribute(eq("users"), any());
    }

    @Test
    void testDownloadAddsUsersToModel() {
        List<User> users = new ArrayList<>();
        when(userService.listAllUsers()).thenReturn(users);

        exportController.download(model);

        verify(model).addAttribute("users", users);
    }
}

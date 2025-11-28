package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportTest {

    @InjectMocks
    private Export export;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportConstructor() {
        Export controller = new Export(userService);
        assertNotNull(controller);
    }

    @Test
    void testDownload() {
        User user1 = User.builder().id(1L).username("user1").build();
        User user2 = User.builder().id(2L).username("user2").build();

        when(userService.listAllUsers()).thenReturn(Arrays.asList(user1, user2));

        String viewName = export.download(model);

        assertEquals("", viewName);
        verify(model).addAttribute(eq("users"), any());
        verify(userService).listAllUsers();
    }

    @Test
    void testDownloadReturnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        String result = export.download(model);

        assertEquals("", result);
    }

    @Test
    void testDownloadVerifyServiceCall() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        export.download(model);

        verify(userService, times(1)).listAllUsers();
        verify(model, times(1)).addAttribute(eq("users"), any());
    }
}

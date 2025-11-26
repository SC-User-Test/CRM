package crm.controller;

import crm.entity.User;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private Export exportController;

    private Model model;

    @BeforeEach
    public void setUp() {
        model = mock(Model.class);
    }

    @Test
    public void testConstructor() {
        UserService service = mock(UserService.class);
        Export export = new Export(service);
        assertNotNull(export);
    }

    @Test
    public void testDownload() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userService.listAllUsers()).thenReturn(users);

        String result = exportController.download(model);

        assertEquals("", result);
        verify(userService).listAllUsers();
        verify(model).addAttribute("users", users);
    }

    @Test
    public void testDownloadWithEmptyUserList() {
        List<User> emptyList = Arrays.asList();
        when(userService.listAllUsers()).thenReturn(emptyList);

        String result = exportController.download(model);

        assertEquals("", result);
        verify(model).addAttribute("users", emptyList);
    }

    @Test
    public void testDownloadWithNullUserService() {
        Export exportWithNull = new Export(null);
        assertNotNull(exportWithNull);
    }

    @Test
    public void testDownloadReturnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());
        String result = exportController.download(model);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

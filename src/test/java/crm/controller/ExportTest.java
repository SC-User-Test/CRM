package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExportTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private Export export;

    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        model = mock(Model.class);
    }

    @Test
    void testConstructor() {
        Export exportController = new Export(userService);
        assertNotNull(exportController);
    }

    @Test
    void testDownload_ReturnsEmptyString() {
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        String result = export.download(model);

        assertEquals("", result);
    }

    @Test
    void testDownload_CallsUserService() {
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        export.download(model);

        verify(userService, times(1)).listAllUsers();
    }

    @Test
    void testDownload_AddsUsersToModel() {
        when(userService.listAllUsers()).thenReturn(Collections.emptyList());

        export.download(model);

        verify(model, times(1)).addAttribute(eq("users"), any());
    }
}

package crm.controller;

import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExportTest {

    @Mock
    private UserService userService;

    @Test
    void testExportClassExists() {
        assertNotNull(Export.class);
    }

    @Test
    void testExportIsInstantiable() {
        Export export = new Export(userService);
        assertNotNull(export);
    }
}

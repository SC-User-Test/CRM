package crm.service;

import crm.entity.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceTest {

    @Test
    void testRoleServiceInterface() {
        // Assert that the interface exists
        assertNotNull(RoleService.class);
        assertTrue(RoleService.class.isInterface());
    }

    @Test
    void testListAllRolesMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(RoleService.class.getMethod("listAllRoles"));
    }

    @Test
    void testInterfaceHasExpectedNumberOfMethods() {
        // Assert that the interface has exactly 1 method
        assertEquals(1, RoleService.class.getDeclaredMethods().length);
    }
}

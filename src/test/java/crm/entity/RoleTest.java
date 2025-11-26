package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    void testConstructor() {
        Role newRole = new Role();
        assertNotNull(newRole);
    }

    @Test
    void testGetters() {
        assertEquals(1, role.getId());
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testSetters() {
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");

        assertEquals(2, newRole.getId());
        assertEquals("ROLE_ADMIN", newRole.getName());
    }

    @Test
    void testSetId() {
        role.setId(3);
        assertEquals(3, role.getId());
    }

    @Test
    void testSetName() {
        role.setName("ROLE_MANAGER");
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testSetName_WithNull() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testSetId_WithZero() {
        role.setId(0);
        assertEquals(0, role.getId());
    }
}

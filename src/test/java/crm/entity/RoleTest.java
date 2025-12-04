package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testRoleSettersAndGetters() {
        role.setId(1);
        role.setName("ROLE_ADMIN");

        assertEquals(1, role.getId());
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testRoleNoArgsConstructor() {
        Role newRole = new Role();
        assertNotNull(newRole);
        assertEquals(0, newRole.getId());
        assertNull(newRole.getName());
    }

    @Test
    void testRoleWithNullName() {
        role.setId(2);
        role.setName(null);

        assertEquals(2, role.getId());
        assertNull(role.getName());
    }

    @Test
    void testRoleWithEmptyName() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    void testRoleUserType() {
        role.setId(3);
        role.setName("ROLE_USER");

        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testRoleManagerType() {
        role.setId(4);
        role.setName("ROLE_MANAGER");

        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testRoleOwnerType() {
        role.setId(5);
        role.setName("ROLE_OWNER");

        assertEquals("ROLE_OWNER", role.getName());
    }

    @Test
    void testRoleIdBoundary() {
        role.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, role.getId());

        role.setId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, role.getId());
    }

    @Test
    void testRoleNameUpdate() {
        role.setName("ROLE_GUEST");
        assertEquals("ROLE_GUEST", role.getName());

        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());
    }
}

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
    void testRoleConstructor() {
        assertNotNull(role);
    }

    @Test
    void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testSetAndGetName() {
        role.setName("ADMIN");
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testSetAndGetNameWithUserRole() {
        role.setName("USER");
        assertEquals("USER", role.getName());
    }

    @Test
    void testSetAndGetNameWithNull() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testSetAndGetNameWithEmptyString() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    void testRoleWithDifferentIds() {
        role.setId(1);
        assertEquals(1, role.getId());

        role.setId(100);
        assertEquals(100, role.getId());
    }

    @Test
    void testRoleWithLongName() {
        String longName = "SUPER_ADMINISTRATOR_ROLE";
        role.setName(longName);
        assertEquals(longName, role.getName());
    }

    @Test
    void testRoleEqualsAndHashCode() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ADMIN");

        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void testRoleToString() {
        role.setId(1);
        role.setName("MANAGER");

        String toString = role.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("MANAGER"));
    }

    @Test
    void testRoleWithZeroId() {
        role.setId(0);
        assertEquals(0, role.getId());
    }

    @Test
    void testRoleWithNegativeId() {
        role.setId(-1);
        assertEquals(-1, role.getId());
    }
}

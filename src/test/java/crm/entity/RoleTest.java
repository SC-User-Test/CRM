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
    void testDefaultConstructor_createsInstance() {
        assertNotNull(role);
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testSetName_withAdminRole_returnsAdminRole() {
        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    void testSetName_withManagerRole_returnsManagerRole() {
        role.setName("ROLE_MANAGER");
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    void testSetId_withZero_returnsZero() {
        role.setId(0);
        assertEquals(0, role.getId());
    }

    @Test
    void testSetId_withNegativeValue_returnsNegativeValue() {
        role.setId(-1);
        assertEquals(-1, role.getId());
    }

    @Test
    void testEquals_equalRoles_returnsTrue() {
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(1);
        r2.setName("ROLE_USER");

        assertEquals(r1, r2);
    }

    @Test
    void testEquals_differentRoles_returnsFalse() {
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(2);
        r2.setName("ROLE_ADMIN");

        assertNotEquals(r1, r2);
    }

    @Test
    void testHashCode_equalRoles_sameHashCode() {
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(1);
        r2.setName("ROLE_USER");

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString_containsRoleName() {
        role.setId(1);
        role.setName("ROLE_USER");
        String toString = role.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ROLE_USER"));
    }
}

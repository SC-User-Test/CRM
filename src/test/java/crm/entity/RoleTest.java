package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
    }

    @Test
    public void testRoleCreation() {
        assertNotNull(role);
    }

    @Test
    public void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    public void testSetAndGetName() {
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    public void testRoleWithAdminRole() {
        role.setId(2);
        role.setName("ROLE_ADMIN");
        assertEquals(2, role.getId());
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    public void testRoleWithManagerRole() {
        role.setId(3);
        role.setName("ROLE_MANAGER");
        assertEquals(3, role.getId());
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    public void testRoleEquality() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_USER");

        assertEquals(role1, role2);
    }

    @Test
    public void testRoleToString() {
        role.setId(1);
        role.setName("ROLE_USER");
        String result = role.toString();
        assertNotNull(result);
        assertTrue(result.contains("ROLE_USER"));
    }
}

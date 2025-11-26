package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RoleTest {

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
    }

    @Test
    public void testConstructor() {
        Role newRole = new Role();
        assertNotNull(newRole);
    }

    @Test
    public void testSetAndGetId() {
        role.setId(1);
        assertEquals(1, role.getId());
    }

    @Test
    public void testSetAndGetName() {
        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    public void testIdDefaultValue() {
        Role newRole = new Role();
        assertEquals(0, newRole.getId());
    }

    @Test
    public void testNullName() {
        role.setName(null);
        assertNull(role.getName());
    }

    @Test
    public void testEmptyName() {
        role.setName("");
        assertEquals("", role.getName());
    }

    @Test
    public void testMultipleRoleNames() {
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());

        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());

        role.setName("ROLE_MANAGER");
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    public void testNegativeId() {
        role.setId(-1);
        assertEquals(-1, role.getId());
    }

    @Test
    public void testLargeId() {
        role.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, role.getId());
    }

    @Test
    public void testRoleNameWithSpecialCharacters() {
        role.setName("ROLE_USER_123");
        assertEquals("ROLE_USER_123", role.getName());
    }
}

package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
    }

    @Test
    void testBuilder() {
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testGetters() {
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertNotNull(user.getRole());
    }

    @Test
    void testSetters() {
        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("newuser");
        newUser.setEmail("new@example.com");
        newUser.setFirstName("Jane");
        newUser.setLastName("Smith");
        newUser.setPassword("newpass");
        newUser.setEnabled(0);
        newUser.setRole(role);

        assertEquals(2L, newUser.getId());
        assertEquals("newuser", newUser.getUsername());
        assertEquals("new@example.com", newUser.getEmail());
        assertEquals("Jane", newUser.getFirstName());
        assertEquals("Smith", newUser.getLastName());
        assertEquals("newpass", newUser.getPassword());
        assertEquals(0, newUser.getEnabled());
        assertNotNull(newUser.getRole());
    }

    @Test
    void testGetColumnCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    void testGetRole_id() {
        int roleId = user.getRole_id();
        assertEquals(1, roleId);
    }

    @Test
    void testGetRole_name() {
        String roleName = user.getRole_name();
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void testGetName() {
        String name = user.getName();
        assertEquals("John Doe", name);
    }

    @Test
    void testGetName_WithNullValues() {
        User newUser = new User();
        newUser.setFirstName(null);
        newUser.setLastName(null);

        String name = newUser.getName();
        assertEquals("null null", name);
    }
}

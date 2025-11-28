package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        role = new Role();
    }

    @Test
    void testUserConstructor() {
        assertNotNull(user);
    }

    @Test
    void testUserBuilder() {
        User builtUser = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();

        assertNotNull(builtUser);
        assertEquals(1L, builtUser.getId());
        assertEquals("johndoe", builtUser.getUsername());
        assertEquals("john@example.com", builtUser.getEmail());
        assertEquals("John", builtUser.getFirstName());
        assertEquals("Doe", builtUser.getLastName());
        assertEquals("password123", builtUser.getPassword());
        assertEquals(1, builtUser.getEnabled());
        assertEquals(role, builtUser.getRole());
    }

    @Test
    void testSetAndGetId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testSetAndGetEmail() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testSetAndGetFirstName() {
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("securePassword");
        assertEquals("securePassword", user.getPassword());
    }

    @Test
    void testSetAndGetEnabled() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());

        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetAndGetRole() {
        role.setId(1);
        role.setName("ADMIN");
        user.setRole(role);

        assertEquals(role, user.getRole());
        assertEquals(1, user.getRole().getId());
        assertEquals("ADMIN", user.getRole().getName());
    }

    @Test
    void testGetColumnCount() {
        int columnCount = user.getColumnCount();
        assertTrue(columnCount > 0);
    }

    @Test
    void testGetRoleId() {
        role.setId(5);
        user.setRole(role);

        assertEquals(5, user.getRole_id());
    }

    @Test
    void testGetRoleName() {
        role.setName("USER");
        user.setRole(role);

        assertEquals("USER", user.getRole_name());
    }

    @Test
    void testGetName() {
        user.setFirstName("John");
        user.setLastName("Doe");

        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetNameWithNullFirstName() {
        user.setFirstName(null);
        user.setLastName("Doe");

        assertEquals("null Doe", user.getName());
    }

    @Test
    void testGetNameWithNullLastName() {
        user.setFirstName("John");
        user.setLastName(null);

        assertEquals("John null", user.getName());
    }

    @Test
    void testUserWithNullValues() {
        user.setUsername(null);
        user.setEmail(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setPassword(null);
        user.setRole(null);

        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserToString() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        String toString = user.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("testuser"));
    }
}

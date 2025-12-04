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
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    void testUserBuilder() {
        User builtUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();

        assertNotNull(builtUser);
        assertEquals(1L, builtUser.getId());
        assertEquals("testuser", builtUser.getUsername());
        assertEquals("test@example.com", builtUser.getEmail());
        assertEquals("John", builtUser.getFirstName());
        assertEquals("Doe", builtUser.getLastName());
        assertEquals("password123", builtUser.getPassword());
        assertEquals(1, builtUser.getEnabled());
    }

    @Test
    void testUserSettersAndGetters() {
        user.setId(2L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("securepass");
        user.setEnabled(1);
        user.setRole(role);

        assertEquals(2L, user.getId());
        assertEquals("johndoe", user.getUsername());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("securepass", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertEquals(role, user.getRole());
    }

    @Test
    void testGetName() {
        user.setFirstName("Jane");
        user.setLastName("Smith");

        assertEquals("Jane Smith", user.getName());
    }

    @Test
    void testGetNameWithNullValues() {
        user.setFirstName(null);
        user.setLastName(null);

        assertEquals("null null", user.getName());
    }

    @Test
    void testGetRoleId() {
        user.setRole(role);
        assertEquals(1, user.getRole_id());
    }

    @Test
    void testGetRoleName() {
        user.setRole(role);
        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    void testGetColumnCount() {
        int columnCount = user.getColumnCount();
        assertTrue(columnCount > 0);
    }

    @Test
    void testUserNoArgsConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
        assertNull(newUser.getId());
        assertNull(newUser.getUsername());
        assertNull(newUser.getEmail());
        assertNull(newUser.getFirstName());
        assertNull(newUser.getLastName());
        assertNull(newUser.getPassword());
        assertEquals(0, newUser.getEnabled());
        assertNull(newUser.getRole());
    }

    @Test
    void testUserAllArgsConstructor() {
        User allArgsUser = new User(
                3L,
                "alice",
                "alice@example.com",
                "Alice",
                "Johnson",
                "password",
                1,
                role
        );

        assertEquals(3L, allArgsUser.getId());
        assertEquals("alice", allArgsUser.getUsername());
        assertEquals("alice@example.com", allArgsUser.getEmail());
        assertEquals("Alice", allArgsUser.getFirstName());
        assertEquals("Johnson", allArgsUser.getLastName());
        assertEquals("password", allArgsUser.getPassword());
        assertEquals(1, allArgsUser.getEnabled());
        assertEquals(role, allArgsUser.getRole());
    }

    @Test
    void testUserWithDisabledFlag() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());

        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testUserEmailValidation() {
        user.setEmail("valid@example.com");
        assertEquals("valid@example.com", user.getEmail());
    }
}

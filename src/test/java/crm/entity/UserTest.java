package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testDefaultConstructor() {
        User u = new User();
        assertNotNull(u);
    }

    @Test
    void testAllArgsConstructor() {
        User u = new User(1L, "testuser", "test@example.com", "Test", "User", "pass", 1, role);
        assertNotNull(u);
        assertEquals("testuser", u.getUsername());
        assertEquals("test@example.com", u.getEmail());
    }

    @Test
    void testBuilderPattern() {
        User u = User.builder()
                .id(2L)
                .username("builder_user")
                .email("builder@example.com")
                .firstName("Builder")
                .lastName("User")
                .password("builderpass")
                .enabled(1)
                .role(role)
                .build();
        assertNotNull(u);
        assertEquals("builder_user", u.getUsername());
        assertEquals("builder@example.com", u.getEmail());
    }

    @Test
    void testGetId() {
        assertEquals(1L, user.getId());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("newusername");
        assertEquals("newusername", user.getUsername());
    }

    @Test
    void testSetAndGetEmail() {
        user.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", user.getEmail());
    }

    @Test
    void testSetAndGetFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetAndGetEnabled() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetAndGetRole() {
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");
        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
    }

    @Test
    void testGetName() {
        String name = user.getName();
        assertEquals("John Doe", name);
    }

    @Test
    void testGetNameWithNullFirstName() {
        user.setFirstName(null);
        // getName() returns firstName + " " + lastName
        String name = user.getName();
        assertEquals("null Doe", name);
    }

    @Test
    void testGetRoleId() {
        int roleId = user.getRole_id();
        assertEquals(1, roleId);
    }

    @Test
    void testGetRoleName() {
        String roleName = user.getRole_name();
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void testGetColumnCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    void testEqualsAndHashCode() {
        User u1 = User.builder().id(1L).username("johndoe").email("john@example.com")
                .firstName("John").lastName("Doe").password("pass").enabled(1).role(role).build();
        User u2 = User.builder().id(1L).username("johndoe").email("john@example.com")
                .firstName("John").lastName("Doe").password("pass").enabled(1).role(role).build();
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testNotEquals() {
        User u1 = User.builder().id(1L).username("user1").email("user1@example.com").build();
        User u2 = User.builder().id(2L).username("user2").email("user2@example.com").build();
        assertNotEquals(u1, u2);
    }

    @Test
    void testToString() {
        String str = user.toString();
        assertNotNull(str);
        assertTrue(str.contains("johndoe"));
    }

    @Test
    void testSetEnabledToZero() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetUsernameNull() {
        user.setUsername(null);
        assertNull(user.getUsername());
    }
}

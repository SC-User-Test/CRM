package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
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
    public void testUserCreation() {
        assertNotNull(user);
    }

    @Test
    public void testUserBuilder() {
        User newUser = User.builder()
                .id(2L)
                .username("newuser")
                .email("new@example.com")
                .build();
        assertNotNull(newUser);
        assertEquals(2L, newUser.getId());
        assertEquals("newuser", newUser.getUsername());
    }

    @Test
    public void testGetId() {
        assertEquals(1L, user.getId());
    }

    @Test
    public void testGetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testGetEmail() {
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testGetEnabled() {
        assertEquals(1, user.getEnabled());
    }

    @Test
    public void testGetRole() {
        assertNotNull(user.getRole());
        assertEquals("ROLE_USER", user.getRole().getName());
    }

    @Test
    public void testGetColumnCount() {
        int columnCount = user.getColumnCount();
        assertTrue(columnCount > 0);
    }

    @Test
    public void testGetRole_id() {
        int roleId = user.getRole_id();
        assertEquals(1, roleId);
    }

    @Test
    public void testGetRole_name() {
        String roleName = user.getRole_name();
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    public void testGetName() {
        String fullName = user.getName();
        assertEquals("John Doe", fullName);
    }

    @Test
    public void testSetUsername() {
        user.setUsername("updateduser");
        assertEquals("updateduser", user.getUsername());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("updated@example.com");
        assertEquals("updated@example.com", user.getEmail());
    }

    @Test
    public void testSetEnabled() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    public void testSetRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);
        assertEquals("ROLE_ADMIN", user.getRole().getName());
    }

    @Test
    public void testUserEquality() {
        User user1 = User.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .build();
        User user2 = User.builder()
                .id(1L)
                .username("test")
                .email("test@test.com")
                .build();
        assertEquals(user1, user2);
    }

    @Test
    public void testUserWithNullRole() {
        User userNoRole = User.builder()
                .id(3L)
                .username("norole")
                .email("norole@example.com")
                .enabled(1)
                .build();
        assertNull(userNoRole.getRole());
    }
}

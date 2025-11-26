package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        user = new User();
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    public void testConstructor() {
        User newUser = new User();
        assertNotNull(newUser);
    }

    @Test
    public void testBuilderPattern() {
        User builtUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(role)
                .build();

        assertNotNull(builtUser);
        assertEquals(1L, builtUser.getId());
        assertEquals("testuser", builtUser.getUsername());
    }

    @Test
    public void testSetAndGetId() {
        user.setId(1L);
        assertEquals(1L, user.getId());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testSetAndGetEmail() {
        user.setEmail("test@example.com");
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testSetAndGetFirstName() {
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testSetAndGetEnabled() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    public void testSetAndGetRole() {
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    public void testGetColumnCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    public void testGetRoleId() {
        user.setRole(role);
        assertEquals(1, user.getRole_id());
    }

    @Test
    public void testGetRoleName() {
        user.setRole(role);
        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    public void testGetName() {
        user.setFirstName("John");
        user.setLastName("Doe");
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testGetNameWithNullValues() {
        user.setFirstName(null);
        user.setLastName(null);
        assertEquals("null null", user.getName());
    }

    @Test
    public void testGetNameWithEmptyStrings() {
        user.setFirstName("");
        user.setLastName("");
        assertEquals(" ", user.getName());
    }

    @Test
    public void testEnabledDefaultValue() {
        User newUser = new User();
        assertEquals(0, newUser.getEnabled());
    }

    @Test
    public void testAllArgsConstructor() {
        User newUser = new User(2L, "user2", "user2@test.com", "Jane", "Smith", "pass456", 1, role);
        assertNotNull(newUser);
        assertEquals(2L, newUser.getId());
        assertEquals("user2", newUser.getUsername());
        assertEquals("user2@test.com", newUser.getEmail());
        assertEquals("Jane", newUser.getFirstName());
        assertEquals("Smith", newUser.getLastName());
    }
}

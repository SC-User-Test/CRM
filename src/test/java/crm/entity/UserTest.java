package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        // Assert
        assertNotNull(user);
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long expectedId = 1L;

        // Act
        user.setId(expectedId);

        // Assert
        assertEquals(expectedId, user.getId());
    }

    @Test
    void testSetAndGetUsername() {
        // Arrange
        String expectedUsername = "testuser";

        // Act
        user.setUsername(expectedUsername);

        // Assert
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    void testSetAndGetEmail() {
        // Arrange
        String expectedEmail = "test@example.com";

        // Act
        user.setEmail(expectedEmail);

        // Assert
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    void testSetAndGetFirstName() {
        // Arrange
        String expectedFirstName = "John";

        // Act
        user.setFirstName(expectedFirstName);

        // Assert
        assertEquals(expectedFirstName, user.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        // Arrange
        String expectedLastName = "Doe";

        // Act
        user.setLastName(expectedLastName);

        // Assert
        assertEquals(expectedLastName, user.getLastName());
    }

    @Test
    void testSetAndGetPassword() {
        // Arrange
        String expectedPassword = "password123";

        // Act
        user.setPassword(expectedPassword);

        // Assert
        assertEquals(expectedPassword, user.getPassword());
    }

    @Test
    void testSetAndGetEnabled() {
        // Arrange
        int expectedEnabled = 1;

        // Act
        user.setEnabled(expectedEnabled);

        // Assert
        assertEquals(expectedEnabled, user.getEnabled());
    }

    @Test
    void testSetAndGetRole() {
        // Arrange
        Role expectedRole = new Role();
        expectedRole.setId(1);
        expectedRole.setName("ROLE_USER");

        // Act
        user.setRole(expectedRole);

        // Assert
        assertEquals(expectedRole, user.getRole());
    }

    @Test
    void testGetName() {
        // Arrange
        user.setFirstName("John");
        user.setLastName("Doe");

        // Act
        String fullName = user.getName();

        // Assert
        assertEquals("John Doe", fullName);
    }

    @Test
    void testGetName_withNullFirstName() {
        // Arrange
        user.setFirstName(null);
        user.setLastName("Doe");

        // Act
        String fullName = user.getName();

        // Assert
        assertEquals("null Doe", fullName);
    }

    @Test
    void testGetName_withNullLastName() {
        // Arrange
        user.setFirstName("John");
        user.setLastName(null);

        // Act
        String fullName = user.getName();

        // Assert
        assertEquals("John null", fullName);
    }

    @Test
    void testGetRoleId() {
        // Arrange
        Role role = new Role();
        role.setId(5);
        user.setRole(role);

        // Act
        int roleId = user.getRole_id();

        // Assert
        assertEquals(5, roleId);
    }

    @Test
    void testGetRoleName() {
        // Arrange
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        user.setRole(role);

        // Act
        String roleName = user.getRole_name();

        // Assert
        assertEquals("ROLE_ADMIN", roleName);
    }

    @Test
    void testGetColumnCount() {
        // Act
        int columnCount = user.getColumnCount();

        // Assert
        assertTrue(columnCount > 0);
    }

    @Test
    void testBuilderPattern() {
        // Arrange & Act
        User builtUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .build();

        // Assert
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
    void testAllArgsConstructor() {
        // Arrange
        Role role = new Role();

        // Act
        User user = new User(
                1L,
                "testuser",
                "test@example.com",
                "John",
                "Doe",
                "password123",
                1,
                role
        );

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        User user = new User();

        // Assert
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
    }

    @Test
    void testUserEquality() {
        // Arrange
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

        // Assert
        assertEquals(user1, user2);
    }

    @Test
    void testUserHashCode() {
        // Arrange
        user.setId(1L);
        user.setUsername("testuser");

        // Act
        int hashCode = user.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void testUserToString() {
        // Arrange
        user.setId(1L);
        user.setUsername("testuser");

        // Act
        String toString = user.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("User"));
    }

    @Test
    void testEntityAnnotationPresent() {
        // Assert
        assertTrue(User.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void testSetNullValues() {
        // Act
        user.setId(null);
        user.setUsername(null);
        user.setEmail(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setPassword(null);
        user.setRole(null);

        // Assert
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void testGetRoleId_withNullRole() {
        // Arrange
        user.setRole(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            user.getRole_id();
        });
    }

    @Test
    void testGetRoleName_withNullRole() {
        // Arrange
        user.setRole(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            user.getRole_name();
        });
    }
}

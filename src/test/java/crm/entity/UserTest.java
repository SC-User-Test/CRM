package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
    void user_shouldBeCreated() {
        // Assert
        assertNotNull(user);
    }

    @Test
    void builder_shouldCreateUserWithAllFields() {
        // Arrange & Act
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();

        // Assert
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("password123", user.getPassword());
        assertEquals(1, user.getEnabled());
        assertEquals(role, user.getRole());
    }

    @Test
    void setId_shouldSetIdCorrectly() {
        // Arrange
        Long expectedId = 100L;

        // Act
        user.setId(expectedId);

        // Assert
        assertEquals(expectedId, user.getId());
    }

    @Test
    void setUsername_shouldSetUsernameCorrectly() {
        // Arrange
        String expectedUsername = "johndoe";

        // Act
        user.setUsername(expectedUsername);

        // Assert
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    void setEmail_shouldSetEmailCorrectly() {
        // Arrange
        String expectedEmail = "john@example.com";

        // Act
        user.setEmail(expectedEmail);

        // Assert
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    void setFirstName_shouldSetFirstNameCorrectly() {
        // Arrange
        String expectedFirstName = "Jane";

        // Act
        user.setFirstName(expectedFirstName);

        // Assert
        assertEquals(expectedFirstName, user.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastNameCorrectly() {
        // Arrange
        String expectedLastName = "Smith";

        // Act
        user.setLastName(expectedLastName);

        // Assert
        assertEquals(expectedLastName, user.getLastName());
    }

    @Test
    void setPassword_shouldSetPasswordCorrectly() {
        // Arrange
        String expectedPassword = "securePassword";

        // Act
        user.setPassword(expectedPassword);

        // Assert
        assertEquals(expectedPassword, user.getPassword());
    }

    @Test
    void setEnabled_shouldSetEnabledCorrectly() {
        // Arrange
        int expectedEnabled = 1;

        // Act
        user.setEnabled(expectedEnabled);

        // Assert
        assertEquals(expectedEnabled, user.getEnabled());
    }

    @Test
    void setRole_shouldSetRoleCorrectly() {
        // Act
        user.setRole(role);

        // Assert
        assertEquals(role, user.getRole());
    }

    @Test
    void getColumnCount_shouldReturnNumberOfFields() {
        // Act
        int columnCount = user.getColumnCount();

        // Assert
        assertTrue(columnCount > 0);
    }

    @Test
    void getRole_id_shouldReturnRoleId() {
        // Arrange
        user.setRole(role);

        // Act
        int roleId = user.getRole_id();

        // Assert
        assertEquals(1, roleId);
    }

    @Test
    void getRole_name_shouldReturnRoleName() {
        // Arrange
        user.setRole(role);

        // Act
        String roleName = user.getRole_name();

        // Assert
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void getName_shouldReturnFullName() {
        // Arrange
        user.setFirstName("John");
        user.setLastName("Doe");

        // Act
        String fullName = user.getName();

        // Assert
        assertEquals("John Doe", fullName);
    }

    @Test
    void user_withNoArgsConstructor_shouldCreateEmptyUser() {
        // Act
        User emptyUser = new User();

        // Assert
        assertNotNull(emptyUser);
    }

    @Test
    void user_withAllArgsConstructor_shouldCreateFullUser() {
        // Act
        User fullUser = new User(1L, "username", "email@test.com", "First", "Last", "password", 1, role);

        // Assert
        assertNotNull(fullUser);
        assertEquals(1L, fullUser.getId());
        assertEquals("username", fullUser.getUsername());
    }

    @Test
    void user_shouldSupportEqualsAndHashCode() {
        // Arrange
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

        // Assert
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}

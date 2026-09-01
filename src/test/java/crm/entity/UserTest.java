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

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        User u = new User();
        // Assert
        assertNotNull(u);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        // Arrange & Act
        User u = new User(1L, "testuser", "test@example.com", "Test", "User", "password", 1, role);
        // Assert
        assertNotNull(u);
        assertEquals(1L, u.getId());
        assertEquals("testuser", u.getUsername());
        assertEquals("test@example.com", u.getEmail());
        assertEquals("Test", u.getFirstName());
        assertEquals("User", u.getLastName());
        assertEquals("password", u.getPassword());
        assertEquals(1, u.getEnabled());
        assertEquals(role, u.getRole());
    }

    @Test
    void testBuilder_createsUserWithAllFields() {
        // Arrange & Act
        User u = User.builder()
                .id(2L)
                .username("janedoe")
                .email("jane@example.com")
                .firstName("Jane")
                .lastName("Doe")
                .password("pass123")
                .enabled(1)
                .role(role)
                .build();
        // Assert
        assertNotNull(u);
        assertEquals(2L, u.getId());
        assertEquals("janedoe", u.getUsername());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        // Arrange
        Long expectedId = 10L;
        // Act
        user.setId(expectedId);
        // Assert
        assertEquals(expectedId, user.getId());
    }

    @Test
    void testSetAndGetUsername_returnsCorrectUsername() {
        // Arrange
        String expectedUsername = "newuser";
        // Act
        user.setUsername(expectedUsername);
        // Assert
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    void testSetAndGetEmail_returnsCorrectEmail() {
        // Arrange
        String expectedEmail = "new@example.com";
        // Act
        user.setEmail(expectedEmail);
        // Assert
        assertEquals(expectedEmail, user.getEmail());
    }

    @Test
    void testSetAndGetFirstName_returnsCorrectFirstName() {
        // Arrange
        String expectedFirstName = "Alice";
        // Act
        user.setFirstName(expectedFirstName);
        // Assert
        assertEquals(expectedFirstName, user.getFirstName());
    }

    @Test
    void testSetAndGetLastName_returnsCorrectLastName() {
        // Arrange
        String expectedLastName = "Smith";
        // Act
        user.setLastName(expectedLastName);
        // Assert
        assertEquals(expectedLastName, user.getLastName());
    }

    @Test
    void testSetAndGetPassword_returnsCorrectPassword() {
        // Arrange
        String expectedPassword = "newpassword";
        // Act
        user.setPassword(expectedPassword);
        // Assert
        assertEquals(expectedPassword, user.getPassword());
    }

    @Test
    void testSetAndGetEnabled_returnsCorrectEnabled() {
        // Arrange
        int expectedEnabled = 0;
        // Act
        user.setEnabled(expectedEnabled);
        // Assert
        assertEquals(expectedEnabled, user.getEnabled());
    }

    @Test
    void testSetAndGetRole_returnsCorrectRole() {
        // Arrange
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");
        // Act
        user.setRole(newRole);
        // Assert
        assertEquals(newRole, user.getRole());
    }

    @Test
    void testGetName_returnsFirstNameAndLastName() {
        // Arrange
        user.setFirstName("John");
        user.setLastName("Doe");
        // Act
        String name = user.getName();
        // Assert
        assertEquals("John Doe", name);
    }

    @Test
    void testGetName_withDifferentNames_returnsCorrectCombination() {
        // Arrange
        user.setFirstName("Alice");
        user.setLastName("Wonderland");
        // Act
        String name = user.getName();
        // Assert
        assertEquals("Alice Wonderland", name);
    }

    @Test
    void testGetRole_id_returnsRoleId() {
        // Arrange & Act
        int roleId = user.getRole_id();
        // Assert
        assertEquals(1, roleId);
    }

    @Test
    void testGetRole_name_returnsRoleName() {
        // Arrange & Act
        String roleName = user.getRole_name();
        // Assert
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void testGetColumnCount_returnsNumberOfFields() {
        // Arrange & Act
        int count = user.getColumnCount();
        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testEquals_equalUsers_returnsTrue() {
        // Arrange
        User u1 = User.builder().id(1L).username("user1").email("u1@test.com").build();
        User u2 = User.builder().id(1L).username("user1").email("u1@test.com").build();
        // Act & Assert
        assertEquals(u1, u2);
    }

    @Test
    void testEquals_differentUsers_returnsFalse() {
        // Arrange
        User u1 = User.builder().id(1L).username("user1").build();
        User u2 = User.builder().id(2L).username("user2").build();
        // Act & Assert
        assertNotEquals(u1, u2);
    }

    @Test
    void testHashCode_equalUsers_sameHashCode() {
        // Arrange
        User u1 = User.builder().id(1L).username("user1").build();
        User u2 = User.builder().id(1L).username("user1").build();
        // Act & Assert
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToString_notNull() {
        // Arrange & Act
        String result = user.toString();
        // Assert
        assertNotNull(result);
    }
}

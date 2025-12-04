package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testNoArgsConstructor_ShouldCreateInstance() {
        // Arrange & Act
        User newUser = new User();

        // Assert
        assertNotNull(newUser);
    }

    @Test
    void testAllArgsConstructor_ShouldCreateInstanceWithAllFields() {
        // Arrange & Act
        User newUser = new User(
                2L,
                "newuser",
                "new@example.com",
                "Jane",
                "Smith",
                "password456",
                1,
                role
        );

        // Assert
        assertNotNull(newUser);
        assertEquals(2L, newUser.getId());
        assertEquals("newuser", newUser.getUsername());
        assertEquals("new@example.com", newUser.getEmail());
    }

    @Test
    void testBuilder_ShouldCreateInstanceWithBuilder() {
        // Arrange & Act
        User newUser = User.builder()
                .id(3L)
                .username("builderuser")
                .email("builder@example.com")
                .firstName("Builder")
                .lastName("User")
                .build();

        // Assert
        assertNotNull(newUser);
        assertEquals(3L, newUser.getId());
        assertEquals("builderuser", newUser.getUsername());
    }

    @Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        User newUser = new User();

        // Act
        newUser.setId(5L);
        newUser.setUsername("setteruser");
        newUser.setEmail("setter@example.com");

        // Assert
        assertEquals(5L, newUser.getId());
        assertEquals("setteruser", newUser.getUsername());
        assertEquals("setter@example.com", newUser.getEmail());
    }

    @Test
    void testSetUsername_ShouldUpdateUsername() {
        // Arrange & Act
        user.setUsername("updateduser");

        // Assert
        assertEquals("updateduser", user.getUsername());
    }

    @Test
    void testSetEmail_ShouldUpdateEmail() {
        // Arrange & Act
        user.setEmail("updated@example.com");

        // Assert
        assertEquals("updated@example.com", user.getEmail());
    }

    @Test
    void testSetFirstName_ShouldUpdateFirstName() {
        // Arrange & Act
        user.setFirstName("UpdatedFirst");

        // Assert
        assertEquals("UpdatedFirst", user.getFirstName());
    }

    @Test
    void testSetLastName_ShouldUpdateLastName() {
        // Arrange & Act
        user.setLastName("UpdatedLast");

        // Assert
        assertEquals("UpdatedLast", user.getLastName());
    }

    @Test
    void testSetPassword_ShouldUpdatePassword() {
        // Arrange & Act
        user.setPassword("newpassword");

        // Assert
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetEnabled_ShouldUpdateEnabled() {
        // Arrange & Act
        user.setEnabled(0);

        // Assert
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetRole_ShouldUpdateRole() {
        // Arrange
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");

        // Act
        user.setRole(newRole);

        // Assert
        assertEquals(newRole, user.getRole());
        assertEquals(2, user.getRole().getId());
    }

    @Test
    void testGetColumnCount_ShouldReturnNumberOfDeclaredFields() {
        // Arrange & Act
        int count = user.getColumnCount();

        // Assert
        assertTrue(count > 0);
    }

    @Test
    void testGetRole_id_ShouldReturnRoleId() {
        // Arrange & Act
        int roleId = user.getRole_id();

        // Assert
        assertEquals(1, roleId);
    }

    @Test
    void testGetRole_name_ShouldReturnRoleName() {
        // Arrange & Act
        String roleName = user.getRole_name();

        // Assert
        assertEquals("ROLE_USER", roleName);
    }

    @Test
    void testGetName_ShouldReturnFullName() {
        // Arrange & Act
        String fullName = user.getName();

        // Assert
        assertEquals("John Doe", fullName);
    }

    @Test
    void testGetName_WithNullFirstName_ShouldConcatenateWithSpace() {
        // Arrange
        user.setFirstName(null);

        // Act
        String fullName = user.getName();

        // Assert
        assertEquals("null Doe", fullName);
    }

    @Test
    void testGetName_WithNullLastName_ShouldConcatenateWithSpace() {
        // Arrange
        user.setLastName(null);

        // Act
        String fullName = user.getName();

        // Assert
        assertEquals("John null", fullName);
    }

    @Test
    void testSetNullValues_ShouldAcceptNull() {
        // Arrange & Act
        user.setUsername(null);
        user.setEmail(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setPassword(null);
        user.setRole(null);

        // Assert
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }
}

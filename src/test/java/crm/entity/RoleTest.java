package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void role_shouldBeCreated() {
        // Assert
        assertNotNull(role);
    }

    @Test
    void setId_shouldSetIdCorrectly() {
        // Arrange
        int expectedId = 1;

        // Act
        role.setId(expectedId);

        // Assert
        assertEquals(expectedId, role.getId());
    }

    @Test
    void setName_shouldSetNameCorrectly() {
        // Arrange
        String expectedName = "ROLE_ADMIN";

        // Act
        role.setName(expectedName);

        // Assert
        assertEquals(expectedName, role.getName());
    }

    @Test
    void role_withAllFields_shouldStoreCorrectly() {
        // Arrange
        int id = 5;
        String name = "ROLE_USER";

        // Act
        role.setId(id);
        role.setName(name);

        // Assert
        assertEquals(id, role.getId());
        assertEquals(name, role.getName());
    }

    @Test
    void role_shouldSupportEqualsAndHashCode() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");

        // Assert
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void role_shouldSupportToString() {
        // Arrange
        role.setId(1);
        role.setName("ROLE_MANAGER");

        // Act
        String result = role.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("ROLE_MANAGER"));
    }
}

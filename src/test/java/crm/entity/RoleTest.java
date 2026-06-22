package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
    }

    @Test
    void testRoleCreation() {
        // Assert
        assertNotNull(role);
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        int expectedId = 1;

        // Act
        role.setId(expectedId);

        // Assert
        assertEquals(expectedId, role.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        String expectedName = "ROLE_ADMIN";

        // Act
        role.setName(expectedName);

        // Assert
        assertEquals(expectedName, role.getName());
    }

    @Test
    void testSetAndGetName_withNullValue() {
        // Act
        role.setName(null);

        // Assert
        assertNull(role.getName());
    }

    @Test
    void testSetAndGetName_withEmptyString() {
        // Arrange
        String emptyName = "";

        // Act
        role.setName(emptyName);

        // Assert
        assertEquals(emptyName, role.getName());
    }

    @Test
    void testSetAndGetId_withZeroValue() {
        // Act
        role.setId(0);

        // Assert
        assertEquals(0, role.getId());
    }

    @Test
    void testSetAndGetId_withNegativeValue() {
        // Arrange
        int negativeId = -1;

        // Act
        role.setId(negativeId);

        // Assert
        assertEquals(negativeId, role.getId());
    }

    @Test
    void testRoleEquality() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_USER");

        // Assert - Lombok @Data generates equals method
        assertEquals(role1, role2);
    }

    @Test
    void testRoleInequality() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        // Assert
        assertNotEquals(role1, role2);
    }

    @Test
    void testRoleHashCode() {
        // Arrange
        role.setId(1);
        role.setName("ROLE_USER");

        // Act
        int hashCode = role.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void testRoleToString() {
        // Arrange
        role.setId(1);
        role.setName("ROLE_ADMIN");

        // Act
        String toString = role.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Role"));
    }

    @Test
    void testEntityAnnotationPresent() {
        // Assert
        assertTrue(Role.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void testTableAnnotationPresent() {
        // Assert
        assertTrue(Role.class.isAnnotationPresent(jakarta.persistence.Table.class));
    }

    @Test
    void testSetMultipleRoleNames() {
        // Test ROLE_USER
        role.setName("ROLE_USER");
        assertEquals("ROLE_USER", role.getName());

        // Test ROLE_ADMIN
        role.setName("ROLE_ADMIN");
        assertEquals("ROLE_ADMIN", role.getName());

        // Test ROLE_MANAGER
        role.setName("ROLE_MANAGER");
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testRoleWithLongName() {
        // Arrange
        String longName = "ROLE_" + "A".repeat(100);

        // Act
        role.setName(longName);

        // Assert
        assertEquals(longName, role.getName());
    }
}

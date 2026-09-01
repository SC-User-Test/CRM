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
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        Role r = new Role();
        // Assert
        assertNotNull(r);
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        // Arrange
        int expectedId = 1;
        // Act
        role.setId(expectedId);
        // Assert
        assertEquals(expectedId, role.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        // Arrange
        String expectedName = "ROLE_ADMIN";
        // Act
        role.setName(expectedName);
        // Assert
        assertEquals(expectedName, role.getName());
    }

    @Test
    void testSetId_withZero_returnsZero() {
        // Arrange & Act
        role.setId(0);
        // Assert
        assertEquals(0, role.getId());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        // Arrange & Act
        role.setName(null);
        // Assert
        assertNull(role.getName());
    }

    @Test
    void testSetName_withRoleUser_returnsRoleUser() {
        // Arrange & Act
        role.setName("ROLE_USER");
        // Assert
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    void testSetName_withRoleManager_returnsRoleManager() {
        // Arrange & Act
        role.setName("ROLE_MANAGER");
        // Assert
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testEquals_equalRoles_returnsTrue() {
        // Arrange
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(1);
        r2.setName("ROLE_USER");
        // Act & Assert
        assertEquals(r1, r2);
    }

    @Test
    void testEquals_differentRoles_returnsFalse() {
        // Arrange
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(2);
        r2.setName("ROLE_ADMIN");
        // Act & Assert
        assertNotEquals(r1, r2);
    }

    @Test
    void testHashCode_equalRoles_sameHashCode() {
        // Arrange
        Role r1 = new Role();
        r1.setId(1);
        r1.setName("ROLE_USER");

        Role r2 = new Role();
        r2.setId(1);
        r2.setName("ROLE_USER");
        // Act & Assert
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testToString_containsFieldValues() {
        // Arrange
        role.setId(1);
        role.setName("ROLE_ADMIN");
        // Act
        String result = role.toString();
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("ROLE_ADMIN"));
    }
}

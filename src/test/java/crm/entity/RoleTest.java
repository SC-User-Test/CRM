package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        Role newRole = new Role();

        // Assert
        assertNotNull(newRole);
    }

    @Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Role newRole = new Role();

        // Act
        newRole.setId(5);
        newRole.setName("ROLE_ADMIN");

        // Assert
        assertEquals(5, newRole.getId());
        assertEquals("ROLE_ADMIN", newRole.getName());
    }

    @Test
    void testSetId_ShouldUpdateId() {
        // Arrange & Act
        role.setId(99);

        // Assert
        assertEquals(99, role.getId());
    }

    @Test
    void testSetName_ShouldUpdateName() {
        // Arrange & Act
        role.setName("ROLE_MANAGER");

        // Assert
        assertEquals("ROLE_MANAGER", role.getName());
    }

    @Test
    void testGetId_ShouldReturnCorrectId() {
        // Arrange & Act
        int id = role.getId();

        // Assert
        assertEquals(1, id);
    }

    @Test
    void testGetName_ShouldReturnCorrectName() {
        // Arrange & Act
        String name = role.getName();

        // Assert
        assertEquals("ROLE_USER", name);
    }

    @Test
    void testSetName_WithNullValue_ShouldAcceptNull() {
        // Arrange & Act
        role.setName(null);

        // Assert
        assertNull(role.getName());
    }

    @Test
    void testSetName_WithEmptyString_ShouldAcceptEmptyString() {
        // Arrange & Act
        role.setName("");

        // Assert
        assertEquals("", role.getName());
    }

    @Test
    void testSetId_WithZeroValue_ShouldAcceptZero() {
        // Arrange & Act
        role.setId(0);

        // Assert
        assertEquals(0, role.getId());
    }

    @Test
    void testSetId_WithNegativeValue_ShouldAcceptNegative() {
        // Arrange & Act
        role.setId(-1);

        // Assert
        assertEquals(-1, role.getId());
    }

    @Test
    void testSetName_WithLongString_ShouldAcceptLongString() {
        // Arrange
        String longName = "ROLE_" + "A".repeat(250);

        // Act
        role.setName(longName);

        // Assert
        assertEquals(longName, role.getName());
    }

    @Test
    void testEquals_WithSameId_ShouldBeEqual() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");

        // Act & Assert
        assertEquals(role1, role2);
    }

    @Test
    void testHashCode_WithSameId_ShouldHaveSameHashCode() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(1);
        role2.setName("ROLE_ADMIN");

        // Act & Assert
        assertEquals(role1.hashCode(), role2.hashCode());
    }
}

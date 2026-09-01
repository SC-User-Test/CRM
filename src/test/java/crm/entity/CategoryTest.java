package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        Category cat = new Category();
        // Assert
        assertNotNull(cat);
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        // Arrange
        Long expectedId = 1L;
        // Act
        category.setId(expectedId);
        // Assert
        assertEquals(expectedId, category.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        // Arrange
        String expectedName = "Technology";
        // Act
        category.setName(expectedName);
        // Assert
        assertEquals(expectedName, category.getName());
    }

    @Test
    void testSetId_withNull_returnsNull() {
        // Arrange & Act
        category.setId(null);
        // Assert
        assertNull(category.getId());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        // Arrange & Act
        category.setName(null);
        // Assert
        assertNull(category.getName());
    }

    @Test
    void testSetName_withEmptyString_returnsEmpty() {
        // Arrange & Act
        category.setName("");
        // Assert
        assertEquals("", category.getName());
    }

    @Test
    void testEquals_sameObject_returnsTrue() {
        // Arrange
        category.setId(1L);
        category.setName("Tech");
        // Act & Assert
        assertEquals(category, category);
    }

    @Test
    void testEquals_equalObjects_returnsTrue() {
        // Arrange
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Tech");

        Category cat2 = new Category();
        cat2.setId(1L);
        cat2.setName("Tech");
        // Act & Assert
        assertEquals(cat1, cat2);
    }

    @Test
    void testEquals_differentObjects_returnsFalse() {
        // Arrange
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Tech");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Finance");
        // Act & Assert
        assertNotEquals(cat1, cat2);
    }

    @Test
    void testHashCode_equalObjects_sameHashCode() {
        // Arrange
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Tech");

        Category cat2 = new Category();
        cat2.setId(1L);
        cat2.setName("Tech");
        // Act & Assert
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testToString_containsFieldValues() {
        // Arrange
        category.setId(1L);
        category.setName("Technology");
        // Act
        String result = category.toString();
        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Technology"));
    }
}

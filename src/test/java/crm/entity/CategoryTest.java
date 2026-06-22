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
    void testCategoryCreation() {
        // Assert
        assertNotNull(category);
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long expectedId = 1L;

        // Act
        category.setId(expectedId);

        // Assert
        assertEquals(expectedId, category.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        String expectedName = "Electronics";

        // Act
        category.setName(expectedName);

        // Assert
        assertEquals(expectedName, category.getName());
    }

    @Test
    void testSetAndGetName_withNullValue() {
        // Act
        category.setName(null);

        // Assert
        assertNull(category.getName());
    }

    @Test
    void testSetAndGetName_withEmptyString() {
        // Arrange
        String emptyName = "";

        // Act
        category.setName(emptyName);

        // Assert
        assertEquals(emptyName, category.getName());
    }

    @Test
    void testSetAndGetId_withNullValue() {
        // Act
        category.setId(null);

        // Assert
        assertNull(category.getId());
    }

    @Test
    void testCategoryEquality() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Test");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Test");

        // Assert - Lombok @Data generates equals method
        assertEquals(category1, category2);
    }

    @Test
    void testCategoryHashCode() {
        // Arrange
        category.setId(1L);
        category.setName("Test");

        // Act
        int hashCode = category.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void testCategoryToString() {
        // Arrange
        category.setId(1L);
        category.setName("Test Category");

        // Act
        String toString = category.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Category"));
    }

    @Test
    void testEntityAnnotationPresent() {
        // Assert
        assertTrue(Category.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void testTableAnnotationPresent() {
        // Assert
        assertTrue(Category.class.isAnnotationPresent(jakarta.persistence.Table.class));
    }
}

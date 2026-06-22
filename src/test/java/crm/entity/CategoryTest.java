package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
    }

    @Test
    void category_shouldBeCreated() {
        // Assert
        assertNotNull(category);
    }

    @Test
    void setId_shouldSetIdCorrectly() {
        // Arrange
        Long expectedId = 1L;

        // Act
        category.setId(expectedId);

        // Assert
        assertEquals(expectedId, category.getId());
    }

    @Test
    void setName_shouldSetNameCorrectly() {
        // Arrange
        String expectedName = "VIP";

        // Act
        category.setName(expectedName);

        // Assert
        assertEquals(expectedName, category.getName());
    }

    @Test
    void category_withAllFields_shouldStoreCorrectly() {
        // Arrange
        Long id = 10L;
        String name = "Premium";

        // Act
        category.setId(id);
        category.setName(name);

        // Assert
        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
    }

    @Test
    void category_shouldSupportEqualsAndHashCode() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Standard");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Standard");

        // Assert
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void category_shouldSupportToString() {
        // Arrange
        category.setId(5L);
        category.setName("Enterprise");

        // Act
        String result = category.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("Enterprise"));
    }

    @Test
    void category_withNullName_shouldAllowNull() {
        // Act
        category.setName(null);

        // Assert
        assertNull(category.getName());
    }
}

package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Premium");
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        Category newCategory = new Category();

        // Assert
        assertNotNull(newCategory);
    }

    @Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Category newCategory = new Category();

        // Act
        newCategory.setId(5L);
        newCategory.setName("Standard");

        // Assert
        assertEquals(5L, newCategory.getId());
        assertEquals("Standard", newCategory.getName());
    }

    @Test
    void testSetId_ShouldUpdateId() {
        // Arrange & Act
        category.setId(99L);

        // Assert
        assertEquals(99L, category.getId());
    }

    @Test
    void testSetName_ShouldUpdateName() {
        // Arrange & Act
        category.setName("VIP");

        // Assert
        assertEquals("VIP", category.getName());
    }

    @Test
    void testGetId_ShouldReturnCorrectId() {
        // Arrange & Act
        Long id = category.getId();

        // Assert
        assertEquals(1L, id);
    }

    @Test
    void testGetName_ShouldReturnCorrectName() {
        // Arrange & Act
        String name = category.getName();

        // Assert
        assertEquals("Premium", name);
    }

    @Test
    void testSetId_WithNullValue_ShouldAcceptNull() {
        // Arrange & Act
        category.setId(null);

        // Assert
        assertNull(category.getId());
    }

    @Test
    void testSetName_WithNullValue_ShouldAcceptNull() {
        // Arrange & Act
        category.setName(null);

        // Assert
        assertNull(category.getName());
    }

    @Test
    void testSetName_WithEmptyString_ShouldAcceptEmptyString() {
        // Arrange & Act
        category.setName("");

        // Assert
        assertEquals("", category.getName());
    }

    @Test
    void testSetName_WithLongString_ShouldAcceptLongString() {
        // Arrange
        String longName = "A".repeat(255);

        // Act
        category.setName(longName);

        // Assert
        assertEquals(longName, category.getName());
    }

    @Test
    void testEquals_WithSameId_ShouldBeEqual() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Cat1");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Cat2");

        // Act & Assert
        assertEquals(category1, category2);
    }

    @Test
    void testHashCode_WithSameId_ShouldHaveSameHashCode() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Cat1");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Cat2");

        // Act & Assert
        assertEquals(category1.hashCode(), category2.hashCode());
    }
}

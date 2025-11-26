package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
    }

    @Test
    void testConstructor() {
        Category newCategory = new Category();
        assertNotNull(newCategory);
    }

    @Test
    void testGetters() {
        assertEquals(1L, category.getId());
        assertEquals("Test Category", category.getName());
    }

    @Test
    void testSetters() {
        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("New Category");

        assertEquals(2L, newCategory.getId());
        assertEquals("New Category", newCategory.getName());
    }

    @Test
    void testSetId() {
        category.setId(3L);
        assertEquals(3L, category.getId());
    }

    @Test
    void testSetName() {
        category.setName("Updated Category");
        assertEquals("Updated Category", category.getName());
    }

    @Test
    void testSetName_WithNull() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    void testSetId_WithZero() {
        category.setId(0L);
        assertEquals(0L, category.getId());
    }
}

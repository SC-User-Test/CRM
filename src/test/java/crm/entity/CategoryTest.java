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
    void testCategorySettersAndGetters() {
        category.setId(1L);
        category.setName("Technology");

        assertEquals(1L, category.getId());
        assertEquals("Technology", category.getName());
    }

    @Test
    void testCategoryNoArgsConstructor() {
        Category newCategory = new Category();
        assertNotNull(newCategory);
        assertNull(newCategory.getId());
        assertNull(newCategory.getName());
    }

    @Test
    void testCategoryWithNullValues() {
        category.setId(null);
        category.setName(null);

        assertNull(category.getId());
        assertNull(category.getName());
    }

    @Test
    void testCategoryWithEmptyName() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testCategoryWithLongName() {
        String longName = "ThisIsAVeryLongCategoryNameForTesting";
        category.setName(longName);
        assertEquals(longName, category.getName());
    }

    @Test
    void testCategoryIdAutoGeneration() {
        category.setId(null);
        assertNull(category.getId());

        category.setId(100L);
        assertEquals(100L, category.getId());
    }

    @Test
    void testCategoryNameUpdate() {
        category.setName("Finance");
        assertEquals("Finance", category.getName());

        category.setName("Banking");
        assertEquals("Banking", category.getName());
    }

    @Test
    void testCategoryWithSpecialCharactersInName() {
        category.setName("Tech & Innovation");
        assertEquals("Tech & Innovation", category.getName());
    }
}

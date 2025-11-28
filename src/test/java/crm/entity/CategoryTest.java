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
    void testCategoryConstructor() {
        assertNotNull(category);
    }

    @Test
    void testSetAndGetId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    void testSetAndGetName() {
        category.setName("Electronics");
        assertEquals("Electronics", category.getName());
    }

    @Test
    void testSetAndGetNameWithNull() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    void testSetAndGetNameWithEmptyString() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testCategoryWithLongName() {
        String longName = "A".repeat(255);
        category.setName(longName);
        assertEquals(longName, category.getName());
    }

    @Test
    void testCategoryEqualsAndHashCode() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Test");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("Test");

        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());
    }

    @Test
    void testCategoryToString() {
        category.setId(1L);
        category.setName("Books");

        String toString = category.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Books"));
    }

    @Test
    void testCategoryWithDifferentIds() {
        Category category1 = new Category();
        category1.setId(1L);

        Category category2 = new Category();
        category2.setId(2L);

        assertNotEquals(category1.getId(), category2.getId());
    }
}

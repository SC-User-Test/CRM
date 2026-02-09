package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
    }

    @Test
    public void testCategoryCreation() {
        assertNotNull(category);
    }

    @Test
    public void testSetAndGetId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    public void testSetAndGetName() {
        category.setName("VIP");
        assertEquals("VIP", category.getName());
    }

    @Test
    public void testCategoryWithDifferentNames() {
        category.setId(2L);
        category.setName("Premium");
        assertEquals(2L, category.getId());
        assertEquals("Premium", category.getName());
    }

    @Test
    public void testCategoryEquality() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("VIP");

        Category category2 = new Category();
        category2.setId(1L);
        category2.setName("VIP");

        assertEquals(category1, category2);
    }

    @Test
    public void testCategoryToString() {
        category.setId(1L);
        category.setName("VIP");
        String result = category.toString();
        assertNotNull(result);
        assertTrue(result.contains("VIP"));
    }

    @Test
    public void testCategoryWithNullName() {
        category.setId(3L);
        category.setName(null);
        assertNull(category.getName());
        assertEquals(3L, category.getId());
    }
}

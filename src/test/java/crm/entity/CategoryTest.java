package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryTest {

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
    }

    @Test
    public void testConstructor() {
        Category newCategory = new Category();
        assertNotNull(newCategory);
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
    public void testNullId() {
        category.setId(null);
        assertNull(category.getId());
    }

    @Test
    public void testNullName() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    public void testEmptyName() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    public void testLongName() {
        String longName = "This is a very long category name that might exceed normal limits";
        category.setName(longName);
        assertEquals(longName, category.getName());
    }

    @Test
    public void testCategoryNameWithSpaces() {
        category.setName("Premium Customer");
        assertEquals("Premium Customer", category.getName());
    }

    @Test
    public void testCategoryNameWithNumbers() {
        category.setName("Category123");
        assertEquals("Category123", category.getName());
    }

    @Test
    public void testMultipleCategoryInstances() {
        Category cat1 = new Category();
        Category cat2 = new Category();
        cat1.setId(1L);
        cat2.setId(2L);

        assertNotEquals(cat1.getId(), cat2.getId());
    }

    @Test
    public void testZeroId() {
        category.setId(0L);
        assertEquals(0L, category.getId());
    }
}

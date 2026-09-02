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
        assertNotNull(category);
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        category.setId(1L);
        assertEquals(1L, category.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        category.setName("VIP");
        assertEquals("VIP", category.getName());
    }

    @Test
    void testSetId_withNull_returnsNull() {
        category.setId(null);
        assertNull(category.getId());
    }

    @Test
    void testSetName_withNull_returnsNull() {
        category.setName(null);
        assertNull(category.getName());
    }

    @Test
    void testSetName_withEmptyString_returnsEmpty() {
        category.setName("");
        assertEquals("", category.getName());
    }

    @Test
    void testEquals_sameObject_returnsTrue() {
        category.setId(1L);
        category.setName("VIP");
        assertEquals(category, category);
    }

    @Test
    void testEquals_equalObjects_returnsTrue() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("VIP");

        Category cat2 = new Category();
        cat2.setId(1L);
        cat2.setName("VIP");

        assertEquals(cat1, cat2);
    }

    @Test
    void testEquals_differentObjects_returnsFalse() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("VIP");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Regular");

        assertNotEquals(cat1, cat2);
    }

    @Test
    void testHashCode_equalObjects_sameHashCode() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("VIP");

        Category cat2 = new Category();
        cat2.setId(1L);
        cat2.setName("VIP");

        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testToString_containsFieldValues() {
        category.setId(1L);
        category.setName("VIP");
        String toString = category.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("VIP"));
    }

    @Test
    void testSetId_withLargeValue_returnsCorrectId() {
        category.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, category.getId());
    }
}

package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        categories = new HashSet<>();
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("VIP");
        categories.add(cat);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Acme Corp");
        customer.setEmail("acme@example.com");
        customer.setPhone(123456789);
        customer.setCategories(categories);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("New York");
        customer.setAddress("123 Main St");
        customer.setEnabled(1);
    }

    @Test
    void testDefaultConstructor() {
        Customer c = new Customer();
        assertNotNull(c);
    }

    @Test
    void testAllArgsConstructor() {
        Customer c = new Customer(1L, "Test Corp", "test@example.com", 987654321,
                categories, "Jane", "Smith", "Boston", "456 Elm St", 1);
        assertNotNull(c);
        assertEquals("Test Corp", c.getName());
        assertEquals("test@example.com", c.getEmail());
    }

    @Test
    void testBuilderPattern() {
        Customer c = Customer.builder()
                .id(2L)
                .name("Builder Corp")
                .email("builder@example.com")
                .phone(111222333)
                .categories(categories)
                .firstName("Builder")
                .lastName("Test")
                .city("Chicago")
                .address("789 Oak Ave")
                .enabled(1)
                .build();
        assertNotNull(c);
        assertEquals("Builder Corp", c.getName());
    }

    @Test
    void testGetId() {
        assertEquals(1L, customer.getId());
    }

    @Test
    void testSetAndGetName() {
        customer.setName("New Corp");
        assertEquals("New Corp", customer.getName());
    }

    @Test
    void testSetAndGetEmail() {
        customer.setEmail("new@example.com");
        assertEquals("new@example.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        customer.setPhone(999888777);
        assertEquals(999888777, customer.getPhone());
    }

    @Test
    void testSetAndGetCategories() {
        Set<Category> newCats = new HashSet<>();
        Category c = new Category();
        c.setId(2L);
        c.setName("Regular");
        newCats.add(c);
        customer.setCategories(newCats);
        assertEquals(newCats, customer.getCategories());
    }

    @Test
    void testSetAndGetFirstName() {
        customer.setFirstName("Jane");
        assertEquals("Jane", customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        customer.setLastName("Smith");
        assertEquals("Smith", customer.getLastName());
    }

    @Test
    void testSetAndGetCity() {
        customer.setCity("Los Angeles");
        assertEquals("Los Angeles", customer.getCity());
    }

    @Test
    void testSetAndGetAddress() {
        customer.setAddress("456 Oak Ave");
        assertEquals("456 Oak Ave", customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testEnabledTrue() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        Customer c1 = Customer.builder().id(1L).name("Corp").email("corp@example.com")
                .phone(123).categories(categories).firstName("A").lastName("B")
                .city("NYC").address("123 St").enabled(1).build();
        Customer c2 = Customer.builder().id(1L).name("Corp").email("corp@example.com")
                .phone(123).categories(categories).firstName("A").lastName("B")
                .city("NYC").address("123 St").enabled(1).build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testNotEquals() {
        Customer c1 = Customer.builder().id(1L).name("Corp1").email("corp1@example.com").build();
        Customer c2 = Customer.builder().id(2L).name("Corp2").email("corp2@example.com").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testToString() {
        String str = customer.toString();
        assertNotNull(str);
        assertTrue(str.contains("Acme Corp"));
    }

    @Test
    void testSetNameNull() {
        customer.setName(null);
        assertNull(customer.getName());
    }

    @Test
    void testSetEmailNull() {
        customer.setEmail(null);
        assertNull(customer.getEmail());
    }

    @Test
    void testSetCategoriesNull() {
        customer.setCategories(null);
        assertNull(customer.getCategories());
    }

    @Test
    void testSetCategoriesEmpty() {
        customer.setCategories(new HashSet<>());
        assertTrue(customer.getCategories().isEmpty());
    }
}

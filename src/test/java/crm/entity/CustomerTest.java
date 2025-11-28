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
        customer = new Customer();
        categories = new HashSet<>();
    }

    @Test
    void testCustomerConstructor() {
        assertNotNull(customer);
    }

    @Test
    void testCustomerBuilder() {
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        categories.add(category);

        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone(123456789)
                .categories(categories)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        assertNotNull(builtCustomer);
        assertEquals(1L, builtCustomer.getId());
        assertEquals("John Doe", builtCustomer.getName());
        assertEquals("john@example.com", builtCustomer.getEmail());
        assertEquals(123456789, builtCustomer.getPhone());
        assertEquals(categories, builtCustomer.getCategories());
        assertEquals("John", builtCustomer.getFirstName());
        assertEquals("Doe", builtCustomer.getLastName());
        assertEquals("New York", builtCustomer.getCity());
        assertEquals("123 Main St", builtCustomer.getAddress());
        assertEquals(1, builtCustomer.getEnabled());
    }

    @Test
    void testSetAndGetId() {
        customer.setId(1L);
        assertEquals(1L, customer.getId());
    }

    @Test
    void testSetAndGetName() {
        customer.setName("Test Customer");
        assertEquals("Test Customer", customer.getName());
    }

    @Test
    void testSetAndGetEmail() {
        customer.setEmail("customer@example.com");
        assertEquals("customer@example.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        customer.setPhone(987654321);
        assertEquals(987654321, customer.getPhone());
    }

    @Test
    void testSetAndGetCategories() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Premium");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Regular");

        categories.add(category1);
        categories.add(category2);

        customer.setCategories(categories);

        assertEquals(2, customer.getCategories().size());
        assertTrue(customer.getCategories().contains(category1));
        assertTrue(customer.getCategories().contains(category2));
    }

    @Test
    void testSetAndGetFirstName() {
        customer.setFirstName("Alice");
        assertEquals("Alice", customer.getFirstName());
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
        customer.setAddress("456 Oak Avenue");
        assertEquals("456 Oak Avenue", customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());

        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testCustomerWithNullValues() {
        customer.setName(null);
        customer.setEmail(null);
        customer.setCategories(null);
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setCity(null);
        customer.setAddress(null);

        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getCategories());
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getCity());
        assertNull(customer.getAddress());
    }

    @Test
    void testCustomerWithEmptyCategories() {
        customer.setCategories(new HashSet<>());
        assertNotNull(customer.getCategories());
        assertEquals(0, customer.getCategories().size());
    }

    @Test
    void testCustomerEqualsAndHashCode() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .build();

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }

    @Test
    void testCustomerToString() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .build();

        String toString = customer.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test Customer"));
    }
}

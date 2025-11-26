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
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        categories.add(category);

        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .categories(categories)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
    }

    @Test
    void testConstructor() {
        Customer newCustomer = new Customer();
        assertNotNull(newCustomer);
    }

    @Test
    void testBuilder() {
        assertNotNull(customer);
        assertEquals("Test Customer", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    void testGetters() {
        assertEquals(1L, customer.getId());
        assertEquals("Test Customer", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals(123456789, customer.getPhone());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("New York", customer.getCity());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals(1, customer.getEnabled());
        assertNotNull(customer.getCategories());
    }

    @Test
    void testSetters() {
        Customer newCustomer = new Customer();
        newCustomer.setId(2L);
        newCustomer.setName("New Customer");
        newCustomer.setEmail("new@example.com");
        newCustomer.setPhone(987654321);
        newCustomer.setFirstName("Jane");
        newCustomer.setLastName("Smith");
        newCustomer.setCity("Los Angeles");
        newCustomer.setAddress("456 Oak Ave");
        newCustomer.setEnabled(0);
        newCustomer.setCategories(categories);

        assertEquals(2L, newCustomer.getId());
        assertEquals("New Customer", newCustomer.getName());
        assertEquals("new@example.com", newCustomer.getEmail());
        assertEquals(987654321, newCustomer.getPhone());
        assertEquals("Jane", newCustomer.getFirstName());
        assertEquals("Smith", newCustomer.getLastName());
        assertEquals("Los Angeles", newCustomer.getCity());
        assertEquals("456 Oak Ave", newCustomer.getAddress());
        assertEquals(0, newCustomer.getEnabled());
        assertNotNull(newCustomer.getCategories());
    }

    @Test
    void testCategories_NotNull() {
        assertNotNull(customer.getCategories());
        assertFalse(customer.getCategories().isEmpty());
    }

    @Test
    void testCategories_Size() {
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testEnabled_DefaultValue() {
        Customer newCustomer = new Customer();
        assertEquals(0, newCustomer.getEnabled());
    }
}

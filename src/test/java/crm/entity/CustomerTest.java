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
    void testCustomerBuilder() {
        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(1234567890)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(categories)
                .build();

        assertNotNull(builtCustomer);
        assertEquals(1L, builtCustomer.getId());
        assertEquals("Test Customer", builtCustomer.getName());
        assertEquals("test@example.com", builtCustomer.getEmail());
        assertEquals(1234567890, builtCustomer.getPhone());
    }

    @Test
    void testCustomerSettersAndGetters() {
        customer.setId(2L);
        customer.setName("Jane Smith");
        customer.setEmail("jane@example.com");
        customer.setPhone(987654321);
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setCity("Boston");
        customer.setAddress("456 Oak Ave");
        customer.setEnabled(1);
        customer.setCategories(categories);

        assertEquals(2L, customer.getId());
        assertEquals("Jane Smith", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
        assertEquals(987654321, customer.getPhone());
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("Boston", customer.getCity());
        assertEquals("456 Oak Ave", customer.getAddress());
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testCustomerNoArgsConstructor() {
        Customer newCustomer = new Customer();
        assertNotNull(newCustomer);
        assertNull(newCustomer.getId());
        assertNull(newCustomer.getName());
        assertEquals(0, newCustomer.getPhone());
    }

    @Test
    void testCustomerAllArgsConstructor() {
        Customer allArgsCustomer = new Customer(
                3L, "Company Inc", "company@example.com", 111222333,
                categories, "Alice", "Brown", "Chicago", "789 Elm St", 1
        );

        assertEquals(3L, allArgsCustomer.getId());
        assertEquals("Company Inc", allArgsCustomer.getName());
        assertEquals("company@example.com", allArgsCustomer.getEmail());
    }

    @Test
    void testCustomerWithCategories() {
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Premium");

        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Enterprise");

        categories.add(cat1);
        categories.add(cat2);

        customer.setCategories(categories);

        assertEquals(2, customer.getCategories().size());
        assertTrue(customer.getCategories().contains(cat1));
        assertTrue(customer.getCategories().contains(cat2));
    }

    @Test
    void testCustomerWithEmptyCategories() {
        customer.setCategories(new HashSet<>());
        assertNotNull(customer.getCategories());
        assertTrue(customer.getCategories().isEmpty());
    }

    @Test
    void testCustomerEnabledFlag() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());

        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testCustomerPhoneBoundary() {
        customer.setPhone(0);
        assertEquals(0, customer.getPhone());

        customer.setPhone(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, customer.getPhone());
    }
}

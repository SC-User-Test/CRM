package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    public void setUp() {
        categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        categories.add(category);

        customer = Customer.builder()
                .id(1L)
                .name("Test Company")
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
    public void testCustomerCreation() {
        assertNotNull(customer);
    }

    @Test
    public void testCustomerBuilder() {
        Customer newCustomer = Customer.builder()
                .id(2L)
                .name("New Company")
                .email("new@example.com")
                .build();
        assertNotNull(newCustomer);
        assertEquals(2L, newCustomer.getId());
        assertEquals("New Company", newCustomer.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(1L, customer.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Company", customer.getName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("test@example.com", customer.getEmail());
    }

    @Test
    public void testGetPhone() {
        assertEquals(123456789, customer.getPhone());
    }

    @Test
    public void testGetCategories() {
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", customer.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", customer.getLastName());
    }

    @Test
    public void testGetCity() {
        assertEquals("New York", customer.getCity());
    }

    @Test
    public void testGetAddress() {
        assertEquals("123 Main St", customer.getAddress());
    }

    @Test
    public void testGetEnabled() {
        assertEquals(1, customer.getEnabled());
    }

    @Test
    public void testSetName() {
        customer.setName("Updated Company");
        assertEquals("Updated Company", customer.getName());
    }

    @Test
    public void testSetEmail() {
        customer.setEmail("updated@example.com");
        assertEquals("updated@example.com", customer.getEmail());
    }

    @Test
    public void testSetEnabled() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    public void testCustomerWithNullCategories() {
        Customer customerNoCategories = Customer.builder()
                .id(3L)
                .name("No Categories")
                .email("nocategories@example.com")
                .phone(111111111)
                .enabled(1)
                .build();
        assertNull(customerNoCategories.getCategories());
    }

    @Test
    public void testCustomerEquality() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();
        Customer customer2 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@test.com")
                .build();
        assertEquals(customer1, customer2);
    }
}

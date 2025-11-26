package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerTest {

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        categories.add(category);
    }

    @Test
    public void testConstructor() {
        Customer newCustomer = new Customer();
        assertNotNull(newCustomer);
    }

    @Test
    public void testBuilderPattern() {
        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("Test Company")
                .email("test@company.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(categories)
                .build();

        assertNotNull(builtCustomer);
        assertEquals(1L, builtCustomer.getId());
        assertEquals("Test Company", builtCustomer.getName());
    }

    @Test
    public void testSetAndGetId() {
        customer.setId(1L);
        assertEquals(1L, customer.getId());
    }

    @Test
    public void testSetAndGetName() {
        customer.setName("ABC Corp");
        assertEquals("ABC Corp", customer.getName());
    }

    @Test
    public void testSetAndGetEmail() {
        customer.setEmail("info@abc.com");
        assertEquals("info@abc.com", customer.getEmail());
    }

    @Test
    public void testSetAndGetPhone() {
        customer.setPhone(987654321);
        assertEquals(987654321, customer.getPhone());
    }

    @Test
    public void testSetAndGetCategories() {
        customer.setCategories(categories);
        assertEquals(categories, customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    public void testSetAndGetFirstName() {
        customer.setFirstName("Jane");
        assertEquals("Jane", customer.getFirstName());
    }

    @Test
    public void testSetAndGetLastName() {
        customer.setLastName("Smith");
        assertEquals("Smith", customer.getLastName());
    }

    @Test
    public void testSetAndGetCity() {
        customer.setCity("Boston");
        assertEquals("Boston", customer.getCity());
    }

    @Test
    public void testSetAndGetAddress() {
        customer.setAddress("456 Oak Ave");
        assertEquals("456 Oak Ave", customer.getAddress());
    }

    @Test
    public void testSetAndGetEnabled() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    public void testEnabledDefaultValue() {
        Customer newCustomer = new Customer();
        assertEquals(0, newCustomer.getEnabled());
    }

    @Test
    public void testPhoneDefaultValue() {
        Customer newCustomer = new Customer();
        assertEquals(0, newCustomer.getPhone());
    }

    @Test
    public void testEmptyCategories() {
        Set<Category> emptySet = new HashSet<>();
        customer.setCategories(emptySet);
        assertNotNull(customer.getCategories());
        assertEquals(0, customer.getCategories().size());
    }

    @Test
    public void testNullCategories() {
        customer.setCategories(null);
        assertNull(customer.getCategories());
    }

    @Test
    public void testAllArgsConstructor() {
        Customer newCustomer = new Customer(2L, "XYZ Ltd", "xyz@test.com", 111222333, categories, "Bob", "Jones", "Chicago", "789 Elm St", 1);
        assertNotNull(newCustomer);
        assertEquals(2L, newCustomer.getId());
        assertEquals("XYZ Ltd", newCustomer.getName());
        assertEquals("xyz@test.com", newCustomer.getEmail());
    }
}

package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
    }

    @Test
    void testCustomerCreation() {
        // Assert
        assertNotNull(customer);
    }

    @Test
    void testSetAndGetId() {
        // Arrange
        Long expectedId = 1L;

        // Act
        customer.setId(expectedId);

        // Assert
        assertEquals(expectedId, customer.getId());
    }

    @Test
    void testSetAndGetName() {
        // Arrange
        String expectedName = "John Doe";

        // Act
        customer.setName(expectedName);

        // Assert
        assertEquals(expectedName, customer.getName());
    }

    @Test
    void testSetAndGetEmail() {
        // Arrange
        String expectedEmail = "john.doe@example.com";

        // Act
        customer.setEmail(expectedEmail);

        // Assert
        assertEquals(expectedEmail, customer.getEmail());
    }

    @Test
    void testSetAndGetPhone() {
        // Arrange
        int expectedPhone = 123456789;

        // Act
        customer.setPhone(expectedPhone);

        // Assert
        assertEquals(expectedPhone, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName() {
        // Arrange
        String expectedFirstName = "John";

        // Act
        customer.setFirstName(expectedFirstName);

        // Assert
        assertEquals(expectedFirstName, customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName() {
        // Arrange
        String expectedLastName = "Doe";

        // Act
        customer.setLastName(expectedLastName);

        // Assert
        assertEquals(expectedLastName, customer.getLastName());
    }

    @Test
    void testSetAndGetCity() {
        // Arrange
        String expectedCity = "New York";

        // Act
        customer.setCity(expectedCity);

        // Assert
        assertEquals(expectedCity, customer.getCity());
    }

    @Test
    void testSetAndGetAddress() {
        // Arrange
        String expectedAddress = "123 Main St";

        // Act
        customer.setAddress(expectedAddress);

        // Assert
        assertEquals(expectedAddress, customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled() {
        // Arrange
        int expectedEnabled = 1;

        // Act
        customer.setEnabled(expectedEnabled);

        // Assert
        assertEquals(expectedEnabled, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories() {
        // Arrange
        Set<Category> expectedCategories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        expectedCategories.add(category);

        // Act
        customer.setCategories(expectedCategories);

        // Assert
        assertEquals(expectedCategories, customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testBuilderPattern() {
        // Arrange & Act
        Customer builtCustomer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("Test")
                .lastName("Customer")
                .city("Test City")
                .address("Test Address")
                .enabled(1)
                .build();

        // Assert
        assertNotNull(builtCustomer);
        assertEquals(1L, builtCustomer.getId());
        assertEquals("Test Customer", builtCustomer.getName());
        assertEquals("test@example.com", builtCustomer.getEmail());
        assertEquals(123456789, builtCustomer.getPhone());
        assertEquals("Test", builtCustomer.getFirstName());
        assertEquals("Customer", builtCustomer.getLastName());
        assertEquals("Test City", builtCustomer.getCity());
        assertEquals("Test Address", builtCustomer.getAddress());
        assertEquals(1, builtCustomer.getEnabled());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Set<Category> categories = new HashSet<>();

        // Act
        Customer customer = new Customer(
                1L,
                "Customer Name",
                "customer@example.com",
                123456789,
                categories,
                "First",
                "Last",
                "City",
                "Address",
                1
        );

        // Assert
        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("Customer Name", customer.getName());
        assertEquals("customer@example.com", customer.getEmail());
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Customer customer = new Customer();

        // Assert
        assertNotNull(customer);
        assertNull(customer.getId());
        assertNull(customer.getName());
    }

    @Test
    void testCustomerEquality() {
        // Arrange
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        // Assert
        assertEquals(customer1, customer2);
    }

    @Test
    void testCustomerHashCode() {
        // Arrange
        customer.setId(1L);
        customer.setName("Test");

        // Act
        int hashCode = customer.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void testCustomerToString() {
        // Arrange
        customer.setId(1L);
        customer.setName("Test Customer");

        // Act
        String toString = customer.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("Customer"));
    }

    @Test
    void testEntityAnnotationPresent() {
        // Assert
        assertTrue(Customer.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }

    @Test
    void testSetNullValues() {
        // Act
        customer.setId(null);
        customer.setName(null);
        customer.setEmail(null);
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setCity(null);
        customer.setAddress(null);
        customer.setCategories(null);

        // Assert
        assertNull(customer.getId());
        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getCity());
        assertNull(customer.getAddress());
        assertNull(customer.getCategories());
    }

    @Test
    void testAddMultipleCategories() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("VIP");
        
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Premium");
        
        categories.add(category1);
        categories.add(category2);

        // Act
        customer.setCategories(categories);

        // Assert
        assertEquals(2, customer.getCategories().size());
        assertTrue(customer.getCategories().contains(category1));
        assertTrue(customer.getCategories().contains(category2));
    }
}

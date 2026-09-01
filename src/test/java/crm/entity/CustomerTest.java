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
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        Customer c = new Customer();
        // Assert
        assertNotNull(c);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        // Arrange
        Long id = 1L;
        String name = "Acme Corp";
        String email = "acme@example.com";
        int phone = 123456789;
        Set<Category> categories = new HashSet<>();
        String firstName = "John";
        String lastName = "Doe";
        String city = "New York";
        String address = "123 Main St";
        int enabled = 1;
        // Act
        Customer c = new Customer(id, name, email, phone, categories, firstName, lastName, city, address, enabled);
        // Assert
        assertNotNull(c);
        assertEquals(id, c.getId());
        assertEquals(name, c.getName());
        assertEquals(email, c.getEmail());
        assertEquals(phone, c.getPhone());
        assertEquals(firstName, c.getFirstName());
        assertEquals(lastName, c.getLastName());
        assertEquals(city, c.getCity());
        assertEquals(address, c.getAddress());
        assertEquals(enabled, c.getEnabled());
    }

    @Test
    void testBuilder_createsCustomerWithAllFields() {
        // Arrange & Act
        Customer c = Customer.builder()
                .id(1L)
                .name("Test Corp")
                .email("test@corp.com")
                .phone(987654321)
                .firstName("Jane")
                .lastName("Smith")
                .city("Chicago")
                .address("456 Oak Ave")
                .enabled(1)
                .build();
        // Assert
        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("Test Corp", c.getName());
        assertEquals("test@corp.com", c.getEmail());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        // Arrange
        Long expectedId = 5L;
        // Act
        customer.setId(expectedId);
        // Assert
        assertEquals(expectedId, customer.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        // Arrange
        String expectedName = "Customer Name";
        // Act
        customer.setName(expectedName);
        // Assert
        assertEquals(expectedName, customer.getName());
    }

    @Test
    void testSetAndGetEmail_returnsCorrectEmail() {
        // Arrange
        String expectedEmail = "customer@example.com";
        // Act
        customer.setEmail(expectedEmail);
        // Assert
        assertEquals(expectedEmail, customer.getEmail());
    }

    @Test
    void testSetAndGetPhone_returnsCorrectPhone() {
        // Arrange
        int expectedPhone = 555123456;
        // Act
        customer.setPhone(expectedPhone);
        // Assert
        assertEquals(expectedPhone, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName_returnsCorrectFirstName() {
        // Arrange
        String expectedFirstName = "Alice";
        // Act
        customer.setFirstName(expectedFirstName);
        // Assert
        assertEquals(expectedFirstName, customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName_returnsCorrectLastName() {
        // Arrange
        String expectedLastName = "Johnson";
        // Act
        customer.setLastName(expectedLastName);
        // Assert
        assertEquals(expectedLastName, customer.getLastName());
    }

    @Test
    void testSetAndGetCity_returnsCorrectCity() {
        // Arrange
        String expectedCity = "Los Angeles";
        // Act
        customer.setCity(expectedCity);
        // Assert
        assertEquals(expectedCity, customer.getCity());
    }

    @Test
    void testSetAndGetAddress_returnsCorrectAddress() {
        // Arrange
        String expectedAddress = "789 Pine St";
        // Act
        customer.setAddress(expectedAddress);
        // Assert
        assertEquals(expectedAddress, customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled_returnsCorrectEnabled() {
        // Arrange
        int expectedEnabled = 1;
        // Act
        customer.setEnabled(expectedEnabled);
        // Assert
        assertEquals(expectedEnabled, customer.getEnabled());
    }

    @Test
    void testSetEnabled_withZero_returnsZero() {
        // Arrange & Act
        customer.setEnabled(0);
        // Assert
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories_returnsCorrectCategories() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Tech");
        categories.add(cat);
        // Act
        customer.setCategories(categories);
        // Assert
        assertEquals(categories, customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testEquals_equalCustomers_returnsTrue() {
        // Arrange
        Customer c1 = Customer.builder().id(1L).name("Corp").email("a@b.com").build();
        Customer c2 = Customer.builder().id(1L).name("Corp").email("a@b.com").build();
        // Act & Assert
        assertEquals(c1, c2);
    }

    @Test
    void testEquals_differentCustomers_returnsFalse() {
        // Arrange
        Customer c1 = Customer.builder().id(1L).name("Corp1").build();
        Customer c2 = Customer.builder().id(2L).name("Corp2").build();
        // Act & Assert
        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCode_equalCustomers_sameHashCode() {
        // Arrange
        Customer c1 = Customer.builder().id(1L).name("Corp").build();
        Customer c2 = Customer.builder().id(1L).name("Corp").build();
        // Act & Assert
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString_notNull() {
        // Arrange
        customer.setId(1L);
        customer.setName("Test");
        // Act
        String result = customer.toString();
        // Assert
        assertNotNull(result);
    }
}

package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
    void customer_shouldBeCreated() {
        // Assert
        assertNotNull(customer);
    }

    @Test
    void builder_shouldCreateCustomerWithAllFields() {
        // Arrange & Act
        Customer customer = Customer.builder()
                .id(1L)
                .name("TestCustomer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        // Assert
        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals("TestCustomer", customer.getName());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals(123456789, customer.getPhone());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("New York", customer.getCity());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void setId_shouldSetIdCorrectly() {
        // Arrange
        Long expectedId = 100L;

        // Act
        customer.setId(expectedId);

        // Assert
        assertEquals(expectedId, customer.getId());
    }

    @Test
    void setName_shouldSetNameCorrectly() {
        // Arrange
        String expectedName = "Customer Name";

        // Act
        customer.setName(expectedName);

        // Assert
        assertEquals(expectedName, customer.getName());
    }

    @Test
    void setEmail_shouldSetEmailCorrectly() {
        // Arrange
        String expectedEmail = "customer@test.com";

        // Act
        customer.setEmail(expectedEmail);

        // Assert
        assertEquals(expectedEmail, customer.getEmail());
    }

    @Test
    void setPhone_shouldSetPhoneCorrectly() {
        // Arrange
        int expectedPhone = 987654321;

        // Act
        customer.setPhone(expectedPhone);

        // Assert
        assertEquals(expectedPhone, customer.getPhone());
    }

    @Test
    void setCategories_shouldSetCategoriesCorrectly() {
        // Arrange
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        categories.add(category);

        // Act
        customer.setCategories(categories);

        // Assert
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void setFirstName_shouldSetFirstNameCorrectly() {
        // Arrange
        String expectedFirstName = "Jane";

        // Act
        customer.setFirstName(expectedFirstName);

        // Assert
        assertEquals(expectedFirstName, customer.getFirstName());
    }

    @Test
    void setLastName_shouldSetLastNameCorrectly() {
        // Arrange
        String expectedLastName = "Smith";

        // Act
        customer.setLastName(expectedLastName);

        // Assert
        assertEquals(expectedLastName, customer.getLastName());
    }

    @Test
    void setCity_shouldSetCityCorrectly() {
        // Arrange
        String expectedCity = "Los Angeles";

        // Act
        customer.setCity(expectedCity);

        // Assert
        assertEquals(expectedCity, customer.getCity());
    }

    @Test
    void setAddress_shouldSetAddressCorrectly() {
        // Arrange
        String expectedAddress = "456 Oak Ave";

        // Act
        customer.setAddress(expectedAddress);

        // Assert
        assertEquals(expectedAddress, customer.getAddress());
    }

    @Test
    void setEnabled_shouldSetEnabledCorrectly() {
        // Arrange
        int expectedEnabled = 1;

        // Act
        customer.setEnabled(expectedEnabled);

        // Assert
        assertEquals(expectedEnabled, customer.getEnabled());
    }

    @Test
    void customer_withNoArgsConstructor_shouldCreateEmptyCustomer() {
        // Act
        Customer emptyCustomer = new Customer();

        // Assert
        assertNotNull(emptyCustomer);
    }

    @Test
    void customer_withAllArgsConstructor_shouldCreateFullCustomer() {
        // Arrange
        Set<Category> categories = new HashSet<>();

        // Act
        Customer fullCustomer = new Customer(1L, "Name", "email@test.com", 123456, categories, 
                                            "First", "Last", "City", "Address", 1);

        // Assert
        assertNotNull(fullCustomer);
        assertEquals(1L, fullCustomer.getId());
        assertEquals("Name", fullCustomer.getName());
    }

    @Test
    void customer_shouldSupportEqualsAndHashCode() {
        // Arrange
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

        // Assert
        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }
}

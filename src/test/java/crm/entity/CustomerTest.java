package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerTest {

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Premium");

        categories = new HashSet<>();
        categories.add(category);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Company");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);
        customer.setCategories(categories);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("Test City");
        customer.setAddress("123 Test Street");
        customer.setEnabled(1);
    }

    @Test
    void testNoArgsConstructor_ShouldCreateInstance() {
        // Arrange & Act
        Customer newCustomer = new Customer();

        // Assert
        assertNotNull(newCustomer);
    }

    @Test
    void testAllArgsConstructor_ShouldCreateInstanceWithAllFields() {
        // Arrange & Act
        Customer newCustomer = new Customer(
                2L,
                "New Company",
                "new@example.com",
                987654321,
                categories,
                "Jane",
                "Smith",
                "New City",
                "456 New Street",
                1
        );

        // Assert
        assertNotNull(newCustomer);
        assertEquals(2L, newCustomer.getId());
        assertEquals("New Company", newCustomer.getName());
        assertEquals("new@example.com", newCustomer.getEmail());
    }

    @Test
    void testBuilder_ShouldCreateInstanceWithBuilder() {
        // Arrange & Act
        Customer newCustomer = Customer.builder()
                .id(3L)
                .name("Builder Company")
                .email("builder@example.com")
                .phone(111222333)
                .city("Builder City")
                .build();

        // Assert
        assertNotNull(newCustomer);
        assertEquals(3L, newCustomer.getId());
        assertEquals("Builder Company", newCustomer.getName());
    }

    @Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Customer newCustomer = new Customer();

        // Act
        newCustomer.setId(5L);
        newCustomer.setName("Setter Company");
        newCustomer.setEmail("setter@example.com");

        // Assert
        assertEquals(5L, newCustomer.getId());
        assertEquals("Setter Company", newCustomer.getName());
        assertEquals("setter@example.com", newCustomer.getEmail());
    }

    @Test
    void testSetName_ShouldUpdateName() {
        // Arrange & Act
        customer.setName("Updated Company");

        // Assert
        assertEquals("Updated Company", customer.getName());
    }

    @Test
    void testSetEmail_ShouldUpdateEmail() {
        // Arrange & Act
        customer.setEmail("updated@example.com");

        // Assert
        assertEquals("updated@example.com", customer.getEmail());
    }

    @Test
    void testSetPhone_ShouldUpdatePhone() {
        // Arrange & Act
        customer.setPhone(999888777);

        // Assert
        assertEquals(999888777, customer.getPhone());
    }

    @Test
    void testSetCategories_ShouldUpdateCategories() {
        // Arrange
        Category newCategory = new Category();
        newCategory.setId(2L);
        newCategory.setName("Standard");
        Set<Category> newCategories = new HashSet<>();
        newCategories.add(newCategory);

        // Act
        customer.setCategories(newCategories);

        // Assert
        assertEquals(newCategories, customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testSetFirstName_ShouldUpdateFirstName() {
        // Arrange & Act
        customer.setFirstName("UpdatedFirst");

        // Assert
        assertEquals("UpdatedFirst", customer.getFirstName());
    }

    @Test
    void testSetLastName_ShouldUpdateLastName() {
        // Arrange & Act
        customer.setLastName("UpdatedLast");

        // Assert
        assertEquals("UpdatedLast", customer.getLastName());
    }

    @Test
    void testSetCity_ShouldUpdateCity() {
        // Arrange & Act
        customer.setCity("Updated City");

        // Assert
        assertEquals("Updated City", customer.getCity());
    }

    @Test
    void testSetAddress_ShouldUpdateAddress() {
        // Arrange & Act
        customer.setAddress("789 Updated Street");

        // Assert
        assertEquals("789 Updated Street", customer.getAddress());
    }

    @Test
    void testSetEnabled_ShouldUpdateEnabled() {
        // Arrange & Act
        customer.setEnabled(0);

        // Assert
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testSetCategories_WithEmptySet_ShouldAcceptEmptySet() {
        // Arrange
        Set<Category> emptySet = new HashSet<>();

        // Act
        customer.setCategories(emptySet);

        // Assert
        assertNotNull(customer.getCategories());
        assertTrue(customer.getCategories().isEmpty());
    }

    @Test
    void testSetCategories_WithMultipleCategories_ShouldStoreAll() {
        // Arrange
        Category cat1 = new Category();
        cat1.setId(1L);
        cat1.setName("Category1");
        Category cat2 = new Category();
        cat2.setId(2L);
        cat2.setName("Category2");
        Set<Category> multipleCategories = new HashSet<>();
        multipleCategories.add(cat1);
        multipleCategories.add(cat2);

        // Act
        customer.setCategories(multipleCategories);

        // Assert
        assertEquals(2, customer.getCategories().size());
    }

    @Test
    void testSetNullValues_ShouldAcceptNull() {
        // Arrange & Act
        customer.setName(null);
        customer.setEmail(null);
        customer.setCategories(null);
        customer.setFirstName(null);
        customer.setLastName(null);
        customer.setCity(null);
        customer.setAddress(null);

        // Assert
        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getCategories());
        assertNull(customer.getFirstName());
        assertNull(customer.getLastName());
        assertNull(customer.getCity());
        assertNull(customer.getAddress());
    }
}

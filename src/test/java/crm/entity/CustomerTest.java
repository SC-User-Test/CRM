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
        assertNotNull(customer);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        Set<Category> categories = new HashSet<>();
        Customer c = new Customer(1L, "TestCo", "test@test.com", 123456789,
                categories, "John", "Doe", "NYC", "123 Main St", 1);
        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("TestCo", c.getName());
        assertEquals("test@test.com", c.getEmail());
        assertEquals(123456789, c.getPhone());
        assertEquals("John", c.getFirstName());
        assertEquals("Doe", c.getLastName());
        assertEquals("NYC", c.getCity());
        assertEquals("123 Main St", c.getAddress());
        assertEquals(1, c.getEnabled());
    }

    @Test
    void testBuilder_createsCustomerWithValues() {
        Customer c = Customer.builder()
                .id(1L)
                .name("BuilderCo")
                .email("builder@test.com")
                .phone(987654321)
                .firstName("Jane")
                .lastName("Smith")
                .city("LA")
                .address("456 Oak Ave")
                .enabled(1)
                .build();

        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("BuilderCo", c.getName());
        assertEquals("builder@test.com", c.getEmail());
        assertEquals(987654321, c.getPhone());
        assertEquals("Jane", c.getFirstName());
        assertEquals("Smith", c.getLastName());
        assertEquals("LA", c.getCity());
        assertEquals("456 Oak Ave", c.getAddress());
        assertEquals(1, c.getEnabled());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        customer.setId(5L);
        assertEquals(5L, customer.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        customer.setName("ACME Corp");
        assertEquals("ACME Corp", customer.getName());
    }

    @Test
    void testSetAndGetEmail_returnsCorrectEmail() {
        customer.setEmail("acme@example.com");
        assertEquals("acme@example.com", customer.getEmail());
    }

    @Test
    void testSetAndGetPhone_returnsCorrectPhone() {
        customer.setPhone(555123456);
        assertEquals(555123456, customer.getPhone());
    }

    @Test
    void testSetAndGetFirstName_returnsCorrectFirstName() {
        customer.setFirstName("Alice");
        assertEquals("Alice", customer.getFirstName());
    }

    @Test
    void testSetAndGetLastName_returnsCorrectLastName() {
        customer.setLastName("Johnson");
        assertEquals("Johnson", customer.getLastName());
    }

    @Test
    void testSetAndGetCity_returnsCorrectCity() {
        customer.setCity("Chicago");
        assertEquals("Chicago", customer.getCity());
    }

    @Test
    void testSetAndGetAddress_returnsCorrectAddress() {
        customer.setAddress("789 Elm St");
        assertEquals("789 Elm St", customer.getAddress());
    }

    @Test
    void testSetAndGetEnabled_withOne_returnsOne() {
        customer.setEnabled(1);
        assertEquals(1, customer.getEnabled());
    }

    @Test
    void testSetAndGetEnabled_withZero_returnsZero() {
        customer.setEnabled(0);
        assertEquals(0, customer.getEnabled());
    }

    @Test
    void testSetAndGetCategories_returnsCorrectCategories() {
        Set<Category> categories = new HashSet<>();
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Tech");
        categories.add(cat);
        customer.setCategories(categories);
        assertNotNull(customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testSetCategories_withEmptySet_returnsEmptySet() {
        customer.setCategories(new HashSet<>());
        assertNotNull(customer.getCategories());
        assertTrue(customer.getCategories().isEmpty());
    }

    @Test
    void testEquals_equalCustomers_returnsTrue() {
        Customer c1 = Customer.builder().id(1L).name("Co1").email("co1@test.com").build();
        Customer c2 = Customer.builder().id(1L).name("Co1").email("co1@test.com").build();
        assertEquals(c1, c2);
    }

    @Test
    void testEquals_differentCustomers_returnsFalse() {
        Customer c1 = Customer.builder().id(1L).name("Co1").build();
        Customer c2 = Customer.builder().id(2L).name("Co2").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCode_equalCustomers_sameHashCode() {
        Customer c1 = Customer.builder().id(1L).name("Co1").email("co1@test.com").build();
        Customer c2 = Customer.builder().id(1L).name("Co1").email("co1@test.com").build();
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString_notNull() {
        customer.setId(1L);
        customer.setName("TestCo");
        assertNotNull(customer.toString());
    }

    @Test
    void testSetId_withNull_returnsNull() {
        customer.setId(null);
        assertNull(customer.getId());
    }
}

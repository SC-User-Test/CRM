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
        Customer c = new Customer(1L, "TestCo", "test@example.com", 1234567890,
                categories, "John", "Doe", "New York", "123 Main St", 1);
        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("TestCo", c.getName());
        assertEquals("test@example.com", c.getEmail());
        assertEquals(1234567890, c.getPhone());
        assertEquals("John", c.getFirstName());
        assertEquals("Doe", c.getLastName());
        assertEquals("New York", c.getCity());
        assertEquals("123 Main St", c.getAddress());
        assertEquals(1, c.getEnabled());
    }

    @Test
    void testBuilder_createsCustomerWithAllFields() {
        Customer c = Customer.builder()
                .id(1L)
                .name("BuilderCo")
                .email("builder@example.com")
                .phone(9876543210L > 0 ? 987654321 : 0)
                .firstName("Jane")
                .lastName("Smith")
                .city("Chicago")
                .address("456 Oak Ave")
                .enabled(1)
                .build();

        assertEquals(1L, c.getId());
        assertEquals("BuilderCo", c.getName());
        assertEquals("builder@example.com", c.getEmail());
        assertEquals("Jane", c.getFirstName());
        assertEquals("Smith", c.getLastName());
        assertEquals("Chicago", c.getCity());
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
        customer.setCity("Los Angeles");
        assertEquals("Los Angeles", customer.getCity());
    }

    @Test
    void testSetAndGetAddress_returnsCorrectAddress() {
        customer.setAddress("789 Pine St");
        assertEquals("789 Pine St", customer.getAddress());
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
        cat.setName("VIP");
        categories.add(cat);
        customer.setCategories(categories);
        assertEquals(categories, customer.getCategories());
        assertEquals(1, customer.getCategories().size());
    }

    @Test
    void testSetCategories_withEmptySet_returnsEmptySet() {
        customer.setCategories(new HashSet<>());
        assertTrue(customer.getCategories().isEmpty());
    }

    @Test
    void testEquals_equalCustomers_returnsTrue() {
        Customer c1 = Customer.builder().id(1L).name("TestCo").email("test@example.com").build();
        Customer c2 = Customer.builder().id(1L).name("TestCo").email("test@example.com").build();
        assertEquals(c1, c2);
    }

    @Test
    void testEquals_differentCustomers_returnsFalse() {
        Customer c1 = Customer.builder().id(1L).name("TestCo").build();
        Customer c2 = Customer.builder().id(2L).name("OtherCo").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCode_equalCustomers_sameHashCode() {
        Customer c1 = Customer.builder().id(1L).name("TestCo").build();
        Customer c2 = Customer.builder().id(1L).name("TestCo").build();
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

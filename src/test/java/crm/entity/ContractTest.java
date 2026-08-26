package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        user.setEnabled(1);
        user.setRole(role);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Acme Corp");
        customer.setEmail("acme@example.com");
        customer.setPhone(123456789);
        customer.setEnabled(1);

        contract = new Contract();
        contract.setId(1L);
        contract.setName("Contract-001");
        contract.setContent("Contract content here");
        contract.setValue(new BigDecimal("10000.00"));
        contract.setBeginDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2024, 12, 31));
        contract.setStatus(Status.PROPOSED);
        contract.setCustomer(customer);
        contract.setUser(user);
    }

    @Test
    void testDefaultConstructor() {
        Contract c = new Contract();
        assertNotNull(c);
    }

    @Test
    void testAllArgsConstructor() {
        Contract c = new Contract(1L, "Test-Contract", "Content", new BigDecimal("5000.00"),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 30),
                Status.NEGOTIATED, customer, user);
        assertNotNull(c);
        assertEquals("Test-Contract", c.getName());
    }

    @Test
    void testBuilderPattern() {
        Contract c = Contract.builder()
                .id(2L)
                .name("Builder-Contract")
                .content("Builder content")
                .value(new BigDecimal("20000.00"))
                .beginDate(LocalDate.of(2024, 2, 1))
                .endDate(LocalDate.of(2024, 11, 30))
                .status(Status.IMPLEMENTED)
                .customer(customer)
                .user(user)
                .build();
        assertNotNull(c);
        assertEquals("Builder-Contract", c.getName());
        assertEquals(Status.IMPLEMENTED, c.getStatus());
    }

    @Test
    void testGetId() {
        assertEquals(1L, contract.getId());
    }

    @Test
    void testSetAndGetName() {
        contract.setName("New-Contract");
        assertEquals("New-Contract", contract.getName());
    }

    @Test
    void testSetAndGetContent() {
        contract.setContent("New content");
        assertEquals("New content", contract.getContent());
    }

    @Test
    void testSetAndGetValue() {
        contract.setValue(new BigDecimal("50000.00"));
        assertEquals(new BigDecimal("50000.00"), contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate() {
        LocalDate date = LocalDate.of(2024, 3, 15);
        contract.setBeginDate(date);
        assertEquals(date, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDate date = LocalDate.of(2025, 3, 15);
        contract.setEndDate(date);
        assertEquals(date, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatusProposed() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatusNegotiated() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatusImplemented() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatusDone() {
        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setId(2L);
        newCustomer.setName("New Corp");
        contract.setCustomer(newCustomer);
        assertEquals(newCustomer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser() {
        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("newuser");
        contract.setUser(newUser);
        assertEquals(newUser, contract.getUser());
    }

    @Test
    void testEqualsAndHashCode() {
        Contract c1 = Contract.builder().id(1L).name("Contract-001").content("Content")
                .value(new BigDecimal("10000.00")).beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31)).status(Status.PROPOSED)
                .customer(customer).user(user).build();
        Contract c2 = Contract.builder().id(1L).name("Contract-001").content("Content")
                .value(new BigDecimal("10000.00")).beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31)).status(Status.PROPOSED)
                .customer(customer).user(user).build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testNotEquals() {
        Contract c1 = Contract.builder().id(1L).name("Contract-001").build();
        Contract c2 = Contract.builder().id(2L).name("Contract-002").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testToString() {
        String str = contract.toString();
        assertNotNull(str);
        assertTrue(str.contains("Contract-001"));
    }

    @Test
    void testSetValueNull() {
        contract.setValue(null);
        assertNull(contract.getValue());
    }

    @Test
    void testSetBeginDateNull() {
        contract.setBeginDate(null);
        assertNull(contract.getBeginDate());
    }

    @Test
    void testSetEndDateNull() {
        contract.setEndDate(null);
        assertNull(contract.getEndDate());
    }

    @Test
    void testSetStatusNull() {
        contract.setStatus(null);
        assertNull(contract.getStatus());
    }

    @Test
    void testValueZero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }
}

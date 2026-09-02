package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    private Contract contract;

    @BeforeEach
    void setUp() {
        contract = new Contract();
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(contract);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        Customer customer = new Customer();
        User user = new User();
        LocalDate beginDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Contract c = new Contract(1L, "Contract1", "Content", BigDecimal.valueOf(1000),
                beginDate, endDate, Status.PROPOSED, customer, user);

        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("Contract1", c.getName());
        assertEquals("Content", c.getContent());
        assertEquals(BigDecimal.valueOf(1000), c.getValue());
        assertEquals(beginDate, c.getBeginDate());
        assertEquals(endDate, c.getEndDate());
        assertEquals(Status.PROPOSED, c.getStatus());
    }

    @Test
    void testBuilder_createsContractWithValues() {
        Contract c = Contract.builder()
                .id(1L)
                .name("TestContract")
                .content("Some content")
                .value(BigDecimal.valueOf(5000))
                .status(Status.NEGOTIATED)
                .build();

        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("TestContract", c.getName());
        assertEquals("Some content", c.getContent());
        assertEquals(BigDecimal.valueOf(5000), c.getValue());
        assertEquals(Status.NEGOTIATED, c.getStatus());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        contract.setId(10L);
        assertEquals(10L, contract.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        contract.setName("MyContract");
        assertEquals("MyContract", contract.getName());
    }

    @Test
    void testSetAndGetContent_returnsCorrectContent() {
        contract.setContent("Contract content here");
        assertEquals("Contract content here", contract.getContent());
    }

    @Test
    void testSetAndGetValue_returnsCorrectValue() {
        contract.setValue(BigDecimal.valueOf(9999.99));
        assertEquals(BigDecimal.valueOf(9999.99), contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate_returnsCorrectDate() {
        LocalDate date = LocalDate.of(2024, 1, 15);
        contract.setBeginDate(date);
        assertEquals(date, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate_returnsCorrectDate() {
        LocalDate date = LocalDate.of(2024, 12, 31);
        contract.setEndDate(date);
        assertEquals(date, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus_proposed() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_negotiated() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_implemented() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    void testSetAndGetStatus_done() {
        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer_returnsCorrectCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("TestCustomer");
        contract.setCustomer(customer);
        assertNotNull(contract.getCustomer());
        assertEquals("TestCustomer", contract.getCustomer().getName());
    }

    @Test
    void testSetAndGetUser_returnsCorrectUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        contract.setUser(user);
        assertNotNull(contract.getUser());
        assertEquals("testuser", contract.getUser().getUsername());
    }

    @Test
    void testEquals_equalContracts_returnsTrue() {
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(1L).name("C1").build();
        assertEquals(c1, c2);
    }

    @Test
    void testEquals_differentContracts_returnsFalse() {
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(2L).name("C2").build();
        assertNotEquals(c1, c2);
    }

    @Test
    void testHashCode_equalContracts_sameHashCode() {
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(1L).name("C1").build();
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString_notNull() {
        contract.setId(1L);
        contract.setName("TestContract");
        assertNotNull(contract.toString());
    }

    @Test
    void testSetValue_withNull_returnsNull() {
        contract.setValue(null);
        assertNull(contract.getValue());
    }

    @Test
    void testSetValue_withZero_returnsZero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }
}

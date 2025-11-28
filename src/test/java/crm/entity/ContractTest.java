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

    @BeforeEach
    void setUp() {
        contract = new Contract();
        customer = new Customer();
        user = new User();
    }

    @Test
    void testContractConstructor() {
        assertNotNull(contract);
    }

    @Test
    void testContractBuilder() {
        Contract builtContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();

        assertNotNull(builtContract);
        assertEquals(1L, builtContract.getId());
        assertEquals("Test Contract", builtContract.getName());
        assertEquals("Contract content", builtContract.getContent());
        assertEquals(new BigDecimal("10000.00"), builtContract.getValue());
        assertEquals(LocalDate.of(2025, 1, 1), builtContract.getBeginDate());
        assertEquals(LocalDate.of(2025, 12, 31), builtContract.getEndDate());
        assertEquals(Status.PROPOSED, builtContract.getStatus());
        assertEquals(customer, builtContract.getCustomer());
        assertEquals(user, builtContract.getUser());
    }

    @Test
    void testSetAndGetId() {
        contract.setId(1L);
        assertEquals(1L, contract.getId());
    }

    @Test
    void testSetAndGetName() {
        contract.setName("Contract Name");
        assertEquals("Contract Name", contract.getName());
    }

    @Test
    void testSetAndGetContent() {
        contract.setContent("This is contract content");
        assertEquals("This is contract content", contract.getContent());
    }

    @Test
    void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("50000.50");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate() {
        LocalDate beginDate = LocalDate.of(2025, 6, 1);
        contract.setBeginDate(beginDate);
        assertEquals(beginDate, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDate endDate = LocalDate.of(2026, 6, 1);
        contract.setEndDate(endDate);
        assertEquals(endDate, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());

        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer() {
        customer.setId(1L);
        customer.setName("Test Customer");
        contract.setCustomer(customer);
        assertEquals(customer, contract.getCustomer());
        assertEquals(1L, contract.getCustomer().getId());
    }

    @Test
    void testSetAndGetUser() {
        user.setId(1L);
        user.setUsername("testuser");
        contract.setUser(user);
        assertEquals(user, contract.getUser());
        assertEquals(1L, contract.getUser().getId());
    }

    @Test
    void testContractWithNullValues() {
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);
        contract.setBeginDate(null);
        contract.setEndDate(null);
        contract.setStatus(null);
        contract.setCustomer(null);
        contract.setUser(null);

        assertNull(contract.getName());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
        assertNull(contract.getBeginDate());
        assertNull(contract.getEndDate());
        assertNull(contract.getStatus());
        assertNull(contract.getCustomer());
        assertNull(contract.getUser());
    }

    @Test
    void testContractEqualsAndHashCode() {
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("Contract A")
                .value(new BigDecimal("1000"))
                .build();

        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Contract A")
                .value(new BigDecimal("1000"))
                .build();

        assertEquals(contract1, contract2);
        assertEquals(contract1.hashCode(), contract2.hashCode());
    }

    @Test
    void testContractToString() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Content")
                .build();

        String toString = contract.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Test Contract"));
    }
}

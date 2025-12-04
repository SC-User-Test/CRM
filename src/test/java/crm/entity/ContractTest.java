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
    void testContractBuilder() {
        Contract builtContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Test Content")
                .value(new BigDecimal("1000.00"))
                .beginDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();

        assertNotNull(builtContract);
        assertEquals(1L, builtContract.getId());
        assertEquals("Test Contract", builtContract.getName());
        assertEquals("Test Content", builtContract.getContent());
        assertEquals(new BigDecimal("1000.00"), builtContract.getValue());
        assertEquals(Status.PROPOSED, builtContract.getStatus());
    }

    @Test
    void testContractSettersAndGetters() {
        contract.setId(2L);
        contract.setName("Contract Name");
        contract.setContent("Contract Content");
        contract.setValue(new BigDecimal("5000.50"));
        contract.setBeginDate(LocalDate.of(2025, 6, 1));
        contract.setEndDate(LocalDate.of(2026, 6, 1));
        contract.setStatus(Status.NEGOTIATED);
        contract.setCustomer(customer);
        contract.setUser(user);

        assertEquals(2L, contract.getId());
        assertEquals("Contract Name", contract.getName());
        assertEquals("Contract Content", contract.getContent());
        assertEquals(new BigDecimal("5000.50"), contract.getValue());
        assertEquals(LocalDate.of(2025, 6, 1), contract.getBeginDate());
        assertEquals(LocalDate.of(2026, 6, 1), contract.getEndDate());
        assertEquals(Status.NEGOTIATED, contract.getStatus());
        assertEquals(customer, contract.getCustomer());
        assertEquals(user, contract.getUser());
    }

    @Test
    void testContractNoArgsConstructor() {
        Contract newContract = new Contract();
        assertNotNull(newContract);
        assertNull(newContract.getId());
        assertNull(newContract.getName());
        assertNull(newContract.getContent());
        assertNull(newContract.getValue());
        assertNull(newContract.getBeginDate());
        assertNull(newContract.getEndDate());
        assertNull(newContract.getStatus());
        assertNull(newContract.getCustomer());
        assertNull(newContract.getUser());
    }

    @Test
    void testContractAllArgsConstructor() {
        Contract allArgsContract = new Contract(
                3L,
                "All Args Contract",
                "Content",
                new BigDecimal("2500.00"),
                LocalDate.of(2025, 3, 1),
                LocalDate.of(2025, 9, 1),
                Status.IMPLEMENTED,
                customer,
                user
        );

        assertEquals(3L, allArgsContract.getId());
        assertEquals("All Args Contract", allArgsContract.getName());
        assertEquals("Content", allArgsContract.getContent());
        assertEquals(new BigDecimal("2500.00"), allArgsContract.getValue());
        assertEquals(Status.IMPLEMENTED, allArgsContract.getStatus());
    }

    @Test
    void testContractWithNullValues() {
        contract.setId(null);
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);
        contract.setBeginDate(null);
        contract.setEndDate(null);
        contract.setStatus(null);
        contract.setCustomer(null);
        contract.setUser(null);

        assertNull(contract.getId());
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
    void testContractStatusTransitions() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());

        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());

        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());

        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }
}

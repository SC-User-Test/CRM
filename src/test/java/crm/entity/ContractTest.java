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
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract Content")
                .value(BigDecimal.valueOf(1000.00))
                .beginDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void testConstructor() {
        Contract newContract = new Contract();
        assertNotNull(newContract);
    }

    @Test
    void testBuilder() {
        assertNotNull(contract);
        assertEquals("Test Contract", contract.getName());
        assertEquals("Contract Content", contract.getContent());
    }

    @Test
    void testGetters() {
        assertEquals(1L, contract.getId());
        assertEquals("Test Contract", contract.getName());
        assertEquals("Contract Content", contract.getContent());
        assertEquals(BigDecimal.valueOf(1000.00), contract.getValue());
        assertEquals(LocalDate.of(2023, 1, 1), contract.getBeginDate());
        assertEquals(LocalDate.of(2023, 12, 31), contract.getEndDate());
        assertEquals(Status.PROPOSED, contract.getStatus());
        assertNotNull(contract.getCustomer());
        assertNotNull(contract.getUser());
    }

    @Test
    void testSetters() {
        Contract newContract = new Contract();
        newContract.setId(2L);
        newContract.setName("New Contract");
        newContract.setContent("New Content");
        newContract.setValue(BigDecimal.valueOf(2000.00));
        newContract.setBeginDate(LocalDate.of(2024, 1, 1));
        newContract.setEndDate(LocalDate.of(2024, 12, 31));
        newContract.setStatus(Status.DONE);
        newContract.setCustomer(customer);
        newContract.setUser(user);

        assertEquals(2L, newContract.getId());
        assertEquals("New Contract", newContract.getName());
        assertEquals("New Content", newContract.getContent());
        assertEquals(BigDecimal.valueOf(2000.00), newContract.getValue());
        assertEquals(LocalDate.of(2024, 1, 1), newContract.getBeginDate());
        assertEquals(LocalDate.of(2024, 12, 31), newContract.getEndDate());
        assertEquals(Status.DONE, newContract.getStatus());
        assertNotNull(newContract.getCustomer());
        assertNotNull(newContract.getUser());
    }

    @Test
    void testStatus_AllValues() {
        contract.setStatus(Status.PROPOSED);
        assertEquals(Status.PROPOSED, contract.getStatus());

        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());

        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());

        contract.setStatus(Status.DONE);
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testValue_Comparison() {
        assertTrue(contract.getValue().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(contract.getValue().compareTo(BigDecimal.valueOf(999)) > 0);
        assertTrue(contract.getValue().compareTo(BigDecimal.valueOf(1001)) < 0);
    }

    @Test
    void testDates_Comparison() {
        assertTrue(contract.getBeginDate().isBefore(contract.getEndDate()));
        assertFalse(contract.getBeginDate().isAfter(contract.getEndDate()));
    }
}

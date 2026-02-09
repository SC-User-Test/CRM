package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    public void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("customer@example.com")
                .build();

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("user@example.com")
                .role(role)
                .build();

        contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    public void testContractCreation() {
        assertNotNull(contract);
    }

    @Test
    public void testContractBuilder() {
        Contract newContract = Contract.builder()
                .id(2L)
                .name("New Contract")
                .build();
        assertNotNull(newContract);
        assertEquals(2L, newContract.getId());
        assertEquals("New Contract", newContract.getName());
    }

    @Test
    public void testGetId() {
        assertEquals(1L, contract.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Contract", contract.getName());
    }

    @Test
    public void testGetContent() {
        assertEquals("Contract content", contract.getContent());
    }

    @Test
    public void testGetValue() {
        assertEquals(new BigDecimal("10000.00"), contract.getValue());
    }

    @Test
    public void testGetBeginDate() {
        assertEquals(LocalDate.of(2024, 1, 1), contract.getBeginDate());
    }

    @Test
    public void testGetEndDate() {
        assertEquals(LocalDate.of(2024, 12, 31), contract.getEndDate());
    }

    @Test
    public void testGetStatus() {
        assertEquals(Status.PROPOSED, contract.getStatus());
    }

    @Test
    public void testGetCustomer() {
        assertNotNull(contract.getCustomer());
        assertEquals("Test Customer", contract.getCustomer().getName());
    }

    @Test
    public void testGetUser() {
        assertNotNull(contract.getUser());
        assertEquals("testuser", contract.getUser().getUsername());
    }

    @Test
    public void testSetName() {
        contract.setName("Updated Contract");
        assertEquals("Updated Contract", contract.getName());
    }

    @Test
    public void testSetStatus() {
        contract.setStatus(Status.NEGOTIATED);
        assertEquals(Status.NEGOTIATED, contract.getStatus());
    }

    @Test
    public void testContractWithAllStatuses() {
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
    public void testContractEquality() {
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("Test")
                .build();
        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Test")
                .build();
        assertEquals(contract1, contract2);
    }

    @Test
    public void testContractWithNullValues() {
        Contract nullContract = Contract.builder()
                .id(3L)
                .name("Minimal Contract")
                .build();
        assertNull(nullContract.getContent());
        assertNull(nullContract.getValue());
        assertNull(nullContract.getBeginDate());
        assertNull(nullContract.getEndDate());
    }
}

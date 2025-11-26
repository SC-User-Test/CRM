package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    public void setUp() {
        contract = new Contract();
        customer = mock(Customer.class);
        user = mock(User.class);
    }

    @Test
    public void testConstructor() {
        Contract newContract = new Contract();
        assertNotNull(newContract);
    }

    @Test
    public void testBuilderPattern() {
        Contract builtContract = Contract.builder()
                .id(1L)
                .name("Service Contract")
                .content("Contract details")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2024, 1, 1))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();

        assertNotNull(builtContract);
        assertEquals(1L, builtContract.getId());
        assertEquals("Service Contract", builtContract.getName());
    }

    @Test
    public void testSetAndGetId() {
        contract.setId(1L);
        assertEquals(1L, contract.getId());
    }

    @Test
    public void testSetAndGetName() {
        contract.setName("Test Contract");
        assertEquals("Test Contract", contract.getName());
    }

    @Test
    public void testSetAndGetContent() {
        contract.setContent("Contract content here");
        assertEquals("Contract content here", contract.getContent());
    }

    @Test
    public void testSetAndGetValue() {
        BigDecimal value = new BigDecimal("5000.50");
        contract.setValue(value);
        assertEquals(value, contract.getValue());
    }

    @Test
    public void testSetAndGetBeginDate() {
        LocalDate beginDate = LocalDate.of(2023, 5, 15);
        contract.setBeginDate(beginDate);
        assertEquals(beginDate, contract.getBeginDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        LocalDate endDate = LocalDate.of(2024, 5, 15);
        contract.setEndDate(endDate);
        assertEquals(endDate, contract.getEndDate());
    }

    @Test
    public void testSetAndGetStatus() {
        contract.setStatus(Status.IMPLEMENTED);
        assertEquals(Status.IMPLEMENTED, contract.getStatus());
    }

    @Test
    public void testSetAndGetCustomer() {
        contract.setCustomer(customer);
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    public void testSetAndGetUser() {
        contract.setUser(user);
        assertEquals(user, contract.getUser());
    }

    @Test
    public void testValueWithZero() {
        contract.setValue(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, contract.getValue());
    }

    @Test
    public void testValueWithNegative() {
        BigDecimal negativeValue = new BigDecimal("-100.00");
        contract.setValue(negativeValue);
        assertEquals(negativeValue, contract.getValue());
    }

    @Test
    public void testAllStatuses() {
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
    public void testNullValues() {
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
    public void testAllArgsConstructor() {
        Contract newContract = new Contract(3L, "New Contract", "Details", new BigDecimal("2000"),
                LocalDate.now(), LocalDate.now().plusMonths(6), Status.NEGOTIATED, customer, user);
        assertNotNull(newContract);
        assertEquals(3L, newContract.getId());
        assertEquals("New Contract", newContract.getName());
        assertEquals(Status.NEGOTIATED, newContract.getStatus());
    }
}

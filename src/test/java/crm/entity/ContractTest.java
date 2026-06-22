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
    void testContractCreation() {
        assertNotNull(contract);
    }

    @Test
    void testSetAndGetId() {
        Long expectedId = 1L;
        contract.setId(expectedId);
        assertEquals(expectedId, contract.getId());
    }

    @Test
    void testSetAndGetName() {
        String expectedName = "Service Contract";
        contract.setName(expectedName);
        assertEquals(expectedName, contract.getName());
    }

    @Test
    void testSetAndGetContent() {
        String expectedContent = "Contract details and terms";
        contract.setContent(expectedContent);
        assertEquals(expectedContent, contract.getContent());
    }

    @Test
    void testSetAndGetValue() {
        BigDecimal expectedValue = new BigDecimal("10000.50");
        contract.setValue(expectedValue);
        assertEquals(expectedValue, contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate() {
        LocalDate expectedDate = LocalDate.of(2024, 1, 1);
        contract.setBeginDate(expectedDate);
        assertEquals(expectedDate, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate() {
        LocalDate expectedDate = LocalDate.of(2024, 12, 31);
        contract.setEndDate(expectedDate);
        assertEquals(expectedDate, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus() {
        Status expectedStatus = Status.PROPOSED;
        contract.setStatus(expectedStatus);
        assertEquals(expectedStatus, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer() {
        Customer expectedCustomer = new Customer();
        expectedCustomer.setId(1L);
        contract.setCustomer(expectedCustomer);
        assertEquals(expectedCustomer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        contract.setUser(expectedUser);
        assertEquals(expectedUser, contract.getUser());
    }

    @Test
    void testBuilderPattern() {
        Contract builtContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Test Content")
                .value(new BigDecimal("5000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .build();

        assertNotNull(builtContract);
        assertEquals(1L, builtContract.getId());
        assertEquals("Test Contract", builtContract.getName());
    }

    @Test
    void testNoArgsConstructor() {
        Contract contract = new Contract();
        assertNotNull(contract);
        assertNull(contract.getId());
    }

    @Test
    void testEntityAnnotationPresent() {
        assertTrue(Contract.class.isAnnotationPresent(jakarta.persistence.Entity.class));
    }
}

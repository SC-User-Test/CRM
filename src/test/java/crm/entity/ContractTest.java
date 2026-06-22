package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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
        customer = Customer.builder().id(1L).name("Test Customer").build();
        user = User.builder().id(1L).username("testuser").build();
    }

    @Test
    void contract_shouldBeCreated() {
        // Assert
        assertNotNull(contract);
    }

    @Test
    void builder_shouldCreateContractWithAllFields() {
        // Arrange & Act
        Contract contract = Contract.builder()
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

        // Assert
        assertNotNull(contract);
        assertEquals(1L, contract.getId());
        assertEquals("Test Contract", contract.getName());
        assertEquals("Contract content", contract.getContent());
        assertEquals(new BigDecimal("10000.00"), contract.getValue());
        assertEquals(LocalDate.of(2024, 1, 1), contract.getBeginDate());
        assertEquals(LocalDate.of(2024, 12, 31), contract.getEndDate());
        assertEquals(Status.PROPOSED, contract.getStatus());
        assertEquals(customer, contract.getCustomer());
        assertEquals(user, contract.getUser());
    }

    @Test
    void setId_shouldSetIdCorrectly() {
        // Arrange
        Long expectedId = 100L;

        // Act
        contract.setId(expectedId);

        // Assert
        assertEquals(expectedId, contract.getId());
    }

    @Test
    void setName_shouldSetNameCorrectly() {
        // Arrange
        String expectedName = "Service Agreement";

        // Act
        contract.setName(expectedName);

        // Assert
        assertEquals(expectedName, contract.getName());
    }

    @Test
    void setContent_shouldSetContentCorrectly() {
        // Arrange
        String expectedContent = "This is the contract content";

        // Act
        contract.setContent(expectedContent);

        // Assert
        assertEquals(expectedContent, contract.getContent());
    }

    @Test
    void setValue_shouldSetValueCorrectly() {
        // Arrange
        BigDecimal expectedValue = new BigDecimal("50000.00");

        // Act
        contract.setValue(expectedValue);

        // Assert
        assertEquals(expectedValue, contract.getValue());
    }

    @Test
    void setBeginDate_shouldSetBeginDateCorrectly() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2024, 6, 1);

        // Act
        contract.setBeginDate(expectedDate);

        // Assert
        assertEquals(expectedDate, contract.getBeginDate());
    }

    @Test
    void setEndDate_shouldSetEndDateCorrectly() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2025, 6, 1);

        // Act
        contract.setEndDate(expectedDate);

        // Assert
        assertEquals(expectedDate, contract.getEndDate());
    }

    @Test
    void setStatus_shouldSetStatusCorrectly() {
        // Arrange
        Status expectedStatus = Status.IMPLEMENTED;

        // Act
        contract.setStatus(expectedStatus);

        // Assert
        assertEquals(expectedStatus, contract.getStatus());
    }

    @Test
    void setCustomer_shouldSetCustomerCorrectly() {
        // Act
        contract.setCustomer(customer);

        // Assert
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    void setUser_shouldSetUserCorrectly() {
        // Act
        contract.setUser(user);

        // Assert
        assertEquals(user, contract.getUser());
    }

    @Test
    void contract_withNoArgsConstructor_shouldCreateEmptyContract() {
        // Act
        Contract emptyContract = new Contract();

        // Assert
        assertNotNull(emptyContract);
    }

    @Test
    void contract_withAllArgsConstructor_shouldCreateFullContract() {
        // Act
        Contract fullContract = new Contract(1L, "Contract", "Content", 
                new BigDecimal("1000"), LocalDate.now(), LocalDate.now().plusDays(30),
                Status.PROPOSED, customer, user);

        // Assert
        assertNotNull(fullContract);
        assertEquals(1L, fullContract.getId());
        assertEquals("Contract", fullContract.getName());
    }

    @Test
    void contract_shouldSupportEqualsAndHashCode() {
        // Arrange
        Contract contract1 = Contract.builder()
                .id(1L)
                .name("Test")
                .value(new BigDecimal("1000"))
                .build();

        Contract contract2 = Contract.builder()
                .id(1L)
                .name("Test")
                .value(new BigDecimal("1000"))
                .build();

        // Assert
        assertEquals(contract1, contract2);
        assertEquals(contract1.hashCode(), contract2.hashCode());
    }
}

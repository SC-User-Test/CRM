package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContractTest {

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Company");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
        contract.setContent("Test Content");
        contract.setValue(new BigDecimal("10000.00"));
        contract.setBeginDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2025, 1, 1));
        contract.setStatus(Status.PROPOSED);
        contract.setCustomer(customer);
        contract.setUser(user);
    }

    @Test
    void testNoArgsConstructor_ShouldCreateInstance() {
        // Arrange & Act
        Contract newContract = new Contract();

        // Assert
        assertNotNull(newContract);
    }

    @Test
    void testAllArgsConstructor_ShouldCreateInstanceWithAllFields() {
        // Arrange & Act
        Contract newContract = new Contract(
                2L,
                "New Contract",
                "New Content",
                new BigDecimal("20000.00"),
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2025, 6, 1),
                Status.NEGOTIATED,
                customer,
                user
        );

        // Assert
        assertNotNull(newContract);
        assertEquals(2L, newContract.getId());
        assertEquals("New Contract", newContract.getName());
    }

    @Test
    void testBuilder_ShouldCreateInstanceWithBuilder() {
        // Arrange & Act
        Contract newContract = Contract.builder()
                .id(3L)
                .name("Builder Contract")
                .value(new BigDecimal("30000.00"))
                .status(Status.IMPLEMENTED)
                .build();

        // Assert
        assertNotNull(newContract);
        assertEquals(3L, newContract.getId());
        assertEquals("Builder Contract", newContract.getName());
    }

    @Test
    void testGettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Contract newContract = new Contract();

        // Act
        newContract.setId(5L);
        newContract.setName("Setter Contract");
        newContract.setValue(new BigDecimal("50000.00"));

        // Assert
        assertEquals(5L, newContract.getId());
        assertEquals("Setter Contract", newContract.getName());
        assertEquals(new BigDecimal("50000.00"), newContract.getValue());
    }

    @Test
    void testSetName_ShouldUpdateName() {
        // Arrange & Act
        contract.setName("Updated Contract");

        // Assert
        assertEquals("Updated Contract", contract.getName());
    }

    @Test
    void testSetContent_ShouldUpdateContent() {
        // Arrange & Act
        contract.setContent("Updated Content");

        // Assert
        assertEquals("Updated Content", contract.getContent());
    }

    @Test
    void testSetValue_ShouldUpdateValue() {
        // Arrange
        BigDecimal newValue = new BigDecimal("99999.99");

        // Act
        contract.setValue(newValue);

        // Assert
        assertEquals(newValue, contract.getValue());
    }

    @Test
    void testSetBeginDate_ShouldUpdateBeginDate() {
        // Arrange
        LocalDate newDate = LocalDate.of(2024, 12, 1);

        // Act
        contract.setBeginDate(newDate);

        // Assert
        assertEquals(newDate, contract.getBeginDate());
    }

    @Test
    void testSetEndDate_ShouldUpdateEndDate() {
        // Arrange
        LocalDate newDate = LocalDate.of(2025, 12, 31);

        // Act
        contract.setEndDate(newDate);

        // Assert
        assertEquals(newDate, contract.getEndDate());
    }

    @Test
    void testSetStatus_ShouldUpdateStatus() {
        // Arrange & Act
        contract.setStatus(Status.DONE);

        // Assert
        assertEquals(Status.DONE, contract.getStatus());
    }

    @Test
    void testSetCustomer_ShouldUpdateCustomer() {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setId(2L);

        // Act
        contract.setCustomer(newCustomer);

        // Assert
        assertEquals(newCustomer, contract.getCustomer());
        assertEquals(2L, contract.getCustomer().getId());
    }

    @Test
    void testSetUser_ShouldUpdateUser() {
        // Arrange
        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("newuser");

        // Act
        contract.setUser(newUser);

        // Assert
        assertEquals(newUser, contract.getUser());
        assertEquals("newuser", contract.getUser().getUsername());
    }

    @Test
    void testSetNullValues_ShouldAcceptNull() {
        // Arrange & Act
        contract.setName(null);
        contract.setContent(null);
        contract.setValue(null);
        contract.setBeginDate(null);
        contract.setEndDate(null);
        contract.setStatus(null);
        contract.setCustomer(null);
        contract.setUser(null);

        // Assert
        assertNull(contract.getName());
        assertNull(contract.getContent());
        assertNull(contract.getValue());
        assertNull(contract.getBeginDate());
        assertNull(contract.getEndDate());
        assertNull(contract.getStatus());
        assertNull(contract.getCustomer());
        assertNull(contract.getUser());
    }
}

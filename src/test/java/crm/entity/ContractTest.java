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
        customer.setId(1L);
        customer.setName("Test Customer");

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        Contract c = new Contract();
        // Assert
        assertNotNull(c);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        // Arrange
        Long id = 1L;
        String name = "Contract A";
        String content = "Content";
        BigDecimal value = new BigDecimal("1000.00");
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        Status status = Status.PROPOSED;
        // Act
        Contract c = new Contract(id, name, content, value, beginDate, endDate, status, customer, user);
        // Assert
        assertNotNull(c);
        assertEquals(id, c.getId());
        assertEquals(name, c.getName());
        assertEquals(content, c.getContent());
        assertEquals(value, c.getValue());
        assertEquals(beginDate, c.getBeginDate());
        assertEquals(endDate, c.getEndDate());
        assertEquals(status, c.getStatus());
        assertEquals(customer, c.getCustomer());
        assertEquals(user, c.getUser());
    }

    @Test
    void testBuilder_createsContractWithAllFields() {
        // Arrange & Act
        Contract c = Contract.builder()
                .id(1L)
                .name("Contract B")
                .content("Some content")
                .value(new BigDecimal("500.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 6, 30))
                .status(Status.NEGOTIATED)
                .customer(customer)
                .user(user)
                .build();
        // Assert
        assertNotNull(c);
        assertEquals(1L, c.getId());
        assertEquals("Contract B", c.getName());
        assertEquals(Status.NEGOTIATED, c.getStatus());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        // Arrange
        Long expectedId = 10L;
        // Act
        contract.setId(expectedId);
        // Assert
        assertEquals(expectedId, contract.getId());
    }

    @Test
    void testSetAndGetName_returnsCorrectName() {
        // Arrange
        String expectedName = "Contract XYZ";
        // Act
        contract.setName(expectedName);
        // Assert
        assertEquals(expectedName, contract.getName());
    }

    @Test
    void testSetAndGetContent_returnsCorrectContent() {
        // Arrange
        String expectedContent = "This is the contract content.";
        // Act
        contract.setContent(expectedContent);
        // Assert
        assertEquals(expectedContent, contract.getContent());
    }

    @Test
    void testSetAndGetValue_returnsCorrectValue() {
        // Arrange
        BigDecimal expectedValue = new BigDecimal("2500.50");
        // Act
        contract.setValue(expectedValue);
        // Assert
        assertEquals(expectedValue, contract.getValue());
    }

    @Test
    void testSetAndGetBeginDate_returnsCorrectDate() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2024, 3, 15);
        // Act
        contract.setBeginDate(expectedDate);
        // Assert
        assertEquals(expectedDate, contract.getBeginDate());
    }

    @Test
    void testSetAndGetEndDate_returnsCorrectDate() {
        // Arrange
        LocalDate expectedDate = LocalDate.of(2025, 3, 15);
        // Act
        contract.setEndDate(expectedDate);
        // Assert
        assertEquals(expectedDate, contract.getEndDate());
    }

    @Test
    void testSetAndGetStatus_returnsCorrectStatus() {
        // Arrange
        Status expectedStatus = Status.IMPLEMENTED;
        // Act
        contract.setStatus(expectedStatus);
        // Assert
        assertEquals(expectedStatus, contract.getStatus());
    }

    @Test
    void testSetAndGetCustomer_returnsCorrectCustomer() {
        // Arrange & Act
        contract.setCustomer(customer);
        // Assert
        assertEquals(customer, contract.getCustomer());
    }

    @Test
    void testSetAndGetUser_returnsCorrectUser() {
        // Arrange & Act
        contract.setUser(user);
        // Assert
        assertEquals(user, contract.getUser());
    }

    @Test
    void testSetStatus_allStatusValues() {
        // Arrange & Act & Assert
        for (Status s : Status.values()) {
            contract.setStatus(s);
            assertEquals(s, contract.getStatus());
        }
    }

    @Test
    void testEquals_equalContracts_returnsTrue() {
        // Arrange
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(1L).name("C1").build();
        // Act & Assert
        assertEquals(c1, c2);
    }

    @Test
    void testEquals_differentContracts_returnsFalse() {
        // Arrange
        Contract c1 = Contract.builder().id(1L).name("C1").build();
        Contract c2 = Contract.builder().id(2L).name("C2").build();
        // Act & Assert
        assertNotEquals(c1, c2);
    }

    @Test
    void testToString_notNull() {
        // Arrange
        contract.setId(1L);
        contract.setName("Test");
        // Act
        String result = contract.toString();
        // Assert
        assertNotNull(result);
    }
}

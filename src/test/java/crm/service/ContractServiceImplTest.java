package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContractServiceImpl contractService;

    private Contract testContract;
    private Customer testCustomer;
    private User testUser;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder().id(1L).name("Test Customer").build();
        testUser = User.builder().id(1L).username("testuser").build();

        testContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .content("Contract content")
                .value(new BigDecimal("10000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(testCustomer)
                .user(testUser)
                .build();
    }

    @Test
    void findByName_withValidName_shouldReturnContract() {
        // Arrange
        when(contractRepository.findByName("Test Contract")).thenReturn(testContract);

        // Act
        Contract result = contractService.findByName("Test Contract");

        // Assert
        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository).findByName("Test Contract");
    }

    @Test
    void listAllContracts_shouldReturnAllContracts() {
        // Arrange
        when(contractRepository.findAll()).thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.listAllContracts();

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    void showContract_withValidId_shouldReturnContract() {
        // Arrange
        when(contractRepository.findById(1L)).thenReturn(Optional.of(testContract));

        // Act
        Contract result = contractService.showContract(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(contractRepository).findById(1L);
    }

    @Test
    void showContract_withInvalidId_shouldReturnNull() {
        // Arrange
        when(contractRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Contract result = contractService.showContract(999L);

        // Assert
        assertNull(result);
        verify(contractRepository).findById(999L);
    }

    @Test
    void findAllByValueLessThanEqual_shouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("15000.00");
        when(contractRepository.findAllByValueLessThanEqual(value))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void findAllByValueGreaterThanEqual_shouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("5000.00");
        when(contractRepository.findAllByValueGreaterThanEqual(value))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void findAllByBeginDate_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 1);
        when(contractRepository.findAllByBeginDate(date))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void findAllByBeginDateBefore_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 6, 1);
        when(contractRepository.findAllByBeginDateBefore(date))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateBefore(date);
    }

    @Test
    void findAllByBeginDateAfter_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 12, 1);
        when(contractRepository.findAllByBeginDateAfter(date))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateAfter(date);
    }

    @Test
    void findAllByEndDate_shouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 31);
        when(contractRepository.findAllByEndDate(date))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDate(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByEndDate(date);
    }

    @Test
    void findAllByStatus_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByStatus(Status.PROPOSED))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void findAllByCustomer_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByCustomer(testCustomer))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(testCustomer);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(testCustomer);
    }

    @Test
    void findAllByUser_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByUser(testUser))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByUser(testUser);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByUser(testUser);
    }

    @Test
    void findAllByCustomerAndUser_shouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByCustomerAndUser(testCustomer, testUser))
                .thenReturn(Arrays.asList(testContract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(testCustomer, testUser);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByCustomerAndUser(testCustomer, testUser);
    }

    @Test
    void saveContract_shouldSaveContract() {
        // Arrange
        when(contractRepository.save(any(Contract.class))).thenReturn(testContract);

        // Act
        contractService.saveContract(testContract);

        // Assert
        verify(contractRepository).save(testContract);
    }
}

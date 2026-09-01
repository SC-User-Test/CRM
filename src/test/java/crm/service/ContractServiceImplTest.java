package crm.service;

import crm.entity.*;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        contract = Contract.builder()
                .id(1L)
                .name("Contract A")
                .content("Content")
                .value(new BigDecimal("1000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        ContractServiceImpl service = new ContractServiceImpl(contractRepository, customerRepository, userRepository);
        // Assert
        assertNotNull(service);
    }

    @Test
    void testFindByName_existingName_returnsContract() {
        // Arrange
        when(contractRepository.findByName("Contract A")).thenReturn(contract);
        // Act
        Contract result = contractService.findByName("Contract A");
        // Assert
        assertNotNull(result);
        assertEquals("Contract A", result.getName());
        verify(contractRepository, times(1)).findByName("Contract A");
    }

    @Test
    void testFindByName_nonExistingName_returnsNull() {
        // Arrange
        when(contractRepository.findByName("NonExistent")).thenReturn(null);
        // Act
        Contract result = contractService.findByName("NonExistent");
        // Assert
        assertNull(result);
    }

    @Test
    void testListAllContracts_returnsAllContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAll()).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.listAllContracts();
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testShowContract_existingId_returnsContract() {
        // Arrange
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        // Act
        Contract result = contractService.showContract(1L);
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testShowContract_nonExistingId_returnsNull() {
        // Arrange
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());
        // Act
        Contract result = contractService.showContract(99L);
        // Assert
        assertNull(result);
    }

    @Test
    void testFindAllByValueLessThanEqual_returnsContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("2000.00");
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual_returnsContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("500.00");
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByBeginDate_returnsContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(date);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByBeginDateBefore_returnsContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 6, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateBefore(date);
    }

    @Test
    void testFindAllByBeginDateAfter_returnsContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateAfter(date);
    }

    @Test
    void testFindAllByEndDate_returnsContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDate(date)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByEndDate(date);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDate(date);
    }

    @Test
    void testFindAllByEndDateBefore_returnsContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByEndDateBefore(date);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateBefore(date);
    }

    @Test
    void testFindAllByEndDateAfter_returnsContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 6, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByEndDateAfter(date);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateAfter(date);
    }

    @Test
    void testFindAllByStatus_returnsContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByCustomer_returnsContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByCustomer(customer)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(customer);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByCustomerAndUser_returnsContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(customer, user);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testFindAllByUser_returnsContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByUser(user)).thenReturn(contracts);
        // Act
        Iterable<Contract> result = contractService.findAllByUser(user);
        // Assert
        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testSaveContract_savesSuccessfully() {
        // Arrange
        when(contractRepository.save(contract)).thenReturn(contract);
        // Act
        contractService.saveContract(contract);
        // Assert
        verify(contractRepository, times(1)).save(contract);
    }
}

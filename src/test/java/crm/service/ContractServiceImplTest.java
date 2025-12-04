package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
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
import java.util.Collections;
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
        customer = new Customer();
        customer.setId(1L);

        user = new User();
        user.setId(1L);

        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
        contract.setValue(new BigDecimal("10000.00"));
        contract.setBeginDate(LocalDate.of(2024, 1, 1));
        contract.setEndDate(LocalDate.of(2025, 1, 1));
        contract.setStatus(Status.PROPOSED);
        contract.setCustomer(customer);
        contract.setUser(user);
    }

    @Test
    void testConstructor_ShouldInitializeWithRepositories() {
        // Arrange & Act
        ContractServiceImpl service = new ContractServiceImpl(contractRepository, customerRepository, userRepository);

        // Assert
        assertNotNull(service);
    }

    @Test
    void testFindByName_ShouldReturnContract() {
        // Arrange
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        // Act
        Contract result = contractService.findByName("Test Contract");

        // Assert
        assertEquals(contract, result);
        verify(contractRepository).findByName("Test Contract");
    }

    @Test
    void testListAllContracts_ShouldReturnAllContracts() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAll()).thenReturn(contracts);

        // Act
        Iterable<Contract> result = contractService.listAllContracts();

        // Assert
        assertEquals(contracts, result);
        verify(contractRepository).findAll();
    }

    @Test
    void testShowContract_ShouldReturnContractById() {
        // Arrange
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        // Act
        Contract result = contractService.showContract(1L);

        // Assert
        assertEquals(contract, result);
        verify(contractRepository).findById(1L);
    }

    @Test
    void testFindAllByValueLessThanEqual_ShouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("10000.00");
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual_ShouldReturnContracts() {
        // Arrange
        BigDecimal value = new BigDecimal("5000.00");
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByBeginDate_ShouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 1, 1);
        when(contractRepository.findAllByBeginDate(date)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDate(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByBeginDateBefore_ShouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 6, 1);
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateBefore(date);
    }

    @Test
    void testFindAllByBeginDateAfter_ShouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2023, 12, 1);
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateAfter(date);
    }

    @Test
    void testFindAllByEndDate_ShouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 1, 1);
        when(contractRepository.findAllByEndDate(date)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDate(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByEndDate(date);
    }

    @Test
    void testFindAllByEndDateBefore_ShouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2025, 6, 1);
        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDateBefore(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByEndDateBefore(date);
    }

    @Test
    void testFindAllByEndDateAfter_ShouldReturnContracts() {
        // Arrange
        LocalDate date = LocalDate.of(2024, 12, 1);
        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByEndDateAfter(date);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByEndDateAfter(date);
    }

    @Test
    void testFindAllByStatus_ShouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByCustomer_ShouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByCustomer(customer)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomer(customer);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByCustomerAndUser_ShouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(customer, user);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testFindAllByUser_ShouldReturnContracts() {
        // Arrange
        when(contractRepository.findAllByUser(user)).thenReturn(Collections.singletonList(contract));

        // Act
        Iterable<Contract> result = contractService.findAllByUser(user);

        // Assert
        assertNotNull(result);
        verify(contractRepository).findAllByUser(user);
    }

    @Test
    void testSaveContract_ShouldSaveContract() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        // Act
        contractService.saveContract(contract);

        // Assert
        verify(contractRepository).save(contract);
    }
}

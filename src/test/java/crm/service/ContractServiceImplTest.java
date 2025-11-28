package crm.service;
import java.util.Optional;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.repository.ContractRepository;
import crm.repository.CustomerRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .build();

        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        Contract result = contractService.findByName("Test Contract");

        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository, times(1)).findByName("Test Contract");
    }

    @Test
    void testListAllContracts() {
        Contract contract1 = Contract.builder().id(1L).name("Contract1").build();
        Contract contract2 = Contract.builder().id(2L).name("Contract2").build();

        when(contractRepository.findAll()).thenReturn(Arrays.asList(contract1, contract2));

        Iterable<Contract> contracts = contractService.listAllContracts();

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testShowContract() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Contract Details")
                .build();

        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        Contract result = contractService.showContract(1L);

        assertNotNull(result);
        assertEquals("Contract Details", result.getName());
        verify(contractRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByValueLessThanEqual() {
        BigDecimal value = new BigDecimal("10000");

        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByValueLessThanEqual(value);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual() {
        BigDecimal value = new BigDecimal("50000");

        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByValueGreaterThanEqual(value);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByBeginDate() {
        LocalDate beginDate = LocalDate.of(2025, 1, 1);

        when(contractRepository.findAllByBeginDate(beginDate)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByBeginDate(beginDate);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByBeginDate(beginDate);
    }

    @Test
    void testFindAllByBeginDateBefore() {
        LocalDate beforeDate = LocalDate.of(2025, 6, 1);

        when(contractRepository.findAllByBeginDateBefore(beforeDate)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByBeginDateBefore(beforeDate);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByBeginDateBefore(beforeDate);
    }

    @Test
    void testFindAllByBeginDateAfter() {
        LocalDate afterDate = LocalDate.of(2024, 12, 31);

        when(contractRepository.findAllByBeginDateAfter(afterDate)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByBeginDateAfter(afterDate);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByBeginDateAfter(afterDate);
    }

    @Test
    void testFindAllByEndDate() {
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        when(contractRepository.findAllByEndDate(endDate)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByEndDate(endDate);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByEndDate(endDate);
    }

    @Test
    void testFindAllByEndDateBefore() {
        LocalDate beforeDate = LocalDate.of(2026, 1, 1);

        when(contractRepository.findAllByEndDateBefore(beforeDate)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByEndDateBefore(beforeDate);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByEndDateBefore(beforeDate);
    }

    @Test
    void testFindAllByEndDateAfter() {
        LocalDate afterDate = LocalDate.of(2025, 6, 30);

        when(contractRepository.findAllByEndDateAfter(afterDate)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByEndDateAfter(afterDate);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByEndDateAfter(afterDate);
    }

    @Test
    void testFindAllByStatus() {
        Status status = Status.PROPOSED;

        when(contractRepository.findAllByStatus(status)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByStatus(status);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByStatus(status);
    }

    @Test
    void testFindAllByCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .build();

        when(contractRepository.findAllByCustomer(customer)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByCustomer(customer);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByCustomerAndUser() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Customer")
                .build();

        User user = User.builder()
                .id(1L)
                .username("user")
                .build();

        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByCustomerAndUser(customer, user);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testFindAllByUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(contractRepository.findAllByUser(user)).thenReturn(Arrays.asList());

        Iterable<Contract> contracts = contractService.findAllByUser(user);

        assertNotNull(contracts);
        verify(contractRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testSaveContract() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("New Contract")
                .value(new BigDecimal("100000"))
                .build();

        when(customerRepository.findAll()).thenReturn(Arrays.asList());
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        contractService.saveContract(contract);

        verify(contractRepository, times(1)).save(contract);
        verify(customerRepository, times(1)).saveAll(any());
        verify(userRepository, times(1)).saveAll(any());
    }
}

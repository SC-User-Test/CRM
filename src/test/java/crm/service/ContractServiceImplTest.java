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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);

        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
        contract.setValue(BigDecimal.valueOf(1000));
        contract.setBeginDate(LocalDate.of(2023, 1, 1));
        contract.setEndDate(LocalDate.of(2023, 12, 31));
        contract.setStatus(Status.PROPOSED);

        customer = new Customer();
        customer.setId(1L);

        user = new User();
        user.setId(1L);
    }

    @Test
    void testConstructor() {
        ContractServiceImpl service = new ContractServiceImpl(contractRepository, customerRepository, userRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName() {
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);

        Contract result = contractService.findByName("Test Contract");

        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository, times(1)).findByName("Test Contract");
    }

    @Test
    void testListAllContracts() {
        when(contractRepository.findAll()).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.listAllContracts();

        assertNotNull(result);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testShowContract() {
        when(contractRepository.findById(1L)).thenReturn(java.util.Optional.of(contract));

        Contract result = contractService.showContract(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(contractRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByValueLessThanEqual() {
        when(contractRepository.findAllByValueLessThanEqual(BigDecimal.valueOf(1000))).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(BigDecimal.valueOf(1000));

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(BigDecimal.valueOf(1000));
    }

    @Test
    void testFindAllByValueGreaterThanEqual() {
        when(contractRepository.findAllByValueGreaterThanEqual(BigDecimal.valueOf(500))).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(BigDecimal.valueOf(500));

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueGreaterThanEqual(BigDecimal.valueOf(500));
    }

    @Test
    void testFindAllByBeginDate() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        when(contractRepository.findAllByBeginDate(date)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByBeginDate(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByBeginDateBefore() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateBefore(date);
    }

    @Test
    void testFindAllByBeginDateAfter() {
        LocalDate date = LocalDate.of(2022, 12, 31);
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDateAfter(date);
    }

    @Test
    void testFindAllByEndDate() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        when(contractRepository.findAllByEndDate(date)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByEndDate(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDate(date);
    }

    @Test
    void testFindAllByEndDateBefore() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByEndDateBefore(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateBefore(date);
    }

    @Test
    void testFindAllByEndDateAfter() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByEndDateAfter(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByEndDateAfter(date);
    }

    @Test
    void testFindAllByStatus() {
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByCustomer() {
        when(contractRepository.findAllByCustomer(customer)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByCustomer(customer);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByCustomerAndUser() {
        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByCustomerAndUser(customer, user);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testFindAllByUser() {
        when(contractRepository.findAllByUser(user)).thenReturn(Collections.singletonList(contract));

        Iterable<Contract> result = contractService.findAllByUser(user);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testSaveContract() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        contractService.saveContract(contract);

        verify(contractRepository, times(1)).save(contract);
    }
}

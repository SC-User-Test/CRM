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
        customer = Customer.builder().id(1L).name("TestCustomer").build();
        user = User.builder().id(1L).username("testuser").build();
        contract = Contract.builder()
                .id(1L)
                .name("TestContract")
                .value(BigDecimal.valueOf(1000))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        ContractServiceImpl service = new ContractServiceImpl(contractRepository, customerRepository, userRepository);
        assertNotNull(service);
    }

    @Test
    void testFindByName_existingContract_returnsContract() {
        when(contractRepository.findByName("TestContract")).thenReturn(contract);
        Contract result = contractService.findByName("TestContract");
        assertNotNull(result);
        assertEquals("TestContract", result.getName());
        verify(contractRepository).findByName("TestContract");
    }

    @Test
    void testFindByName_nonExistingContract_returnsNull() {
        when(contractRepository.findByName("NonExistent")).thenReturn(null);
        Contract result = contractService.findByName("NonExistent");
        assertNull(result);
    }

    @Test
    void testListAllContracts_returnsAllContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAll()).thenReturn(contracts);
        Iterable<Contract> result = contractService.listAllContracts();
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    void testShowContract_existingId_returnsContract() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        Contract result = contractService.showContract(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testShowContract_nonExistingId_returnsNull() {
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());
        Contract result = contractService.showContract(99L);
        assertNull(result);
    }

    @Test
    void testFindAllByValueLessThanEqual_returnsContracts() {
        BigDecimal value = BigDecimal.valueOf(2000);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByValueGreaterThanEqual_returnsContracts() {
        BigDecimal value = BigDecimal.valueOf(500);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    void testFindAllByBeginDate_returnsContracts() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByBeginDate(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByBeginDateBefore_returnsContracts() {
        LocalDate date = LocalDate.of(2023, 6, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateBefore(date);
    }

    @Test
    void testFindAllByBeginDateAfter_returnsContracts() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateAfter(date);
    }

    @Test
    void testFindAllByEndDate_returnsContracts() {
        LocalDate date = LocalDate.of(2023, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDate(date)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByEndDate(date);
        assertNotNull(result);
        verify(contractRepository).findAllByEndDate(date);
    }

    @Test
    void testFindAllByEndDateBefore_returnsContracts() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByEndDateBefore(date);
        assertNotNull(result);
        verify(contractRepository).findAllByEndDateBefore(date);
    }

    @Test
    void testFindAllByEndDateAfter_returnsContracts() {
        LocalDate date = LocalDate.of(2022, 12, 31);
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByEndDateAfter(date);
        assertNotNull(result);
        verify(contractRepository).findAllByEndDateAfter(date);
    }

    @Test
    void testFindAllByStatus_proposed_returnsContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testFindAllByStatus_done_returnsContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByStatus(Status.DONE)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByStatus(Status.DONE);
        assertNotNull(result);
    }

    @Test
    void testFindAllByCustomer_returnsContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByCustomer(customer)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByCustomer(customer);
        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByCustomerAndUser_returnsContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(customer, user);
        assertNotNull(result);
        verify(contractRepository).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testFindAllByUser_returnsContracts() {
        List<Contract> contracts = Arrays.asList(contract);
        when(contractRepository.findAllByUser(user)).thenReturn(contracts);
        Iterable<Contract> result = contractService.findAllByUser(user);
        assertNotNull(result);
        verify(contractRepository).findAllByUser(user);
    }

    @Test
    void testSaveContract_callsRepositorySave() {
        contractService.saveContract(contract);
        verify(contractRepository).save(contract);
    }

    @Test
    void testSaveContract_withNewContract_savesSuccessfully() {
        Contract newContract = Contract.builder().name("NewContract").build();
        contractService.saveContract(newContract);
        verify(contractRepository).save(newContract);
    }
}

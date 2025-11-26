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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceImplTest {

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
    public void setUp() {
        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
        customer = new Customer();
        user = new User();
    }

    @Test
    public void testConstructor() {
        ContractServiceImpl service = new ContractServiceImpl(contractRepository, customerRepository, userRepository);
        assertNotNull(service);
    }

    @Test
    public void testFindByName() {
        when(contractRepository.findByName("Test Contract")).thenReturn(contract);
        Contract result = contractService.findByName("Test Contract");
        assertEquals(contract, result);
        verify(contractRepository).findByName("Test Contract");
    }

    @Test
    public void testListAllContracts() {
        when(contractRepository.findAll()).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.listAllContracts();
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    public void testShowContract() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));
        Contract result = contractService.showContract(1L);
        assertEquals(contract, result);
        verify(contractRepository).findById(1L);
    }

    @Test
    public void testShowContractNotFound() {
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());
        Contract result = contractService.showContract(99L);
        assertNull(result);
    }

    @Test
    public void testFindAllByValueLessThanEqual() {
        BigDecimal value = new BigDecimal("1000");
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    public void testFindAllByValueGreaterThanEqual() {
        BigDecimal value = new BigDecimal("5000");
        when(contractRepository.findAllByValueGreaterThanEqual(value)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByValueGreaterThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueGreaterThanEqual(value);
    }

    @Test
    public void testFindAllByBeginDate() {
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByBeginDate(date)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByBeginDate(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDate(date);
    }

    @Test
    public void testFindAllByBeginDateBefore() {
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByBeginDateBefore(date)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByBeginDateBefore(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateBefore(date);
    }

    @Test
    public void testFindAllByBeginDateAfter() {
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByBeginDateAfter(date)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByBeginDateAfter(date);
        assertNotNull(result);
        verify(contractRepository).findAllByBeginDateAfter(date);
    }

    @Test
    public void testFindAllByEndDate() {
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByEndDate(date)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByEndDate(date);
        assertNotNull(result);
        verify(contractRepository).findAllByEndDate(date);
    }

    @Test
    public void testFindAllByEndDateBefore() {
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByEndDateBefore(date)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByEndDateBefore(date);
        assertNotNull(result);
        verify(contractRepository).findAllByEndDateBefore(date);
    }

    @Test
    public void testFindAllByEndDateAfter() {
        LocalDate date = LocalDate.now();
        when(contractRepository.findAllByEndDateAfter(date)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByEndDateAfter(date);
        assertNotNull(result);
        verify(contractRepository).findAllByEndDateAfter(date);
    }

    @Test
    public void testFindAllByStatus() {
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    public void testFindAllByCustomer() {
        when(contractRepository.findAllByCustomer(customer)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByCustomer(customer);
        assertNotNull(result);
        verify(contractRepository).findAllByCustomer(customer);
    }

    @Test
    public void testFindAllByCustomerAndUser() {
        when(contractRepository.findAllByCustomerAndUser(customer, user)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByCustomerAndUser(customer, user);
        assertNotNull(result);
        verify(contractRepository).findAllByCustomerAndUser(customer, user);
    }

    @Test
    public void testFindAllByUser() {
        when(contractRepository.findAllByUser(user)).thenReturn(Arrays.asList(contract));
        Iterable<Contract> result = contractService.findAllByUser(user);
        assertNotNull(result);
        verify(contractRepository).findAllByUser(user);
    }

    @Test
    public void testSaveContract() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(customerRepository.saveAll(anyIterable())).thenReturn(Arrays.asList(customer));
        when(userRepository.saveAll(anyIterable())).thenReturn(Arrays.asList(user));
        when(contractRepository.save(contract)).thenReturn(contract);

        contractService.saveContract(contract);

        verify(customerRepository).saveAll(anyIterable());
        verify(userRepository).saveAll(anyIterable());
        verify(contractRepository).save(contract);
    }
}

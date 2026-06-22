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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        testContract = new Contract();
        testContract.setId(1L);
        testContract.setName("Test Contract");
        testContract.setValue(new BigDecimal("10000.00"));
        testContract.setBeginDate(LocalDate.of(2024, 1, 1));
        testContract.setEndDate(LocalDate.of(2024, 12, 31));
        testContract.setStatus(Status.PROPOSED);

        testCustomer = new Customer();
        testCustomer.setId(1L);

        testUser = new User();
        testUser.setId(1L);
    }

    @Test
    void findByName_shouldReturnContract() {
        when(contractRepository.findByName("Test Contract")).thenReturn(testContract);
        Contract result = contractService.findByName("Test Contract");
        assertNotNull(result);
        assertEquals("Test Contract", result.getName());
        verify(contractRepository).findByName("Test Contract");
    }

    @Test
    void listAllContracts_shouldReturnAllContracts() {
        when(contractRepository.findAll()).thenReturn(Arrays.asList(testContract));
        Iterable<Contract> result = contractService.listAllContracts();
        assertNotNull(result);
        verify(contractRepository).findAll();
    }

    @Test
    void showContract_shouldReturnContractById() {
        when(contractRepository.findById(1L)).thenReturn(Optional.of(testContract));
        Contract result = contractService.showContract(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(contractRepository).findById(1L);
    }

    @Test
    void showContract_withNonExistentId_shouldReturnNull() {
        when(contractRepository.findById(999L)).thenReturn(Optional.empty());
        Contract result = contractService.showContract(999L);
        assertNull(result);
        verify(contractRepository).findById(999L);
    }

    @Test
    void findAllByValueLessThanEqual_shouldReturnContracts() {
        BigDecimal value = new BigDecimal("10000.00");
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(Arrays.asList(testContract));
        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);
        assertNotNull(result);
        verify(contractRepository).findAllByValueLessThanEqual(value);
    }

    @Test
    void findAllByStatus_shouldReturnContracts() {
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(Arrays.asList(testContract));
        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);
        assertNotNull(result);
        verify(contractRepository).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void saveContract_shouldSaveContract() {
        when(contractRepository.save(any(Contract.class))).thenReturn(testContract);
        contractService.saveContract(testContract);
        verify(contractRepository).save(testContract);
    }

    @Test
    void constructor_shouldInitializeRepositories() {
        ContractRepository contractRepo = mock(ContractRepository.class);
        CustomerRepository customerRepo = mock(CustomerRepository.class);
        UserRepository userRepo = mock(UserRepository.class);
        ContractServiceImpl service = new ContractServiceImpl(contractRepo, customerRepo, userRepo);
        assertNotNull(service);
    }
}

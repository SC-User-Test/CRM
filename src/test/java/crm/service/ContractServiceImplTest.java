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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        Contract contract = new Contract();
        contract.setName("TestContract");
        when(contractRepository.findByName("TestContract")).thenReturn(contract);

        Contract result = contractService.findByName("TestContract");

        assertNotNull(result);
        assertEquals("TestContract", result.getName());
        verify(contractRepository, times(1)).findByName("TestContract");
    }

    @Test
    void testListAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAll()).thenReturn(contracts);

        Iterable<Contract> result = contractService.listAllContracts();

        assertNotNull(result);
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testShowContract() {
        Contract contract = new Contract();
        contract.setId(1L);
        when(contractRepository.findById(1L)).thenReturn(Optional.of(contract));

        Contract result = contractService.showContract(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(contractRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByValueLessThanEqual() {
        BigDecimal value = new BigDecimal("1000");
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByValueLessThanEqual(value)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByValueLessThanEqual(value);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByValueLessThanEqual(value);
    }

    @Test
    void testFindAllByBeginDate() {
        LocalDate date = LocalDate.now();
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByBeginDate(date)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByBeginDate(date);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByBeginDate(date);
    }

    @Test
    void testFindAllByStatus() {
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByStatus(Status.PROPOSED);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testSaveContract() {
        Contract contract = new Contract();
        when(customerRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> contractService.saveContract(contract));
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void testFindAllByCustomer() {
        Customer customer = new Customer();
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByCustomer(customer)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByCustomer(customer);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByCustomer(customer);
    }

    @Test
    void testFindAllByUser() {
        User user = new User();
        List<Contract> contracts = new ArrayList<>();
        when(contractRepository.findAllByUser(user)).thenReturn(contracts);

        Iterable<Contract> result = contractService.findAllByUser(user);

        assertNotNull(result);
        verify(contractRepository, times(1)).findAllByUser(user);
    }
}

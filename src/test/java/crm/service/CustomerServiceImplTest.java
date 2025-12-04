package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(10L);

        Long result = customerService.getMaxId();

        assertEquals(10L, result);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    void testListAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);

        Iterable<Customer> result = customerService.listAllCustomers();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testShowCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.showCustomer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByEnabledTrue() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);

        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindOneByEnabledTrueAndName() {
        Customer customer = new Customer();
        customer.setName("Test");
        when(customerRepository.findOneByEnabledAndName(1, "Test")).thenReturn(customer);

        Customer result = customerService.findOneByEnabledTrueAndName("Test");

        assertNotNull(result);
        assertEquals("Test", result.getName());
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, "Test");
    }

    @Test
    void testFindByEnabledTrueAndEmail() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndPhone(1, 123456)).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndPhone(1, 123456);
    }

    @Test
    void testSaveCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);

        assertDoesNotThrow(() -> customerService.saveCustomer(customer));
        assertEquals(1, customer.getEnabled());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testFindByEnabledTrueAndCity() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndCity(1, "TestCity")).thenReturn(customers);

        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("TestCity");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCity(1, "TestCity");
    }
}

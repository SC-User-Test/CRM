package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);
        customer.setEnabled(1);

        categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        categories.add(category);
    }

    @Test
    void testConstructor() {
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository);
        assertNotNull(service);
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
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.listAllCustomers();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testShowCustomer() {
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(customer));

        Customer result = customerService.showCustomer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByEnabledTrue() {
        when(customerRepository.findAllByEnabled(1)).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse() {
        when(customerRepository.findAllByEnabled(0)).thenReturn(Collections.emptyList());

        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName() {
        when(customerRepository.findOneByEnabledAndName(1, "Test Customer")).thenReturn(customer);

        Customer result = customerService.findOneByEnabledTrueAndName("Test Customer");

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, "Test Customer");
    }

    @Test
    void testFindOneByEnabledFalseAndName() {
        when(customerRepository.findOneByEnabledAndName(0, "Test Customer")).thenReturn(null);

        Customer result = customerService.findOneByEnabledFalseAndName("Test Customer");

        assertNull(result);
        verify(customerRepository, times(1)).findOneByEnabledAndName(0, "Test Customer");
    }

    @Test
    void testFindOneByName() {
        when(customerRepository.findOneByName("Test Customer")).thenReturn(customer);

        Customer result = customerService.findOneByName("Test Customer");

        assertNotNull(result);
        verify(customerRepository, times(1)).findOneByName("Test Customer");
    }

    @Test
    void testFindByEnabledTrueAndEmail() {
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void testFindByEnabledFalseAndEmail() {
        when(customerRepository.findByEnabledAndEmail(0, "test@example.com")).thenReturn(Collections.emptyList());

        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndEmail(0, "test@example.com");
    }

    @Test
    void testFindByEmail() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEmail("test@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone() {
        when(customerRepository.findByEnabledAndPhone(1, 123456789)).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void testFindByPhone() {
        when(customerRepository.findByPhone(123456789)).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByPhone(123456789);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByPhone(123456789);
    }

    @Test
    void testFindByEnabledTrueAndFirstName() {
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void testFindByFirstName() {
        when(customerRepository.findByFirstName("John")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByFirstName("John");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByFirstName("John");
    }

    @Test
    void testFindByEnabledTrueAndLastName() {
        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndLastName(1, "Doe");
    }

    @Test
    void testFindByLastName() {
        when(customerRepository.findByLastName("Doe")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByLastName("Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByLastName("Doe");
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName() {
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndFirstNameAndLastName(1, "John", "Doe");
    }

    @Test
    void testFindByFirstNameAndLastName() {
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindByEnabledTrueAndCity() {
        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCity(1, "New York");
    }

    @Test
    void testFindByCity() {
        when(customerRepository.findByCity("New York")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByCity("New York");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByCity("New York");
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress() {
        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCityAndAddress(1, "New York", "123 Main St");
    }

    @Test
    void testFindByCityAndAddress() {
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");

        assertNotNull(result);
        verify(customerRepository, times(1)).findByCityAndAddress("New York", "123 Main St");
    }

    @Test
    void testSaveCustomer() {
        customerService.saveCustomer(customer);

        assertEquals(1, customer.getEnabled());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testFindByEnabledTrueAndCategories() {
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByEnabledAndCategories(1, categories);
    }

    @Test
    void testFindByCategories() {
        when(customerRepository.findByCategories(categories)).thenReturn(Collections.singletonList(customer));

        Iterable<Customer> result = customerService.findByCategories(categories);

        assertNotNull(result);
        verify(customerRepository, times(1)).findByCategories(categories);
    }
}

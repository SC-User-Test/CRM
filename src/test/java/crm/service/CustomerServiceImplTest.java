package crm.service;
import java.util.Optional;

import crm.entity.Category;
import crm.entity.Customer;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(100L);

        Long maxId = customerService.getMaxId();

        assertEquals(100L, maxId);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    void testListAllCustomers() {
        Customer customer1 = Customer.builder().id(1L).name("Customer1").build();
        Customer customer2 = Customer.builder().id(2L).name("Customer2").build();

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        Iterable<Customer> customers = customerService.listAllCustomers();

        assertNotNull(customers);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testShowCustomer() {
        Customer customer = Customer.builder().id(1L).name("Test Customer").build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.showCustomer(1L);

        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAllByEnabledTrue() {
        when(customerRepository.findAllByEnabled(1)).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findAllByEnabledTrue();

        assertNotNull(customers);
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse() {
        when(customerRepository.findAllByEnabled(0)).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findAllByEnabledFalse();

        assertNotNull(customers);
        verify(customerRepository, times(1)).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName() {
        Customer customer = Customer.builder().id(1L).name("John Doe").enabled(1).build();

        when(customerRepository.findOneByEnabledAndName(1, "John Doe")).thenReturn(customer);

        Customer result = customerService.findOneByEnabledTrueAndName("John Doe");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerRepository, times(1)).findOneByEnabledAndName(1, "John Doe");
    }

    @Test
    void testFindOneByEnabledFalseAndName() {
        Customer customer = Customer.builder().id(1L).name("Jane Doe").enabled(0).build();

        when(customerRepository.findOneByEnabledAndName(0, "Jane Doe")).thenReturn(customer);

        Customer result = customerService.findOneByEnabledFalseAndName("Jane Doe");

        assertNotNull(result);
        verify(customerRepository, times(1)).findOneByEnabledAndName(0, "Jane Doe");
    }

    @Test
    void testFindOneByName() {
        Customer customer = Customer.builder().id(1L).name("Test").build();

        when(customerRepository.findOneByName("Test")).thenReturn(customer);

        Customer result = customerService.findOneByName("Test");

        assertNotNull(result);
        assertEquals("Test", result.getName());
    }

    @Test
    void testFindByEnabledTrueAndEmail() {
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndEmail("test@example.com");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone() {
        when(customerRepository.findByEnabledAndPhone(1, 123456789)).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndPhone(123456789);

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void testFindByEnabledTrueAndCategories() {
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        categories.add(category);

        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndCategories(categories);

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndCategories(1, categories);
    }

    @Test
    void testFindByEnabledTrueAndFirstName() {
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndFirstName("John");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void testFindByEnabledTrueAndLastName() {
        when(customerRepository.findByEnabledAndLastName(1, "Smith")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndLastName("Smith");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndLastName(1, "Smith");
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName() {
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndFirstNameAndLastName(1, "John", "Doe");
    }

    @Test
    void testFindByEnabledTrueAndCity() {
        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndCity("New York");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndCity(1, "New York");
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress() {
        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEnabledAndCityAndAddress(1, "New York", "123 Main St");
    }

    @Test
    void testSaveCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("New Customer")
                .email("new@example.com")
                .build();

        customerService.saveCustomer(customer);

        assertEquals(1, customer.getEnabled());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testFindByEmail() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByEmail("test@example.com");

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindByPhone() {
        when(customerRepository.findByPhone(987654321)).thenReturn(Arrays.asList());

        Iterable<Customer> customers = customerService.findByPhone(987654321);

        assertNotNull(customers);
        verify(customerRepository, times(1)).findByPhone(987654321);
    }
}

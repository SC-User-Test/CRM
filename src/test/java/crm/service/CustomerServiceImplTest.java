package crm.service;

import crm.entity.Customer;
import crm.entity.Category;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("VIP");
        categories = new HashSet<>();
        categories.add(cat);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Acme Corp");
        customer.setEmail("acme@example.com");
        customer.setPhone(123456789);
        customer.setCategories(categories);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("New York");
        customer.setAddress("123 Main St");
        customer.setEnabled(1);
    }

    @Test
    void testGetMaxId() {
        when(customerRepository.getMaxId()).thenReturn(5L);
        Long maxId = customerService.getMaxId();
        assertEquals(5L, maxId);
        verify(customerRepository).getMaxId();
    }

    @Test
    void testGetMaxIdReturnsNull() {
        when(customerRepository.getMaxId()).thenReturn(null);
        Long maxId = customerService.getMaxId();
        assertNull(maxId);
    }

    @Test
    void testListAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        Iterable<Customer> result = customerService.listAllCustomers();
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void testListAllCustomersEmpty() {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.listAllCustomers();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testShowCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer result = customerService.showCustomer(1L);
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testShowCustomerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        Customer result = customerService.showCustomer(99L);
        assertNull(result);
    }

    @Test
    void testFindAllByEnabledTrue() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByEnabledTrue();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse() {
        when(customerRepository.findAllByEnabled(0)).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findAllByEnabledFalse();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName() {
        when(customerRepository.findOneByEnabledAndName(1, "Acme Corp")).thenReturn(customer);
        Customer result = customerService.findOneByEnabledTrueAndName("Acme Corp");
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
    }

    @Test
    void testFindOneByEnabledFalseAndName() {
        when(customerRepository.findOneByEnabledAndName(0, "Acme Corp")).thenReturn(null);
        Customer result = customerService.findOneByEnabledFalseAndName("Acme Corp");
        assertNull(result);
    }

    @Test
    void testFindOneByName() {
        when(customerRepository.findOneByName("Acme Corp")).thenReturn(customer);
        Customer result = customerService.findOneByName("Acme Corp");
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
    }

    @Test
    void testFindByEnabledTrueAndEmail() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(1, "acme@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("acme@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "acme@example.com");
    }

    @Test
    void testFindByEnabledFalseAndEmail() {
        when(customerRepository.findByEnabledAndEmail(0, "acme@example.com")).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("acme@example.com");
        assertNotNull(result);
    }

    @Test
    void testFindByEmail() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEmail("acme@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEmail("acme@example.com");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndPhone() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(1, 123456789)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void testFindByEnabledFalseAndPhone() {
        when(customerRepository.findByEnabledAndPhone(0, 123456789)).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndPhone(123456789);
        assertNotNull(result);
    }

    @Test
    void testFindByPhone() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByPhone(123456789)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByPhone(123456789);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCategories() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCategories() {
        when(customerRepository.findByEnabledAndCategories(0, categories)).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndCategories(categories);
        assertNotNull(result);
    }

    @Test
    void testFindByCategories() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCategories(categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCategories(categories);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndFirstName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndFirstName() {
        when(customerRepository.findByEnabledAndFirstName(0, "John")).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstName("John");
        assertNotNull(result);
    }

    @Test
    void testFindByFirstName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstName("John");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndLastName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndLastName() {
        when(customerRepository.findByEnabledAndLastName(0, "Doe")).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndLastName("Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByLastName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByLastName("Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndFirstNameAndLastName() {
        when(customerRepository.findByEnabledAndFirstNameAndLastName(0, "John", "Doe")).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByFirstNameAndLastName() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCity() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCity() {
        when(customerRepository.findByEnabledAndCity(0, "New York")).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndCity("New York");
        assertNotNull(result);
    }

    @Test
    void testFindByCity() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity("New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCity("New York");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCityAndAddress() {
        when(customerRepository.findByEnabledAndCityAndAddress(0, "New York", "123 Main St")).thenReturn(Collections.emptyList());
        Iterable<Customer> result = customerService.findByEnabledFalseAndCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
    }

    @Test
    void testFindByCityAndAddress() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
    }

    @Test
    void testSaveCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setName("New Corp");
        newCustomer.setEmail("new@example.com");
        customerService.saveCustomer(newCustomer);
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void testSaveCustomerSetsEnabledToOne() {
        Customer c = new Customer();
        c.setEnabled(0);
        customerService.saveCustomer(c);
        assertEquals(1, c.getEnabled());
    }
}

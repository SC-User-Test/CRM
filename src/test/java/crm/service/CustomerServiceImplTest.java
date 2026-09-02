package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
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
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("VIP");

        customer = Customer.builder()
                .id(1L)
                .name("TestCustomer")
                .email("test@example.com")
                .phone(555123456)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository);
        assertNotNull(service);
    }

    @Test
    void testGetMaxId_returnsMaxId() {
        when(customerRepository.getMaxId()).thenReturn(5L);
        Long result = customerService.getMaxId();
        assertEquals(5L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void testListAllCustomers_returnsAllCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        Iterable<Customer> result = customerService.listAllCustomers();
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void testShowCustomer_existingId_returnsCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer result = customerService.showCustomer(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository).findById(1L);
    }

    @Test
    void testShowCustomer_nonExistingId_returnsNull() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        Customer result = customerService.showCustomer(99L);
        assertNull(result);
    }

    @Test
    void testFindAllByEnabledTrue_returnsEnabledCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByEnabledTrue();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse_returnsDisabledCustomers() {
        Customer disabledCustomer = Customer.builder().id(2L).name("Disabled").enabled(0).build();
        List<Customer> customers = Arrays.asList(disabledCustomer);
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByEnabledFalse();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName_returnsCustomer() {
        when(customerRepository.findOneByEnabledAndName(1, "TestCustomer")).thenReturn(customer);
        Customer result = customerService.findOneByEnabledTrueAndName("TestCustomer");
        assertNotNull(result);
        assertEquals("TestCustomer", result.getName());
        verify(customerRepository).findOneByEnabledAndName(1, "TestCustomer");
    }

    @Test
    void testFindOneByEnabledFalseAndName_returnsCustomer() {
        Customer disabledCustomer = Customer.builder().id(2L).name("Disabled").enabled(0).build();
        when(customerRepository.findOneByEnabledAndName(0, "Disabled")).thenReturn(disabledCustomer);
        Customer result = customerService.findOneByEnabledFalseAndName("Disabled");
        assertNotNull(result);
        verify(customerRepository).findOneByEnabledAndName(0, "Disabled");
    }

    @Test
    void testFindOneByName_returnsCustomer() {
        when(customerRepository.findOneByName("TestCustomer")).thenReturn(customer);
        Customer result = customerService.findOneByName("TestCustomer");
        assertNotNull(result);
        verify(customerRepository).findOneByName("TestCustomer");
    }

    @Test
    void testFindByEnabledTrueAndEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void testFindByEnabledFalseAndEmail_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndEmail(0, "test@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("test@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(0, "test@example.com");
    }

    @Test
    void testFindByEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEmail("test@example.com");
        assertNotNull(result);
        verify(customerRepository).findByEmail("test@example.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(1, 555123456)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(555123456);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 555123456);
    }

    @Test
    void testFindByEnabledFalseAndPhone_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndPhone(0, 555123456)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndPhone(555123456);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(0, 555123456);
    }

    @Test
    void testFindByPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByPhone(555123456)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByPhone(555123456);
        assertNotNull(result);
        verify(customerRepository).findByPhone(555123456);
    }

    @Test
    void testFindByEnabledTrueAndCategories_returnsCustomers() {
        Set<Category> categories = new HashSet<>(Arrays.asList(category));
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(1, categories);
    }

    @Test
    void testFindByEnabledFalseAndCategories_returnsCustomers() {
        Set<Category> categories = new HashSet<>(Arrays.asList(category));
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndCategories(0, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCategories(categories);
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(0, categories);
    }

    @Test
    void testFindByCategories_returnsCustomers() {
        Set<Category> categories = new HashSet<>(Arrays.asList(category));
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCategories(categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCategories(categories);
        assertNotNull(result);
        verify(customerRepository).findByCategories(categories);
    }

    @Test
    void testFindByEnabledTrueAndFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void testFindByEnabledFalseAndFirstName_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndFirstName(0, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstName("John");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(0, "John");
    }

    @Test
    void testFindByFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstName("John");
        assertNotNull(result);
        verify(customerRepository).findByFirstName("John");
    }

    @Test
    void testFindByEnabledTrueAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(1, "Doe");
    }

    @Test
    void testFindByEnabledFalseAndLastName_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndLastName(0, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndLastName("Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(0, "Doe");
    }

    @Test
    void testFindByLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByLastName("Doe");
        assertNotNull(result);
        verify(customerRepository).findByLastName("Doe");
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstNameAndLastName(1, "John", "Doe");
    }

    @Test
    void testFindByEnabledFalseAndFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndFirstNameAndLastName(0, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstNameAndLastName(0, "John", "Doe");
    }

    @Test
    void testFindByFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
        verify(customerRepository).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testFindByEnabledTrueAndCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "New York");
    }

    @Test
    void testFindByEnabledFalseAndCity_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndCity(0, "New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCity("New York");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(0, "New York");
    }

    @Test
    void testFindByCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity("New York")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCity("New York");
        assertNotNull(result);
        verify(customerRepository).findByCity("New York");
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCityAndAddress(1, "New York", "123 Main St");
    }

    @Test
    void testFindByEnabledFalseAndCityAndAddress_returnsCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findByEnabledAndCityAndAddress(0, "New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCityAndAddress(0, "New York", "123 Main St");
    }

    @Test
    void testFindByCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");
        assertNotNull(result);
        verify(customerRepository).findByCityAndAddress("New York", "123 Main St");
    }

    @Test
    void testSaveCustomer_setsEnabledToOneAndSaves() {
        Customer newCustomer = Customer.builder().name("NewCustomer").email("new@example.com").build();
        customerService.saveCustomer(newCustomer);
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void testSaveCustomer_withExistingCustomer_updatesAndSaves() {
        customer.setEnabled(0);
        customerService.saveCustomer(customer);
        assertEquals(1, customer.getEnabled());
        verify(customerRepository).save(customer);
    }
}

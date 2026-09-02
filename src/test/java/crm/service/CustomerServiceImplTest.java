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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        category.setName("Tech");

        customer = Customer.builder()
                .id(1L)
                .name("TestCo")
                .email("test@test.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("NYC")
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
        Customer disabledCustomer = Customer.builder().id(2L).enabled(0).build();
        List<Customer> customers = Arrays.asList(disabledCustomer);
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);
        Iterable<Customer> result = customerService.findAllByEnabledFalse();
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName_returnsCustomer() {
        when(customerRepository.findOneByEnabledAndName(1, "TestCo")).thenReturn(customer);
        Customer result = customerService.findOneByEnabledTrueAndName("TestCo");
        assertNotNull(result);
        assertEquals("TestCo", result.getName());
    }

    @Test
    void testFindOneByEnabledFalseAndName_returnsCustomer() {
        Customer disabledCustomer = Customer.builder().id(2L).name("OldCo").enabled(0).build();
        when(customerRepository.findOneByEnabledAndName(0, "OldCo")).thenReturn(disabledCustomer);
        Customer result = customerService.findOneByEnabledFalseAndName("OldCo");
        assertNotNull(result);
        assertEquals("OldCo", result.getName());
    }

    @Test
    void testFindOneByName_returnsCustomer() {
        when(customerRepository.findOneByName("TestCo")).thenReturn(customer);
        Customer result = customerService.findOneByName("TestCo");
        assertNotNull(result);
        assertEquals("TestCo", result.getName());
    }

    @Test
    void testFindByEnabledTrueAndEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(1, "test@test.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@test.com");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(0, "test@test.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("test@test.com");
        assertNotNull(result);
    }

    @Test
    void testFindByEmail_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEmail("test@test.com")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEmail("test@test.com");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(1, 123456789)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(0, 123456789)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndPhone(123456789);
        assertNotNull(result);
    }

    @Test
    void testFindByPhone_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByPhone(123456789)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByPhone(123456789);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCategories_returnsCustomers() {
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCategories_returnsCustomers() {
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(0, categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCategories(categories);
        assertNotNull(result);
    }

    @Test
    void testFindByCategories_returnsCustomers() {
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCategories(categories)).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCategories(categories);
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(0, "John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstName("John");
        assertNotNull(result);
    }

    @Test
    void testFindByFirstName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstName("John");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(0, "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndLastName("Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByLastName("Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(0, "John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByFirstNameAndLastName_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(1, "NYC")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("NYC");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(0, "NYC")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCity("NYC");
        assertNotNull(result);
    }

    @Test
    void testFindByCity_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity("NYC")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCity("NYC");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(1, "NYC", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("NYC", "123 Main St");
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(0, "NYC", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByEnabledFalseAndCityAndAddress("NYC", "123 Main St");
        assertNotNull(result);
    }

    @Test
    void testFindByCityAndAddress_returnsCustomers() {
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCityAndAddress("NYC", "123 Main St")).thenReturn(customers);
        Iterable<Customer> result = customerService.findByCityAndAddress("NYC", "123 Main St");
        assertNotNull(result);
    }

    @Test
    void testSaveCustomer_setsEnabledToOneAndSaves() {
        Customer newCustomer = Customer.builder().name("NewCo").email("new@test.com").build();
        customerService.saveCustomer(newCustomer);
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void testSaveCustomer_withExistingCustomer_savesSuccessfully() {
        customerService.saveCustomer(customer);
        verify(customerRepository).save(customer);
    }
}

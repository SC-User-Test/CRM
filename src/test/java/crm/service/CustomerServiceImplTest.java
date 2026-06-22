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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer testCustomer;
    private Set<Category> testCategories;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("test@example.com");
        testCustomer.setEnabled(1);

        testCategories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        testCategories.add(category);
    }

    @Test
    void getMaxId_shouldReturnMaxId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(10L);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertEquals(10L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void listAllCustomers_shouldReturnAllCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findAll()).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.listAllCustomers();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void showCustomer_shouldReturnCustomerById() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Act
        Customer result = customerService.showCustomer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository).findById(1L);
    }

    @Test
    void showCustomer_withNonExistentId_shouldReturnNull() {
        // Arrange
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Customer result = customerService.showCustomer(999L);

        // Assert
        assertNull(result);
        verify(customerRepository).findById(999L);
    }

    @Test
    void findAllByEnabledTrue_shouldReturnEnabledCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void findAllByEnabledFalse_shouldReturnDisabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(0)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void findOneByEnabledTrueAndName_shouldReturnCustomer() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(1, "Test Customer")).thenReturn(testCustomer);

        // Act
        Customer result = customerService.findOneByEnabledTrueAndName("Test Customer");

        // Assert
        assertNotNull(result);
        assertEquals("Test Customer", result.getName());
        verify(customerRepository).findOneByEnabledAndName(1, "Test Customer");
    }

    @Test
    void findOneByEnabledFalseAndName_shouldReturnCustomer() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(0, "Disabled Customer")).thenReturn(testCustomer);

        // Act
        Customer result = customerService.findOneByEnabledFalseAndName("Disabled Customer");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findOneByEnabledAndName(0, "Disabled Customer");
    }

    @Test
    void findOneByName_shouldReturnCustomer() {
        // Arrange
        when(customerRepository.findOneByName("Test Customer")).thenReturn(testCustomer);

        // Act
        Customer result = customerService.findOneByName("Test Customer");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findOneByName("Test Customer");
    }

    @Test
    void findByEnabledTrueAndEmail_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void findByEmail_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByEmail("test@example.com")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByEmail("test@example.com");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEmail("test@example.com");
    }

    @Test
    void findByPhone_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByPhone(123456789)).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByPhone(123456789);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByPhone(123456789);
    }

    @Test
    void findByCategories_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByCategories(testCategories)).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByCategories(testCategories);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByCategories(testCategories);
    }

    @Test
    void findByFirstName_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByFirstName("John");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByFirstName("John");
    }

    @Test
    void findByLastName_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByLastName("Doe");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByLastName("Doe");
    }

    @Test
    void findByFirstNameAndLastName_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void findByCity_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByCity("New York")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByCity("New York");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByCity("New York");
    }

    @Test
    void findByCityAndAddress_shouldReturnCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);

        // Act
        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByCityAndAddress("New York", "123 Main St");
    }

    @Test
    void saveCustomer_shouldSetEnabledAndSave() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        customerService.saveCustomer(testCustomer);

        // Assert
        assertEquals(1, testCustomer.getEnabled());
        verify(customerRepository).save(testCustomer);
    }

    @Test
    void constructor_shouldInitializeRepository() {
        // Arrange
        CustomerRepository repository = mock(CustomerRepository.class);

        // Act
        CustomerServiceImpl service = new CustomerServiceImpl(repository);

        // Assert
        assertNotNull(service);
    }
}

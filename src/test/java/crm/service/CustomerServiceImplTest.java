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
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Technology");

        categories = new HashSet<>();
        categories.add(category);

        customer = Customer.builder()
                .id(1L)
                .name("Acme Corp")
                .email("acme@example.com")
                .phone(123456789)
                .categories(categories)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository);
        // Assert
        assertNotNull(service);
    }

    @Test
    void testGetMaxId_returnsMaxId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(5L);
        // Act
        Long result = customerService.getMaxId();
        // Assert
        assertEquals(5L, result);
        verify(customerRepository, times(1)).getMaxId();
    }

    @Test
    void testListAllCustomers_returnsAllCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAll()).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.listAllCustomers();
        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void testShowCustomer_existingId_returnsCustomer() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        // Act
        Customer result = customerService.showCustomer(1L);
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testShowCustomer_nonExistingId_returnsNull() {
        // Arrange
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        // Act
        Customer result = customerService.showCustomer(99L);
        // Assert
        assertNull(result);
    }

    @Test
    void testFindAllByEnabledTrue_returnsEnabledCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findAllByEnabled(1)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();
        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse_returnsDisabledCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findAllByEnabled(0)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();
        // Assert
        assertNotNull(result);
        verify(customerRepository, times(1)).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName_returnsCustomer() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(1, "Acme Corp")).thenReturn(customer);
        // Act
        Customer result = customerService.findOneByEnabledTrueAndName("Acme Corp");
        // Assert
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
    }

    @Test
    void testFindOneByEnabledFalseAndName_returnsNull() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(0, "Acme Corp")).thenReturn(null);
        // Act
        Customer result = customerService.findOneByEnabledFalseAndName("Acme Corp");
        // Assert
        assertNull(result);
    }

    @Test
    void testFindOneByName_returnsCustomer() {
        // Arrange
        when(customerRepository.findOneByName("Acme Corp")).thenReturn(customer);
        // Act
        Customer result = customerService.findOneByName("Acme Corp");
        // Assert
        assertNotNull(result);
        assertEquals("Acme Corp", result.getName());
    }

    @Test
    void testFindByEnabledTrueAndEmail_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndEmail(1, "acme@example.com")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("acme@example.com");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndEmail_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndEmail(0, "acme@example.com")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndEmail("acme@example.com");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEmail_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEmail("acme@example.com")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEmail("acme@example.com");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndPhone_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndPhone(1, 123456789)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndPhone_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndPhone(0, 123456789)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndPhone(123456789);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByPhone_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByPhone(123456789)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByPhone(123456789);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCategories_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCategories(1, categories)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCategories_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndCategories(0, categories)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndCategories(categories);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByCategories_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCategories(categories)).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByCategories(categories);
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndFirstName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstName(1, "John")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndFirstName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndFirstName(0, "John")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstName("John");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByFirstName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstName("John")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByFirstName("John");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndLastName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndLastName(1, "Doe")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndLastName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndLastName(0, "Doe")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndLastName("Doe");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByLastName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByLastName("Doe")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByLastName("Doe");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndFirstNameAndLastName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndFirstNameAndLastName(0, "John", "Doe")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndFirstNameAndLastName("John", "Doe");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByFirstNameAndLastName_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByFirstNameAndLastName("John", "Doe");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCity_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCity(1, "New York")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCity_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndCity(0, "New York")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndCity("New York");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByCity_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCity("New York")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByCity("New York");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByEnabledAndCityAndAddress(1, "New York", "123 Main St")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByEnabledFalseAndCityAndAddress_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList();
        when(customerRepository.findByEnabledAndCityAndAddress(0, "New York", "123 Main St")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByEnabledFalseAndCityAndAddress("New York", "123 Main St");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testFindByCityAndAddress_returnsCustomers() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerRepository.findByCityAndAddress("New York", "123 Main St")).thenReturn(customers);
        // Act
        Iterable<Customer> result = customerService.findByCityAndAddress("New York", "123 Main St");
        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveCustomer_setsEnabledAndSaves() {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setName("New Corp");
        when(customerRepository.save(any(Customer.class))).thenReturn(newCustomer);
        // Act
        customerService.saveCustomer(newCustomer);
        // Assert
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository, times(1)).save(newCustomer);
    }
}

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
import java.util.Collections;
import java.util.HashSet;
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
        category.setName("Premium");

        categories = new HashSet<>();
        categories.add(category);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Company");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);
        customer.setCategories(categories);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("Test City");
        customer.setAddress("123 Test St");
        customer.setEnabled(1);
    }

    @Test
    void testConstructor_ShouldInitializeWithRepository() {
        // Arrange & Act
        CustomerServiceImpl service = new CustomerServiceImpl(customerRepository);

        // Assert
        assertNotNull(service);
    }

    @Test
    void testGetMaxId_ShouldReturnMaxId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(10L);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertEquals(10L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void testListAllCustomers_ShouldReturnAllCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        // Act
        Iterable<Customer> result = customerService.listAllCustomers();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void testShowCustomer_ShouldReturnCustomerById() {
        // Arrange
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        Customer result = customerService.showCustomer(1L);

        // Assert
        assertEquals(customer, result);
        verify(customerRepository).findById(1L);
    }

    @Test
    void testFindAllByEnabledTrue_ShouldReturnEnabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(1)).thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void testFindAllByEnabledFalse_ShouldReturnDisabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(0)).thenReturn(Collections.emptyList());

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledFalse();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(0);
    }

    @Test
    void testFindOneByEnabledTrueAndName_ShouldReturnCustomer() {
        // Arrange
        when(customerRepository.findOneByEnabledAndName(1, "Test Company")).thenReturn(customer);

        // Act
        Customer result = customerService.findOneByEnabledTrueAndName("Test Company");

        // Assert
        assertEquals(customer, result);
        verify(customerRepository).findOneByEnabledAndName(1, "Test Company");
    }

    @Test
    void testFindByEnabledTrueAndEmail_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void testFindByEnabledTrueAndPhone_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndPhone(1, 123456789))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void testFindByEnabledTrueAndFirstName_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndFirstName(1, "John"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void testFindByEnabledTrueAndLastName_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndLastName(1, "Doe"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(1, "Doe");
    }

    @Test
    void testFindByEnabledTrueAndFirstNameAndLastName_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndFirstNameAndLastName(1, "John", "Doe"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstNameAndLastName(1, "John", "Doe");
    }

    @Test
    void testFindByEnabledTrueAndCity_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCity(1, "Test City"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("Test City");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "Test City");
    }

    @Test
    void testFindByEnabledTrueAndCityAndAddress_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCityAndAddress(1, "Test City", "123 Test St"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCityAndAddress("Test City", "123 Test St");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCityAndAddress(1, "Test City", "123 Test St");
    }

    @Test
    void testSaveCustomer_ShouldSetEnabledAndSave() {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setName("New Company");

        // Act
        customerService.saveCustomer(newCustomer);

        // Assert
        assertEquals(1, newCustomer.getEnabled());
        verify(customerRepository).save(newCustomer);
    }

    @Test
    void testFindByEnabledTrueAndCategories_ShouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCategories(1, categories))
                .thenReturn(Collections.singletonList(customer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(1, categories);
    }
}

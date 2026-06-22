package crm.service;

import crm.entity.Customer;
import crm.entity.Category;
import crm.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private Set<Category> categories;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("VIP");
        
        categories = new HashSet<>();
        categories.add(category);

        testCustomer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .categories(categories)
                .build();
    }

    @Test
    void getMaxId_shouldReturnMaxId() {
        // Arrange
        when(customerRepository.getMaxId()).thenReturn(100L);

        // Act
        Long result = customerService.getMaxId();

        // Assert
        assertEquals(100L, result);
        verify(customerRepository).getMaxId();
    }

    @Test
    void listAllCustomers_shouldReturnAllCustomers() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.listAllCustomers();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAll();
    }

    @Test
    void showCustomer_withValidId_shouldReturnCustomer() {
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
    void showCustomer_withInvalidId_shouldReturnNull() {
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
        when(customerRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findAllByEnabledTrue();

        // Assert
        assertNotNull(result);
        verify(customerRepository).findAllByEnabled(1);
    }

    @Test
    void findAllByEnabledFalse_shouldReturnDisabledCustomers() {
        // Arrange
        when(customerRepository.findAllByEnabled(0)).thenReturn(Arrays.asList());

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
        when(customerRepository.findByEnabledAndEmail(1, "test@example.com"))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndEmail("test@example.com");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndEmail(1, "test@example.com");
    }

    @Test
    void findByEnabledTrueAndPhone_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndPhone(1, 123456789))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndPhone(123456789);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndPhone(1, 123456789);
    }

    @Test
    void findByEnabledTrueAndFirstName_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndFirstName(1, "John"))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndFirstName("John");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndFirstName(1, "John");
    }

    @Test
    void findByEnabledTrueAndLastName_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndLastName(1, "Doe"))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndLastName("Doe");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndLastName(1, "Doe");
    }

    @Test
    void findByEnabledTrueAndCity_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCity(1, "New York"))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCity("New York");

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCity(1, "New York");
    }

    @Test
    void saveCustomer_shouldSetEnabledAndSave() {
        // Arrange
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // Act
        customerService.saveCustomer(testCustomer);

        // Assert
        verify(customerRepository).save(testCustomer);
        assertEquals(1, testCustomer.getEnabled());
    }

    @Test
    void findByEnabledTrueAndCategories_shouldReturnCustomers() {
        // Arrange
        when(customerRepository.findByEnabledAndCategories(1, categories))
                .thenReturn(Arrays.asList(testCustomer));

        // Act
        Iterable<Customer> result = customerService.findByEnabledTrueAndCategories(categories);

        // Assert
        assertNotNull(result);
        verify(customerRepository).findByEnabledAndCategories(1, categories);
    }
}

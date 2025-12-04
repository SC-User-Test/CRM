package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Company");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("Test City");
        customer.setAddress("Test Address");
        customer.setEnabled(1);
    }

    @Test
    void testConstructor_ShouldInitializeWithCustomerService() {
        // Arrange & Act
        CustomerController controller = new CustomerController(customerService);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowAllCustomers_ShouldReturnListView() {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(customer));

        // Act
        String result = customerController.showAllCustomers(model);

        // Assert
        assertEquals("customer/list", result);
    }

    @Test
    void testShowAllCustomers_ShouldAddCustomersToModel() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.listAllCustomers()).thenReturn(customers);

        // Act
        customerController.showAllCustomers(model);

        // Assert
        verify(model).addAttribute("customers", customers);
    }

    @Test
    void testShowFormAddCustomer_ShouldReturnAddView() {
        // Arrange & Act
        String result = customerController.showFormAddCustomer(model);

        // Assert
        assertEquals("customer/add", result);
    }

    @Test
    void testShowFormAddCustomer_ShouldAddNewCustomerToModel() {
        // Arrange & Act
        customerController.showFormAddCustomer(model);

        // Assert
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomer_WithValidationErrors_ShouldRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = customerController.processRequestAddCustomer(customer, bindingResult);

        // Assert
        assertEquals("redirect:/customer/add", result);
    }

    @Test
    void testProcessRequestAddCustomer_WithoutErrors_ShouldSaveCustomer() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        customerController.processRequestAddCustomer(customer, bindingResult);

        // Assert
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestAddCustomer_WithoutErrors_ShouldReturnSuccessView() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = customerController.processRequestAddCustomer(customer, bindingResult);

        // Assert
        assertEquals("customer/success", result);
    }

    @Test
    void testShowFormEditCustomer_ShouldReturnEditView() {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act
        String result = customerController.showFormEditCustomer(model, 1L);

        // Assert
        assertEquals("customer/edit", result);
    }

    @Test
    void testProcessRequestEditCustomer_WithValidationErrors_ShouldRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        // Assert
        assertEquals("redirect:/customer/edit/1", result);
    }

    @Test
    void testProcessRequestEditCustomer_WithoutErrors_ShouldRedirectToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        // Assert
        assertEquals("redirect:/customer/list", result);
    }

    @Test
    void testShowFormCreateCustomerBasedOnAnotherOne_ShouldReturnView() {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act
        String result = customerController.showFormCreateCustomerBasedOnAnotherOne(model, 1L);

        // Assert
        assertEquals("customer/add-customer-based-on-another-one", result);
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne_WithValidationErrors_ShouldRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);

        // Assert
        assertEquals("redirect:/customer/addCustomerBasedOnAnotherOne/1", result);
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne_WithoutErrors_ShouldSaveNewCustomer() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.getMaxId()).thenReturn(5L);

        // Act
        customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);

        // Assert
        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    void testShowNameSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showNameSearchForm(model);

        // Assert
        assertEquals("customer/name-search", result);
    }

    @Test
    void testProcessRequestNameSearch_ShouldFindByName() {
        // Arrange
        when(customerService.findOneByEnabledTrueAndName("Test Company")).thenReturn(customer);

        // Act
        String result = customerController.processRequestNameSearch(customer, model);

        // Assert
        assertEquals("customer/show-one", result);
        verify(customerService).findOneByEnabledTrueAndName("Test Company");
    }

    @Test
    void testShowEmailSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showEmailSearchForm(model);

        // Assert
        assertEquals("customer/email-search", result);
    }

    @Test
    void testProcessRequestEmailSearch_ShouldFindByEmail() {
        // Arrange
        when(customerService.findByEnabledTrueAndEmail("test@example.com"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestEmailSearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
        verify(customerService).findByEnabledTrueAndEmail("test@example.com");
    }

    @Test
    void testShowPhoneSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showPhoneSearchForm(model);

        // Assert
        assertEquals("customer/phone-search", result);
    }

    @Test
    void testProcessRequestPhoneSearch_ShouldFindByPhone() {
        // Arrange
        when(customerService.findByEnabledTrueAndPhone(123456789))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestPhoneSearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
    }

    @Test
    void testShowFirstNameSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showFirstNameSearchForm(model);

        // Assert
        assertEquals("customer/first-name-search", result);
    }

    @Test
    void testProcessRequestFirstNameSearch_ShouldFindByFirstName() {
        // Arrange
        when(customerService.findByEnabledTrueAndFirstName("John"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestFirstNameSearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
    }

    @Test
    void testShowLastNameSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showLastNameSearchForm(model);

        // Assert
        assertEquals("customer/last-name-search", result);
    }

    @Test
    void testProcessRequestLastNameSearch_ShouldFindByLastName() {
        // Arrange
        when(customerService.findByEnabledTrueAndLastName("Doe"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestLastNameSearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
    }

    @Test
    void testShowFirstNameLastNameSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showFirstNameLastNameSearchForm(model);

        // Assert
        assertEquals("customer/first-name-last-name-search", result);
    }

    @Test
    void testProcessRequestFirstNameLastNameSearch_ShouldFindByBothNames() {
        // Arrange
        when(customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestFirstNameLastNameSearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
    }

    @Test
    void testShowCitySearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showCitySearchForm(model);

        // Assert
        assertEquals("customer/city-search", result);
    }

    @Test
    void testProcessRequestCitySearch_ShouldFindByCity() {
        // Arrange
        when(customerService.findByEnabledTrueAndCity("Test City"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestCitySearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
    }

    @Test
    void testShowCityAddressSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = customerController.showCityAddressSearchForm(model);

        // Assert
        assertEquals("customer/city-address-search", result);
    }

    @Test
    void testProcessRequestCityAddressSearch_ShouldFindByCityAndAddress() {
        // Arrange
        when(customerService.findByEnabledTrueAndCityAndAddress("Test City", "Test Address"))
                .thenReturn(Collections.singletonList(customer));

        // Act
        String result = customerController.processRequestCityAddressSearch(customer, model);

        // Assert
        assertEquals("customer/show-list", result);
    }
}

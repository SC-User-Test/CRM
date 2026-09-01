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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Acme Corp")
                .email("acme@example.com")
                .phone(1234567890)
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
        CustomerController controller = new CustomerController(customerService);
        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowAllCustomers_returnsCustomerListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.listAllCustomers()).thenReturn(customers);
        // Act
        String viewName = customerController.showAllCustomers(model);
        // Assert
        assertEquals("customer/list", viewName);
        verify(model).addAttribute(eq("customers"), any());
    }

    @Test
    void testShowFormAddCustomer_returnsAddView() {
        // Arrange & Act
        String viewName = customerController.showFormAddCustomer(model);
        // Assert
        assertEquals("customer/add", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomer_withNoErrors_returnsSuccessView() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        // Act
        String viewName = customerController.processRequestAddCustomer(customer, bindingResult);
        // Assert
        assertEquals("customer/success", viewName);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestAddCustomer_withErrors_redirectsToAdd() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = customerController.processRequestAddCustomer(customer, bindingResult);
        // Assert
        assertEquals("redirect:/customer/add", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowFormEditCustomer_returnsEditView() {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);
        // Act
        String viewName = customerController.showFormEditCustomer(model, 1L);
        // Assert
        assertEquals("customer/edit", viewName);
        verify(model).addAttribute(eq("customer"), eq(customer));
    }

    @Test
    void testProcessRequestEditCustomer_withNoErrors_redirectsToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        // Act
        String viewName = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        // Assert
        assertEquals("redirect:/customer/list", viewName);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestEditCustomer_withErrors_redirectsToEdit() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        // Assert
        assertEquals("redirect:/customer/edit/1", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowFormCreateCustomerBasedOnAnotherOne_returnsView() {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);
        // Act
        String viewName = customerController.showFormCreateCustomerBasedOnAnotherOne(model, 1L);
        // Assert
        assertEquals("customer/add-customer-based-on-another-one", viewName);
        verify(model).addAttribute(eq("customer"), eq(customer));
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne_withNoErrors_redirectsToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.getMaxId()).thenReturn(5L);
        // Act
        String viewName = customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);
        // Assert
        assertEquals("redirect:/customer/list", viewName);
        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne_withErrors_redirectsToForm() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);
        // Assert
        assertEquals("redirect:/customer/addCustomerBasedOnAnotherOne/1", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowNameSearchForm_returnsNameSearchView() {
        // Arrange & Act
        String viewName = customerController.showNameSearchForm(model);
        // Assert
        assertEquals("customer/name-search", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestNameSearch_returnsShowOneView() {
        // Arrange
        when(customerService.findOneByEnabledTrueAndName("Acme Corp")).thenReturn(customer);
        // Act
        String viewName = customerController.processRequestNameSearch(customer, model);
        // Assert
        assertEquals("customer/show-one", viewName);
        verify(model).addAttribute(eq("customer"), eq(customer));
    }

    @Test
    void testShowEmailSearchForm_returnsEmailSearchView() {
        // Arrange & Act
        String viewName = customerController.showEmailSearchForm(model);
        // Assert
        assertEquals("customer/email-search", viewName);
    }

    @Test
    void testProcessRequestEmailSearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndEmail("acme@example.com")).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestEmailSearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
        verify(model).addAttribute(eq("customers"), any());
    }

    @Test
    void testShowPhoneSearchForm_returnsPhoneSearchView() {
        // Arrange & Act
        String viewName = customerController.showPhoneSearchForm(model);
        // Assert
        assertEquals("customer/phone-search", viewName);
    }

    @Test
    void testProcessRequestPhoneSearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndPhone(1234567890)).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestPhoneSearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
    }

    @Test
    void testShowFirstNameSearchForm_returnsFirstNameSearchView() {
        // Arrange & Act
        String viewName = customerController.showFirstNameSearchForm(model);
        // Assert
        assertEquals("customer/first-name-search", viewName);
    }

    @Test
    void testProcessRequestFirstNameSearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndFirstName("John")).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestFirstNameSearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
    }

    @Test
    void testShowLastNameSearchForm_returnsLastNameSearchView() {
        // Arrange & Act
        String viewName = customerController.showLastNameSearchForm(model);
        // Assert
        assertEquals("customer/last-name-search", viewName);
    }

    @Test
    void testProcessRequestLastNameSearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndLastName("Doe")).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestLastNameSearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
    }

    @Test
    void testShowFirstNameLastNameSearchForm_returnsView() {
        // Arrange & Act
        String viewName = customerController.showFirstNameLastNameSearchForm(model);
        // Assert
        assertEquals("customer/first-name-last-name-search", viewName);
    }

    @Test
    void testProcessRequestFirstNameLastNameSearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe")).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestFirstNameLastNameSearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
    }

    @Test
    void testShowCitySearchForm_returnsCitySearchView() {
        // Arrange & Act
        String viewName = customerController.showCitySearchForm(model);
        // Assert
        assertEquals("customer/city-search", viewName);
    }

    @Test
    void testProcessRequestCitySearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndCity("New York")).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestCitySearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
    }

    @Test
    void testShowCityAddressSearchForm_returnsCityAddressSearchView() {
        // Arrange & Act
        String viewName = customerController.showCityAddressSearchForm(model);
        // Assert
        assertEquals("customer/city-address-search", viewName);
    }

    @Test
    void testProcessRequestCityAddressSearch_returnsShowListView() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St")).thenReturn(customers);
        // Act
        String viewName = customerController.processRequestCityAddressSearch(customer, model);
        // Assert
        assertEquals("customer/show-list", viewName);
    }
}

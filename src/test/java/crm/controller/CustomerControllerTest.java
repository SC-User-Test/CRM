package crm.controller;

import crm.entity.Category;
import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCustomerControllerConstructor() {
        CustomerController controller = new CustomerController(customerService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllCustomers() {
        Customer customer1 = Customer.builder().id(1L).name("Customer1").build();
        Customer customer2 = Customer.builder().id(2L).name("Customer2").build();

        when(customerService.listAllCustomers()).thenReturn(Arrays.asList(customer1, customer2));

        String viewName = customerController.showAllCustomers(model);

        assertEquals("customer/list", viewName);
        verify(model).addAttribute(eq("customers"), any());
        verify(customerService).listAllCustomers();
    }

    @Test
    void testShowFormAddCustomer() {
        String viewName = customerController.showFormAddCustomer(model);

        assertEquals("customer/add", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomerWithValidData() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("New Customer")
                .email("new@example.com")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = customerController.processRequestAddCustomer(customer, bindingResult);

        assertEquals("customer/success", viewName);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestAddCustomerWithErrors() {
        Customer customer = Customer.builder().build();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = customerController.processRequestAddCustomer(customer, bindingResult);

        assertEquals("redirect:/customer/add", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowFormEditCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Edit Customer")
                .build();

        when(customerService.showCustomer(1L)).thenReturn(customer);

        String viewName = customerController.showFormEditCustomer(model, 1L);

        assertEquals("customer/edit", viewName);
        verify(model).addAttribute("customer", customer);
        verify(customerService).showCustomer(1L);
    }

    @Test
    void testProcessRequestEditCustomerWithValidData() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Updated Customer")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        assertEquals("redirect:/customer/list", viewName);
        verify(customerService).saveCustomer(customer);
    }

    @Test
    void testProcessRequestEditCustomerWithErrors() {
        Customer customer = Customer.builder().build();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        assertEquals("redirect:/customer/edit/1", viewName);
        verify(customerService, never()).saveCustomer(any());
    }

    @Test
    void testShowFormCreateCustomerBasedOnAnotherOne() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Reference Customer")
                .build();

        when(customerService.showCustomer(1L)).thenReturn(customer);

        String viewName = customerController.showFormCreateCustomerBasedOnAnotherOne(model, 1L);

        assertEquals("customer/add-customer-based-on-another-one", viewName);
        verify(model).addAttribute("customer", customer);
    }

    @Test
    void testCreateCustomerBasedOnAnotherOneWithValidData() {
        Set<Category> categories = new HashSet<>();
        Customer customer = Customer.builder()
                .name("New Based Customer")
                .email("based@example.com")
                .phone(123456789)
                .categories(categories)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.getMaxId()).thenReturn(10L);

        String viewName = customerController.createCustomerBasedOnAnotherOne(1L, customer, bindingResult);

        assertEquals("redirect:/customer/list", viewName);
        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    void testShowNameSearchForm() {
        String viewName = customerController.showNameSearchForm(model);

        assertEquals("customer/name-search", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestNameSearch() {
        Customer searchCustomer = Customer.builder().name("Search Name").build();
        Customer foundCustomer = Customer.builder().id(1L).name("Search Name").build();

        when(customerService.findOneByEnabledTrueAndName("Search Name")).thenReturn(foundCustomer);

        String viewName = customerController.processRequestNameSearch(searchCustomer, model);

        assertEquals("customer/show-one", viewName);
        verify(model).addAttribute("customer", foundCustomer);
    }

    @Test
    void testShowEmailSearchForm() {
        String viewName = customerController.showEmailSearchForm(model);

        assertEquals("customer/email-search", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestEmailSearch() {
        Customer searchCustomer = Customer.builder().email("test@example.com").build();

        when(customerService.findByEnabledTrueAndEmail("test@example.com")).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestEmailSearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndEmail("test@example.com");
    }

    @Test
    void testShowPhoneSearchForm() {
        String viewName = customerController.showPhoneSearchForm(model);

        assertEquals("customer/phone-search", viewName);
        verify(model).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestPhoneSearch() {
        Customer searchCustomer = Customer.builder().phone(123456789).build();

        when(customerService.findByEnabledTrueAndPhone(123456789)).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestPhoneSearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndPhone(123456789);
    }

    @Test
    void testProcessRequestFirstNameSearch() {
        Customer searchCustomer = Customer.builder().firstName("John").build();

        when(customerService.findByEnabledTrueAndFirstName("John")).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestFirstNameSearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndFirstName("John");
    }

    @Test
    void testProcessRequestLastNameSearch() {
        Customer searchCustomer = Customer.builder().lastName("Doe").build();

        when(customerService.findByEnabledTrueAndLastName("Doe")).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestLastNameSearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndLastName("Doe");
    }

    @Test
    void testProcessRequestFirstNameLastNameSearch() {
        Customer searchCustomer = Customer.builder().firstName("John").lastName("Doe").build();

        when(customerService.findByEnabledTrueAndFirstNameAndLastName("John", "Doe")).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestFirstNameLastNameSearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testProcessRequestCitySearch() {
        Customer searchCustomer = Customer.builder().city("New York").build();

        when(customerService.findByEnabledTrueAndCity("New York")).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestCitySearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndCity("New York");
    }

    @Test
    void testProcessRequestCityAddressSearch() {
        Customer searchCustomer = Customer.builder().city("New York").address("123 Main St").build();

        when(customerService.findByEnabledTrueAndCityAndAddress("New York", "123 Main St")).thenReturn(Arrays.asList());

        String viewName = customerController.processRequestCityAddressSearch(searchCustomer, model);

        assertEquals("customer/show-list", viewName);
        verify(customerService).findByEnabledTrueAndCityAndAddress("New York", "123 Main St");
    }
}

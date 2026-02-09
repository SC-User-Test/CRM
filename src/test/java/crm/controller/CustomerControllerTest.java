package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private CustomerController customerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customerController = new CustomerController(customerService);
    }

    @Test
    public void testCustomerControllerCreation() {
        assertNotNull(customerController);
    }

    @Test
    public void testShowAllCustomers() {
        List<Customer> customers = Arrays.asList(
                Customer.builder().id(1L).name("Customer1").build(),
                Customer.builder().id(2L).name("Customer2").build()
        );
        when(customerService.listAllCustomers()).thenReturn(customers);

        String result = customerController.showAllCustomers(model);
        assertEquals("customer/list", result);
        verify(customerService, times(1)).listAllCustomers();
        verify(model, times(1)).addAttribute("customers", customers);
    }

    @Test
    public void testShowFormAddCustomer() {
        String result = customerController.showFormAddCustomer(model);
        assertEquals("customer/add", result);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    public void testProcessRequestAddCustomerSuccess() {
        Customer customer = Customer.builder()
                .name("New Customer")
                .email("new@example.com")
                .phone(123456789)
                .enabled(1)
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = customerController.processRequestAddCustomer(customer, bindingResult);
        assertEquals("customer/success", result);
        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    public void testProcessRequestAddCustomerValidationErrors() {
        Customer customer = Customer.builder().name("C").build();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = customerController.processRequestAddCustomer(customer, bindingResult);
        assertEquals("redirect:/customer/add", result);
        verify(customerService, never()).saveCustomer(customer);
    }

    @Test
    public void testShowFormEditCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .build();
        when(customerService.showCustomer(1L)).thenReturn(customer);

        String result = customerController.showFormEditCustomer(model, 1L);
        assertEquals("customer/edit", result);
        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    public void testProcessRequestEditCustomerSuccess() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Updated Customer")
                .email("updated@example.com")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        assertEquals("redirect:/customer/list", result);
        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    public void testProcessRequestEditCustomerValidationErrors() {
        Customer customer = Customer.builder().id(1L).build();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);
        assertEquals("redirect:/customer/edit/1", result);
    }

    @Test
    public void testShowNameSearchForm() {
        String result = customerController.showNameSearchForm(model);
        assertEquals("customer/name-search", result);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    public void testProcessRequestNameSearch() {
        Customer customer = Customer.builder().name("Search Name").build();
        Customer foundCustomer = Customer.builder()
                .id(1L)
                .name("Search Name")
                .enabled(1)
                .build();
        when(customerService.findOneByEnabledTrueAndName("Search Name")).thenReturn(foundCustomer);

        String result = customerController.processRequestNameSearch(customer, model);
        assertEquals("customer/show-one", result);
        verify(customerService, times(1)).findOneByEnabledTrueAndName("Search Name");
    }

    @Test
    public void testShowEmailSearchForm() {
        String result = customerController.showEmailSearchForm(model);
        assertEquals("customer/email-search", result);
    }

    @Test
    public void testProcessRequestEmailSearch() {
        Customer customer = Customer.builder().email("test@example.com").build();
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndEmail("test@example.com")).thenReturn(customers);

        String result = customerController.processRequestEmailSearch(customer, model);
        assertEquals("customer/show-list", result);
        verify(customerService, times(1)).findByEnabledTrueAndEmail("test@example.com");
    }

    @Test
    public void testShowPhoneSearchForm() {
        String result = customerController.showPhoneSearchForm(model);
        assertEquals("customer/phone-search", result);
    }

    @Test
    public void testProcessRequestPhoneSearch() {
        Customer customer = Customer.builder().phone(123456789).build();
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndPhone(123456789)).thenReturn(customers);

        String result = customerController.processRequestPhoneSearch(customer, model);
        assertEquals("customer/show-list", result);
        verify(customerService, times(1)).findByEnabledTrueAndPhone(123456789);
    }

    @Test
    public void testShowCitySearchForm() {
        String result = customerController.showCitySearchForm(model);
        assertEquals("customer/city-search", result);
    }

    @Test
    public void testProcessRequestCitySearch() {
        Customer customer = Customer.builder().city("New York").build();
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.findByEnabledTrueAndCity("New York")).thenReturn(customers);

        String result = customerController.processRequestCitySearch(customer, model);
        assertEquals("customer/show-list", result);
        verify(customerService, times(1)).findByEnabledTrueAndCity("New York");
    }
}

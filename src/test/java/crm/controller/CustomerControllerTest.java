package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerService.listAllCustomers()).thenReturn(customers);

        String result = customerController.showAllCustomers(model);

        assertEquals("customer/list", result);
        verify(model, times(1)).addAttribute("customers", customers);
    }

    @Test
    void testShowFormAddCustomer() {
        String result = customerController.showFormAddCustomer(model);

        assertEquals("customer/add", result);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestAddCustomerSuccess() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = customerController.processRequestAddCustomer(customer, bindingResult);

        assertEquals("customer/success", result);
        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    void testProcessRequestAddCustomerWithErrors() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = customerController.processRequestAddCustomer(customer, bindingResult);

        assertEquals("redirect:/customer/add", result);
    }

    @Test
    void testShowFormEditCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        when(customerService.showCustomer(1L)).thenReturn(customer);

        String result = customerController.showFormEditCustomer(model, 1L);

        assertEquals("customer/edit", result);
        verify(model, times(1)).addAttribute("customer", customer);
    }

    @Test
    void testProcessRequestEditCustomerSuccess() {
        Customer customer = new Customer();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = customerController.processRequestEditCustomer(1L, customer, bindingResult);

        assertEquals("redirect:/customer/list", result);
        verify(customerService, times(1)).saveCustomer(customer);
    }

    @Test
    void testShowNameSearchForm() {
        String result = customerController.showNameSearchForm(model);

        assertEquals("customer/name-search", result);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestNameSearch() {
        Customer customer = new Customer();
        customer.setName("TestCustomer");
        when(customerService.findOneByEnabledTrueAndName("TestCustomer")).thenReturn(customer);

        String result = customerController.processRequestNameSearch(customer, model);

        assertEquals("customer/show-one", result);
        verify(model, times(1)).addAttribute("customer", customer);
    }

    @Test
    void testShowEmailSearchForm() {
        String result = customerController.showEmailSearchForm(model);

        assertEquals("customer/email-search", result);
        verify(model, times(1)).addAttribute(eq("customer"), any(Customer.class));
    }

    @Test
    void testProcessRequestEmailSearch() {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        List<Customer> customers = new ArrayList<>();
        when(customerService.findByEnabledTrueAndEmail("test@example.com")).thenReturn(customers);

        String result = customerController.processRequestEmailSearch(customer, model);

        assertEquals("customer/show-list", result);
        verify(model, times(1)).addAttribute("customers", customers);
    }

    @Test
    void testShowCitySearchForm() {
        String result = customerController.showCitySearchForm(model);

        assertEquals("customer/city-search", result);
    }

    @Test
    void testCreateCustomerBasedOnAnotherOne() {
        Customer existingCustomer = Customer.builder()
                .id(1L)
                .name("Existing")
                .email("existing@example.com")
                .phone(123456)
                .categories(new HashSet<>())
                .firstName("John")
                .lastName("Doe")
                .city("City")
                .address("Address")
                .enabled(1)
                .build();

        Customer newCustomer = Customer.builder()
                .name("New")
                .email("new@example.com")
                .phone(654321)
                .categories(new HashSet<>())
                .firstName("Jane")
                .lastName("Smith")
                .city("NewCity")
                .address("NewAddress")
                .enabled(1)
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(customerService.getMaxId()).thenReturn(10L);

        String result = customerController.createCustomerBasedOnAnotherOne(1L, newCustomer, bindingResult);

        assertEquals("redirect:/customer/list", result);
        verify(customerService, times(1)).saveCustomer(any(Customer.class));
    }
}

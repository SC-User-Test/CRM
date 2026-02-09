package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    private CSVController csvController;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        csvController = new CSVController(customerService);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    public void testCSVControllerCreation() {
        assertNotNull(csvController);
    }

    @Test
    public void testFindCustomers() throws IOException {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .email("customer1@example.com")
                .phone(123456789)
                .enabled(1)
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer2")
                .email("customer2@example.com")
                .phone(987654321)
                .enabled(1)
                .build();

        List<Customer> customers = Arrays.asList(customer1, customer2);
        when(customerService.listAllCustomers()).thenReturn(customers);

        csvController.findCustomers(httpServletResponse);
        verify(customerService, times(1)).listAllCustomers();
        verify(httpServletResponse, times(1)).getWriter();
    }

    @Test
    public void testFindCustomersEmpty() throws IOException {
        List<Customer> emptyList = Arrays.asList();
        when(customerService.listAllCustomers()).thenReturn(emptyList);

        csvController.findCustomers(httpServletResponse);
        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    public void testFindCustomer() throws IOException {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .enabled(1)
                .build();

        when(customerService.showCustomer(1L)).thenReturn(customer);

        csvController.findCustomer(1L, httpServletResponse);
        verify(customerService, times(1)).showCustomer(1L);
        verify(httpServletResponse, times(1)).getWriter();
    }

    @Test
    public void testFindCustomerNotFound() throws IOException {
        when(customerService.showCustomer(999L)).thenReturn(null);

        csvController.findCustomer(999L, httpServletResponse);
        verify(customerService, times(1)).showCustomer(999L);
    }

    @Test
    public void testFindCustomerWithDifferentIds() throws IOException {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("Customer1")
                .email("customer1@example.com")
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer2")
                .email("customer2@example.com")
                .build();

        when(customerService.showCustomer(1L)).thenReturn(customer1);
        when(customerService.showCustomer(2L)).thenReturn(customer2);

        csvController.findCustomer(1L, httpServletResponse);
        csvController.findCustomer(2L, httpServletResponse);

        verify(customerService, times(1)).showCustomer(1L);
        verify(customerService, times(1)).showCustomer(2L);
    }
}

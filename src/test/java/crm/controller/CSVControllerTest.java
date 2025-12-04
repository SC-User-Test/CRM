package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private CSVController csvController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindCustomers() throws Exception {
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("City")
                .address("Address")
                .enabled(1)
                .categories(new HashSet<>())
                .build();
        customers.add(customer);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(customerService.listAllCustomers()).thenReturn(customers);
        when(httpServletResponse.getWriter()).thenReturn(writer);

        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomer() throws Exception {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .phone(123456)
                .firstName("John")
                .lastName("Doe")
                .city("City")
                .address("Address")
                .enabled(1)
                .categories(new HashSet<>())
                .build();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(customerService.showCustomer(1L)).thenReturn(customer);
        when(httpServletResponse.getWriter()).thenReturn(writer);

        assertDoesNotThrow(() -> csvController.findCustomer(1L, httpServletResponse));
        verify(customerService, times(1)).showCustomer(1L);
    }

    @Test
    void testFindCustomersEmpty() throws Exception {
        List<Customer> customers = new ArrayList<>();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(customerService.listAllCustomers()).thenReturn(customers);
        when(httpServletResponse.getWriter()).thenReturn(writer);

        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
    }
}

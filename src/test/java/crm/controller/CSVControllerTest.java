package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private CSVController csvController;

    private Customer customer;
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        customer = Customer.builder()
                .id(1L)
                .name("TestCustomer")
                .email("test@example.com")
                .phone(555123456)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void testConstructor_createsInstance() {
        CSVController controller = new CSVController(customerService);
        assertNotNull(controller);
    }

    @Test
    void testFindCustomers_callsCustomerServiceListAll() throws IOException {
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.listAllCustomers()).thenReturn(customers);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        csvController.findCustomers(httpServletResponse);

        verify(customerService).listAllCustomers();
        verify(httpServletResponse).getWriter();
    }

    @Test
    void testFindCustomer_callsCustomerServiceShowCustomer() throws IOException {
        when(customerService.showCustomer(1L)).thenReturn(customer);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        csvController.findCustomer(1L, httpServletResponse);

        verify(customerService).showCustomer(1L);
        verify(httpServletResponse).getWriter();
    }

    @Test
    void testFindCustomers_withEmptyList_doesNotThrow() throws IOException {
        List<Customer> emptyList = Arrays.asList();
        when(customerService.listAllCustomers()).thenReturn(emptyList);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
    }

    @Test
    void testFindCustomer_withValidId_doesNotThrow() throws IOException {
        when(customerService.showCustomer(1L)).thenReturn(customer);
        when(httpServletResponse.getWriter()).thenReturn(printWriter);

        assertDoesNotThrow(() -> csvController.findCustomer(1L, httpServletResponse));
    }
}

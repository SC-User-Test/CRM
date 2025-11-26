package crm.controller;

import crm.entity.Customer;
import crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CSVController csvController;

    private HttpServletResponse response;
    private Customer customer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        response = mock(HttpServletResponse.class);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testConstructor() {
        CSVController controller = new CSVController(customerService);
        assertNotNull(controller);
    }

    @Test
    void testFindCustomers() throws Exception {
        when(customerService.listAllCustomers()).thenReturn(Collections.singletonList(customer));

        assertDoesNotThrow(() -> csvController.findCustomers(response));

        verify(customerService, times(1)).listAllCustomers();
    }

    @Test
    void testFindCustomer() throws Exception {
        when(customerService.showCustomer(1L)).thenReturn(customer);

        assertDoesNotThrow(() -> csvController.findCustomer(1L, response));

        verify(customerService, times(1)).showCustomer(1L);
    }
}

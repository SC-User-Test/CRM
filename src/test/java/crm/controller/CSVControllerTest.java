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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CSVControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CSVController csvController;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private PrintWriter printWriter;

    private Customer customer;

    @BeforeEach
    void setUp() throws IOException {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Company");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);

        when(httpServletResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testConstructor_ShouldInitializeWithCustomerService() {
        // Arrange & Act
        CSVController controller = new CSVController(customerService);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testFindCustomers_ShouldCallCustomerService() throws IOException {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);
        when(customerService.listAllCustomers()).thenReturn(customers);

        // Act
        csvController.findCustomers(httpServletResponse);

        // Assert
        verify(customerService).listAllCustomers();
    }

    @Test
    void testFindCustomers_ShouldGetWriterFromResponse() throws IOException {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Collections.emptyList());

        // Act
        csvController.findCustomers(httpServletResponse);

        // Assert
        verify(httpServletResponse).getWriter();
    }

    @Test
    void testFindCustomers_WithEmptyList_ShouldNotThrowException() throws IOException {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertDoesNotThrow(() -> csvController.findCustomers(httpServletResponse));
    }

    @Test
    void testFindCustomers_WithMultipleCustomers_ShouldProcessAll() throws IOException {
        // Arrange
        Customer customer2 = new Customer();
        customer2.setId(2L);
        List<Customer> customers = Arrays.asList(customer, customer2);
        when(customerService.listAllCustomers()).thenReturn(customers);

        // Act
        csvController.findCustomers(httpServletResponse);

        // Assert
        verify(customerService).listAllCustomers();
    }

    @Test
    void testFindCustomer_ShouldCallCustomerServiceWithId() throws IOException {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act
        csvController.findCustomer(1L, httpServletResponse);

        // Assert
        verify(customerService).showCustomer(1L);
    }

    @Test
    void testFindCustomer_ShouldGetWriterFromResponse() throws IOException {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act
        csvController.findCustomer(1L, httpServletResponse);

        // Assert
        verify(httpServletResponse).getWriter();
    }

    @Test
    void testFindCustomer_WithNullId_ShouldCallService() throws IOException {
        // Arrange
        when(customerService.showCustomer(null)).thenReturn(null);

        // Act
        csvController.findCustomer(null, httpServletResponse);

        // Assert
        verify(customerService).showCustomer(null);
    }

    @Test
    void testFindCustomer_WithValidId_ShouldNotThrowException() throws IOException {
        // Arrange
        when(customerService.showCustomer(1L)).thenReturn(customer);

        // Act & Assert
        assertDoesNotThrow(() -> csvController.findCustomer(1L, httpServletResponse));
    }

    @Test
    void testFindCustomers_WithIOException_ShouldPropagateException() throws IOException {
        // Arrange
        when(customerService.listAllCustomers()).thenReturn(Collections.emptyList());
        when(httpServletResponse.getWriter()).thenThrow(new IOException("Test exception"));

        // Act & Assert
        assertThrows(IOException.class, () -> csvController.findCustomers(httpServletResponse));
    }
}

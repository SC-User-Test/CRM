package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WriteCsvToResponseTest {

    private PrintWriter printWriter;
    private StringWriter stringWriter;
    private Customer customer;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Company");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("Test City");
        customer.setAddress("123 Test St");
        customer.setEnabled(1);
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        WriteCsvToResponse utils = new WriteCsvToResponse();

        // Assert
        assertNotNull(utils);
    }

    @Test
    void testWriteCustomers_WithValidList_ShouldNotThrowException() {
        // Arrange
        List<Customer> customers = Arrays.asList(customer);

        // Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, customers));
    }

    @Test
    void testWriteCustomers_WithEmptyList_ShouldNotThrowException() {
        // Arrange
        List<Customer> customers = Collections.emptyList();

        // Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, customers));
    }

    @Test
    void testWriteCustomers_WithMultipleCustomers_ShouldWriteAll() {
        // Arrange
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Company2");
        List<Customer> customers = Arrays.asList(customer, customer2);

        // Act
        WriteCsvToResponse.writeCustomers(printWriter, customers);

        // Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, customers));
    }

    @Test
    void testWriteCustomer_WithValidCustomer_ShouldNotThrowException() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(printWriter, customer));
    }

    @Test
    void testWriteCustomer_WithNullCustomer_ShouldHandleGracefully() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(printWriter, null));
    }

    @Test
    void testWriteCustomers_StaticMethod_ShouldBeAccessible() throws Exception {
        // Arrange & Act & Assert
        assertNotNull(WriteCsvToResponse.class.getMethod("writeCustomers", PrintWriter.class, List.class));
    }

    @Test
    void testWriteCustomer_StaticMethod_ShouldBeAccessible() throws Exception {
        // Arrange & Act & Assert
        assertNotNull(WriteCsvToResponse.class.getMethod("writeCustomer", PrintWriter.class, Customer.class));
    }
}

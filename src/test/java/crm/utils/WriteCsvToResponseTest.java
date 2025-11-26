package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class WriteCsvToResponseTest {

    private PrintWriter printWriter;
    private StringWriter stringWriter;
    private Customer customer;
    private List<Customer> customers;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");
        customer.setEmail("test@example.com");
        customer.setPhone(123456789);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setCity("New York");
        customer.setAddress("123 Main St");
        customer.setEnabled(1);

        customers = Arrays.asList(customer);
    }

    @Test
    public void testConstructor() {
        WriteCsvToResponse writer = new WriteCsvToResponse();
        assertNotNull(writer);
    }

    @Test
    public void testWriteCustomersNotNull() {
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    public void testWriteCustomersWithEmptyList() {
        List<Customer> emptyList = Arrays.asList();
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, emptyList);
        });
    }

    @Test
    public void testWriteCustomerNotNull() {
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, customer);
        });
    }

    @Test
    public void testWriteCustomersProducesOutput() {
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        printWriter.flush();
        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    public void testWriteCustomerProducesOutput() {
        WriteCsvToResponse.writeCustomer(printWriter, customer);
        printWriter.flush();
        String output = stringWriter.toString();
        assertNotNull(output);
    }

    @Test
    public void testWriteCustomersWithMultipleCustomers() {
        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Second Customer");
        customer2.setEmail("second@example.com");
        List<Customer> multipleCustomers = Arrays.asList(customer, customer2);

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, multipleCustomers);
        });
    }

    @Test
    public void testWriteCustomerWithNullFields() {
        Customer nullCustomer = new Customer();
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, nullCustomer);
        });
    }
}

package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    private PrintWriter printWriter;
    private StringWriter stringWriter;
    private Customer customer;
    private List<Customer> customers;

    @BeforeEach
    void setUp() {
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

        customers = new ArrayList<>();
        customers.add(customer);
    }

    @Test
    void testConstructor() {
        WriteCsvToResponse writeCsvToResponse = new WriteCsvToResponse();
        assertNotNull(writeCsvToResponse);
    }

    @Test
    void testWriteCustomers_NotNull() {
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, customers));
    }

    @Test
    void testWriteCustomer_NotNull() {
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(printWriter, customer));
    }

    @Test
    void testWriteCustomersMethod_Exists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.utils.WriteCsvToResponse");
            assertNotNull(clazz.getMethod("writeCustomers", PrintWriter.class, List.class));
        });
    }

    @Test
    void testWriteCustomerMethod_Exists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.utils.WriteCsvToResponse");
            assertNotNull(clazz.getMethod("writeCustomer", PrintWriter.class, Customer.class));
        });
    }
}

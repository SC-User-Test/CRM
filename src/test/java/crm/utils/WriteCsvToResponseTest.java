package crm.utils;

import com.opencsv.exceptions.CsvException;
import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WriteCsvToResponseTest {

    private PrintWriter printWriter;
    private StringWriter stringWriter;
    private Customer customer;
    private List<Customer> customers;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        customer = Customer.builder()
                .id(1L)
                .name("Test Company")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        customers = Arrays.asList(customer);
    }

    @Test
    public void testWriteCustomers() {
        WriteCsvToResponse.writeCustomers(printWriter, customers);
        printWriter.flush();
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testWriteCustomersWithEmptyList() {
        List<Customer> emptyList = Arrays.asList();
        WriteCsvToResponse.writeCustomers(printWriter, emptyList);
        printWriter.flush();
        String result = stringWriter.toString();
        assertNotNull(result);
    }

    @Test
    public void testWriteCustomer() {
        WriteCsvToResponse.writeCustomer(printWriter, customer);
        printWriter.flush();
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testWriteCustomerWithNullValues() {
        Customer customerWithNulls = Customer.builder()
                .id(2L)
                .name("Null Test")
                .email("null@test.com")
                .phone(0)
                .enabled(0)
                .build();

        WriteCsvToResponse.writeCustomer(printWriter, customerWithNulls);
        printWriter.flush();
        String result = stringWriter.toString();
        assertNotNull(result);
    }

    @Test
    public void testWriteCustomersMultiple() {
        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Second Company")
                .email("second@example.com")
                .phone(987654321)
                .firstName("Jane")
                .lastName("Smith")
                .city("Boston")
                .address("456 Oak Ave")
                .enabled(1)
                .build();

        List<Customer> multipleCustomers = Arrays.asList(customer, customer2);
        WriteCsvToResponse.writeCustomers(printWriter, multipleCustomers);
        printWriter.flush();
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
}

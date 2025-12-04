package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private List<Customer> customers;
    private Customer customer;

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        customers = new ArrayList<>();

        customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("TestCity")
                .address("123 Test St")
                .enabled(1)
                .categories(new HashSet<>())
                .build();

        customers.add(customer);
    }

    @Test
    void testWriteCustomers() {
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomersWithEmptyList() {
        List<Customer> emptyList = new ArrayList<>();
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, emptyList);
        });
    }

    @Test
    void testWriteCustomer() {
        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, customer);
        });
    }

    @Test
    void testWriteCustomersWithMultiple() {
        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Customer 2")
                .email("customer2@example.com")
                .phone(987654321)
                .firstName("Jane")
                .lastName("Smith")
                .city("City2")
                .address("456 Test Ave")
                .enabled(1)
                .categories(new HashSet<>())
                .build();

        customers.add(customer2);

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCsvToResponseClassExists() {
        assertNotNull(WriteCsvToResponse.class);
    }
}

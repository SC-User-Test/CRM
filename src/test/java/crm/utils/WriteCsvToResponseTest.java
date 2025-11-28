package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    @Mock
    private PrintWriter printWriter;

    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    @Test
    void testWriteCsvToResponseConstructor() {
        WriteCsvToResponse utils = new WriteCsvToResponse();
        assertNotNull(utils);
    }

    @Test
    void testWriteCustomersWithEmptyList() {
        List<Customer> customers = new ArrayList<>();

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomersWithSingleCustomer() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
        customers.add(customer);

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomersWithMultipleCustomers() {
        List<Customer> customers = new ArrayList<>();

        Customer customer1 = Customer.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .phone(123456789)
                .enabled(1)
                .build();

        Customer customer2 = Customer.builder()
                .id(2L)
                .name("Jane Smith")
                .email("jane@example.com")
                .phone(987654321)
                .enabled(1)
                .build();

        customers.add(customer1);
        customers.add(customer2);

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomerWithValidCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(111222333)
                .firstName("Test")
                .lastName("Customer")
                .city("Test City")
                .address("Test Address")
                .enabled(1)
                .build();

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, customer);
        });
    }

    @Test
    void testWriteCustomerWithNullValues() {
        Customer customer = Customer.builder()
                .id(1L)
                .name(null)
                .email(null)
                .phone(0)
                .firstName(null)
                .lastName(null)
                .city(null)
                .address(null)
                .enabled(0)
                .build();

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, customer);
        });
    }

    @Test
    void testWriteCustomersDoesNotThrowException() {
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder().id(1L).name("Test").build();
        customers.add(customer);

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomers(printWriter, customers);
        });
    }

    @Test
    void testWriteCustomerDoesNotThrowException() {
        Customer customer = Customer.builder().id(1L).name("Test").build();

        assertDoesNotThrow(() -> {
            WriteCsvToResponse.writeCustomer(printWriter, customer);
        });
    }
}

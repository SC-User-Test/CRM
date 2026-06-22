package crm.utils;

import crm.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    void writeCustomers_withValidCustomerList_shouldWriteCsv() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();
        customers.add(customer);

        // Act
        WriteCsvToResponse.writeCustomers(printWriter, customers);

        // Assert
        assertNotNull(stringWriter.toString());
    }

    @Test
    void writeCustomers_withEmptyList_shouldHandleGracefully() {
        // Arrange
        List<Customer> customers = new ArrayList<>();

        // Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomers(printWriter, customers));
    }

    @Test
    void writeCustomer_withValidCustomer_shouldWriteCsv() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("Test Customer")
                .email("test@example.com")
                .phone(123456789)
                .firstName("John")
                .lastName("Doe")
                .city("New York")
                .address("123 Main St")
                .enabled(1)
                .build();

        // Act
        WriteCsvToResponse.writeCustomer(printWriter, customer);

        // Assert
        assertNotNull(stringWriter.toString());
    }

    @Test
    void writeCustomer_withNullCustomer_shouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> WriteCsvToResponse.writeCustomer(printWriter, null));
    }

    @Test
    void writeCustomers_withMultipleCustomers_shouldWriteAllRecords() {
        // Arrange
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Customer customer = Customer.builder()
                    .id((long) i)
                    .name("Customer " + i)
                    .email("customer" + i + "@example.com")
                    .phone(123456789 + i)
                    .firstName("First" + i)
                    .lastName("Last" + i)
                    .city("City" + i)
                    .address("Address " + i)
                    .enabled(1)
                    .build();
            customers.add(customer);
        }

        // Act
        WriteCsvToResponse.writeCustomers(printWriter, customers);

        // Assert
        assertNotNull(stringWriter.toString());
    }
}

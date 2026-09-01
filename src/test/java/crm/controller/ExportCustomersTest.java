package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExportCustomersTest {

    @InjectMocks
    private ExportCustomers exportCustomers;

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        ExportCustomers ec = new ExportCustomers();
        // Assert
        assertNotNull(ec);
    }

    @Test
    void testInstance_isNotNull() {
        // Arrange & Act & Assert
        assertNotNull(exportCustomers);
    }
}

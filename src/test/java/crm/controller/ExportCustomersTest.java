package crm.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportCustomersTest {

    @Test
    void exportCustomers_shouldBeInstantiable() {
        // Act & Assert
        assertDoesNotThrow(() -> new ExportCustomers());
    }

    @Test
    void exportCustomers_classExists() {
        // Assert
        assertNotNull(ExportCustomers.class);
    }
}

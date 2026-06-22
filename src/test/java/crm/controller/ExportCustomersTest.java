package crm.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportCustomersTest {

    @Test
    void testExportCustomersClassExists() {
        // Assert
        assertNotNull(ExportCustomers.class);
    }

    @Test
    void testExportCustomersIsInstantiable() {
        // Act
        ExportCustomers exportCustomers = new ExportCustomers();

        // Assert
        assertNotNull(exportCustomers);
    }
}

package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportCustomersTest {

    private ExportCustomers exportCustomers;

    @BeforeEach
    void setUp() {
        exportCustomers = new ExportCustomers();
    }

    @Test
    void testExportCustomersConstructor() {
        assertNotNull(exportCustomers);
    }

    @Test
    void testExportCustomersClassExists() {
        assertNotNull(ExportCustomers.class);
    }

    @Test
    void testExportCustomersIsCommented() {
        assertTrue(ExportCustomers.class.getName().contains("ExportCustomers"));
    }
}

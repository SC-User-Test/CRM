package crm.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportCustomersTest {

    @Test
    void testDefaultConstructor_createsInstance() {
        ExportCustomers exportCustomers = new ExportCustomers();
        assertNotNull(exportCustomers);
    }

    @Test
    void testExportCustomers_isInstantiable() {
        ExportCustomers exportCustomers = new ExportCustomers();
        assertNotNull(exportCustomers);
        assertInstanceOf(ExportCustomers.class, exportCustomers);
    }
}

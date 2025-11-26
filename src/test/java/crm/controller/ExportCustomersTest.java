package crm.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExportCustomersTest {

    @Test
    void testConstructor() {
        ExportCustomers exportCustomers = new ExportCustomers();
        assertNotNull(exportCustomers);
    }

    @Test
    void testClassExists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.controller.ExportCustomers");
            assertNotNull(clazz);
        });
    }
}

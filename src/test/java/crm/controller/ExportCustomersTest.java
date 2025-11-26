package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExportCustomersTest {

    private ExportCustomers exportCustomers;

    @BeforeEach
    public void setUp() {
        exportCustomers = new ExportCustomers();
    }

    @Test
    public void testConstructor() {
        ExportCustomers controller = new ExportCustomers();
        assertNotNull(controller);
    }

    @Test
    public void testExportCustomersInstantiation() {
        assertNotNull(exportCustomers);
    }

    @Test
    public void testExportCustomersIsNotNull() {
        ExportCustomers newInstance = new ExportCustomers();
        assertNotNull(newInstance);
        assertNotSame(exportCustomers, newInstance);
    }
}

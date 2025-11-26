package crm.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVTestTest {

    @Test
    void testConstructor() {
        CSVTest csvTest = new CSVTest();
        assertNotNull(csvTest);
    }

    @Test
    void testClassExists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.csv.CSVTest");
            assertNotNull(clazz);
        });
    }

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.csv.CSVTest");
            assertNotNull(clazz.getMethod("main", String[].class));
        });
    }
}

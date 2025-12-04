package crm.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVTestTest {

    @Test
    void testCSVTestClassExists() {
        assertNotNull(CSVTest.class);
    }

    @Test
    void testCSVTestMainMethodExists() {
        assertDoesNotThrow(() -> {
            CSVTest.class.getDeclaredMethod("main", String[].class);
        });
    }

    @Test
    void testCSVTestConstructor() {
        assertDoesNotThrow(() -> {
            new CSVTest();
        });
    }
}

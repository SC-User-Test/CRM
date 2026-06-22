package crm.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVTestTest {

    @Test
    void testCSVTestClassExists() {
        // Assert
        assertNotNull(CSVTest.class);
    }

    @Test
    void testCSVTestIsInstantiable() {
        // Act
        CSVTest csvTest = new CSVTest();

        // Assert
        assertNotNull(csvTest);
    }
}

package crm.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CSVTestTest {

    @Test
    public void testConstructor() {
        CSVTest csvTest = new CSVTest();
        assertNotNull(csvTest);
    }

    @Test
    public void testCSVTestInstantiation() {
        CSVTest instance = new CSVTest();
        assertNotNull(instance);
    }

    @Test
    public void testCSVTestIsNotNull() {
        CSVTest test1 = new CSVTest();
        CSVTest test2 = new CSVTest();
        assertNotNull(test1);
        assertNotNull(test2);
        assertNotSame(test1, test2);
    }
}

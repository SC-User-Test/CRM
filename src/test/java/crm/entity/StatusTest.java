package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StatusTest {

    @Test
    public void testProposedValue() {
        Status status = Status.PROPOSED;
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    public void testNegotiatedValue() {
        Status status = Status.NEGOTIATED;
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    public void testImplementedValue() {
        Status status = Status.IMPLEMENTED;
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    public void testDoneValue() {
        Status status = Status.DONE;
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    public void testAllStatuses() {
        Status[] allStatuses = Status.ALL;
        assertNotNull(allStatuses);
        assertEquals(4, allStatuses.length);
    }

    @Test
    public void testAllArrayContainsAllStatuses() {
        Status[] allStatuses = Status.ALL;
        assertEquals(Status.PROPOSED, allStatuses[0]);
        assertEquals(Status.NEGOTIATED, allStatuses[1]);
        assertEquals(Status.IMPLEMENTED, allStatuses[2]);
        assertEquals(Status.DONE, allStatuses[3]);
    }

    @Test
    public void testValuesMethod() {
        Status[] values = Status.values();
        assertEquals(4, values.length);
        assertTrue(values.length > 0);
    }

    @Test
    public void testValueOfMethod() {
        Status status = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    public void testValueOfThrowsExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }

    @Test
    public void testAllStatusesAreUnique() {
        Status[] allStatuses = Status.values();
        for (int i = 0; i < allStatuses.length; i++) {
            for (int j = i + 1; j < allStatuses.length; j++) {
                assertNotEquals(allStatuses[i], allStatuses[j]);
            }
        }
    }
}

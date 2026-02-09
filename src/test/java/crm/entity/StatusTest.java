package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatusTest {

    @Test
    public void testProposedStatus() {
        Status status = Status.PROPOSED;
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    public void testNegotiatedStatus() {
        Status status = Status.NEGOTIATED;
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    public void testImplementedStatus() {
        Status status = Status.IMPLEMENTED;
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    public void testDoneStatus() {
        Status status = Status.DONE;
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    public void testAllStatusesArray() {
        Status[] allStatuses = Status.ALL;
        assertNotNull(allStatuses);
        assertEquals(4, allStatuses.length);
        assertEquals(Status.PROPOSED, allStatuses[0]);
        assertEquals(Status.NEGOTIATED, allStatuses[1]);
        assertEquals(Status.IMPLEMENTED, allStatuses[2]);
        assertEquals(Status.DONE, allStatuses[3]);
    }

    @Test
    public void testStatusValueOf() {
        Status status = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, status);

        status = Status.valueOf("NEGOTIATED");
        assertEquals(Status.NEGOTIATED, status);

        status = Status.valueOf("IMPLEMENTED");
        assertEquals(Status.IMPLEMENTED, status);

        status = Status.valueOf("DONE");
        assertEquals(Status.DONE, status);
    }

    @Test
    public void testStatusValues() {
        Status[] values = Status.values();
        assertNotNull(values);
        assertTrue(values.length >= 4);
    }

    @Test
    public void testInvalidStatusValueOf() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }
}

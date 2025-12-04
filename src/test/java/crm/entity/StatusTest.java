package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testStatusEnumValues() {
        assertEquals(4, Status.values().length);
    }

    @Test
    void testStatusProposed() {
        Status status = Status.PROPOSED;
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void testStatusNegotiated() {
        Status status = Status.NEGOTIATED;
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void testStatusImplemented() {
        Status status = Status.IMPLEMENTED;
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void testStatusDone() {
        Status status = Status.DONE;
        assertEquals("DONE", status.name());
    }

    @Test
    void testStatusAllArray() {
        Status[] allStatuses = Status.ALL;
        assertEquals(4, allStatuses.length);
        assertEquals(Status.PROPOSED, allStatuses[0]);
        assertEquals(Status.NEGOTIATED, allStatuses[1]);
        assertEquals(Status.IMPLEMENTED, allStatuses[2]);
        assertEquals(Status.DONE, allStatuses[3]);
    }

    @Test
    void testStatusValueOf() {
        Status proposed = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, proposed);

        Status done = Status.valueOf("DONE");
        assertEquals(Status.DONE, done);
    }

    @Test
    void testStatusValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }

    @Test
    void testStatusOrdinal() {
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(1, Status.NEGOTIATED.ordinal());
        assertEquals(2, Status.IMPLEMENTED.ordinal());
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    void testStatusComparison() {
        assertTrue(Status.PROPOSED.ordinal() < Status.DONE.ordinal());
        assertTrue(Status.NEGOTIATED.ordinal() < Status.IMPLEMENTED.ordinal());
    }

    @Test
    void testStatusArrayContainsAllValues() {
        Status[] all = Status.ALL;
        for (Status status : Status.values()) {
            boolean found = false;
            for (Status s : all) {
                if (s == status) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "Status " + status + " not found in ALL array");
        }
    }
}

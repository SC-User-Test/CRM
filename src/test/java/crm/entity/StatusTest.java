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
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void testStatusNegotiated() {
        Status status = Status.NEGOTIATED;
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void testStatusImplemented() {
        Status status = Status.IMPLEMENTED;
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void testStatusDone() {
        Status status = Status.DONE;
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    void testStatusAllArray() {
        assertNotNull(Status.ALL);
        assertEquals(4, Status.ALL.length);
    }

    @Test
    void testStatusAllContainsProposed() {
        assertTrue(containsStatus(Status.ALL, Status.PROPOSED));
    }

    @Test
    void testStatusAllContainsNegotiated() {
        assertTrue(containsStatus(Status.ALL, Status.NEGOTIATED));
    }

    @Test
    void testStatusAllContainsImplemented() {
        assertTrue(containsStatus(Status.ALL, Status.IMPLEMENTED));
    }

    @Test
    void testStatusAllContainsDone() {
        assertTrue(containsStatus(Status.ALL, Status.DONE));
    }

    @Test
    void testStatusValueOf() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testStatusValueOfInvalid() {
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
    }

    private boolean containsStatus(Status[] array, Status status) {
        for (Status s : array) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}

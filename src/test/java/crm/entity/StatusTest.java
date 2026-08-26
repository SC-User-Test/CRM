package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

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
    void testAllStatusArray() {
        Status[] all = Status.ALL;
        assertNotNull(all);
        assertEquals(4, all.length);
    }

    @Test
    void testAllStatusArrayContainsProposed() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.PROPOSED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllStatusArrayContainsNegotiated() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.NEGOTIATED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllStatusArrayContainsImplemented() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.IMPLEMENTED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllStatusArrayContainsDone() {
        boolean found = false;
        for (Status s : Status.ALL) {
            if (s == Status.DONE) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testValueOf() {
        Status status = Status.valueOf("PROPOSED");
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void testValueOfNegotiated() {
        Status status = Status.valueOf("NEGOTIATED");
        assertEquals(Status.NEGOTIATED, status);
    }

    @Test
    void testValueOfInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID_STATUS"));
    }

    @Test
    void testOrdinalProposed() {
        assertEquals(0, Status.PROPOSED.ordinal());
    }

    @Test
    void testOrdinalNegotiated() {
        assertEquals(1, Status.NEGOTIATED.ordinal());
    }

    @Test
    void testOrdinalImplemented() {
        assertEquals(2, Status.IMPLEMENTED.ordinal());
    }

    @Test
    void testOrdinalDone() {
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    void testValues() {
        Status[] values = Status.values();
        assertEquals(4, values.length);
    }

    @Test
    void testEquality() {
        assertEquals(Status.PROPOSED, Status.PROPOSED);
        assertNotEquals(Status.PROPOSED, Status.DONE);
    }
}

package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testProposedStatus_exists() {
        assertNotNull(Status.PROPOSED);
    }

    @Test
    void testNegotiatedStatus_exists() {
        assertNotNull(Status.NEGOTIATED);
    }

    @Test
    void testImplementedStatus_exists() {
        assertNotNull(Status.IMPLEMENTED);
    }

    @Test
    void testDoneStatus_exists() {
        assertNotNull(Status.DONE);
    }

    @Test
    void testAllStatuses_containsFourValues() {
        assertEquals(4, Status.ALL.length);
    }

    @Test
    void testAllStatuses_containsProposed() {
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
    void testAllStatuses_containsNegotiated() {
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
    void testAllStatuses_containsImplemented() {
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
    void testAllStatuses_containsDone() {
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
    void testValueOf_proposed_returnsProposed() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
    }

    @Test
    void testValueOf_negotiated_returnsNegotiated() {
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
    }

    @Test
    void testValueOf_implemented_returnsImplemented() {
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
    }

    @Test
    void testValueOf_done_returnsDone() {
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testValueOf_invalid_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testValues_returnsAllStatuses() {
        Status[] values = Status.values();
        assertEquals(4, values.length);
    }

    @Test
    void testOrdinal_proposed_isZero() {
        assertEquals(0, Status.PROPOSED.ordinal());
    }

    @Test
    void testOrdinal_negotiated_isOne() {
        assertEquals(1, Status.NEGOTIATED.ordinal());
    }

    @Test
    void testOrdinal_implemented_isTwo() {
        assertEquals(2, Status.IMPLEMENTED.ordinal());
    }

    @Test
    void testOrdinal_done_isThree() {
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    void testName_proposed_returnsProposed() {
        assertEquals("PROPOSED", Status.PROPOSED.name());
    }

    @Test
    void testName_done_returnsDone() {
        assertEquals("DONE", Status.DONE.name());
    }
}

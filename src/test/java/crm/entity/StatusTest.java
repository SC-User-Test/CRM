package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testProposedEnumValue() {
        assertNotNull(Status.PROPOSED);
        assertEquals("PROPOSED", Status.PROPOSED.name());
    }

    @Test
    void testNegotiatedEnumValue() {
        assertNotNull(Status.NEGOTIATED);
        assertEquals("NEGOTIATED", Status.NEGOTIATED.name());
    }

    @Test
    void testImplementedEnumValue() {
        assertNotNull(Status.IMPLEMENTED);
        assertEquals("IMPLEMENTED", Status.IMPLEMENTED.name());
    }

    @Test
    void testDoneEnumValue() {
        assertNotNull(Status.DONE);
        assertEquals("DONE", Status.DONE.name());
    }

    @Test
    void testAllArray_ContainsAllValues() {
        assertEquals(4, Status.ALL.length);
    }

    @Test
    void testAllArray_ContainsProposed() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.PROPOSED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllArray_ContainsNegotiated() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.NEGOTIATED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllArray_ContainsImplemented() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.IMPLEMENTED) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testAllArray_ContainsDone() {
        boolean found = false;
        for (Status status : Status.ALL) {
            if (status == Status.DONE) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testValueOf() {
        assertEquals(Status.PROPOSED, Status.valueOf("PROPOSED"));
        assertEquals(Status.NEGOTIATED, Status.valueOf("NEGOTIATED"));
        assertEquals(Status.IMPLEMENTED, Status.valueOf("IMPLEMENTED"));
        assertEquals(Status.DONE, Status.valueOf("DONE"));
    }

    @Test
    void testValueOf_InvalidValue_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID");
        });
    }
}

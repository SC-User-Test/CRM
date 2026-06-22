package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testStatusEnumValues() {
        // Assert
        assertEquals(4, Status.values().length);
    }

    @Test
    void testProposedStatus() {
        // Act
        Status status = Status.PROPOSED;

        // Assert
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void testNegotiatedStatus() {
        // Act
        Status status = Status.NEGOTIATED;

        // Assert
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void testImplementedStatus() {
        // Act
        Status status = Status.IMPLEMENTED;

        // Assert
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void testDoneStatus() {
        // Act
        Status status = Status.DONE;

        // Assert
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    void testStatusValueOf() {
        // Act
        Status proposed = Status.valueOf("PROPOSED");
        Status negotiated = Status.valueOf("NEGOTIATED");
        Status implemented = Status.valueOf("IMPLEMENTED");
        Status done = Status.valueOf("DONE");

        // Assert
        assertEquals(Status.PROPOSED, proposed);
        assertEquals(Status.NEGOTIATED, negotiated);
        assertEquals(Status.IMPLEMENTED, implemented);
        assertEquals(Status.DONE, done);
    }

    @Test
    void testStatusValueOf_withInvalidValue() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Status.valueOf("INVALID_STATUS");
        });
    }

    @Test
    void testStatusAllArray() {
        // Act
        Status[] allStatuses = Status.ALL;

        // Assert
        assertNotNull(allStatuses);
        assertEquals(4, allStatuses.length);
        assertEquals(Status.PROPOSED, allStatuses[0]);
        assertEquals(Status.NEGOTIATED, allStatuses[1]);
        assertEquals(Status.IMPLEMENTED, allStatuses[2]);
        assertEquals(Status.DONE, allStatuses[3]);
    }

    @Test
    void testStatusOrdinal() {
        // Assert
        assertEquals(0, Status.PROPOSED.ordinal());
        assertEquals(1, Status.NEGOTIATED.ordinal());
        assertEquals(2, Status.IMPLEMENTED.ordinal());
        assertEquals(3, Status.DONE.ordinal());
    }

    @Test
    void testStatusEquality() {
        // Arrange
        Status status1 = Status.PROPOSED;
        Status status2 = Status.PROPOSED;

        // Assert
        assertEquals(status1, status2);
        assertSame(status1, status2);
    }

    @Test
    void testStatusInequality() {
        // Arrange
        Status status1 = Status.PROPOSED;
        Status status2 = Status.DONE;

        // Assert
        assertNotEquals(status1, status2);
    }

    @Test
    void testStatusToString() {
        // Act
        String proposedString = Status.PROPOSED.toString();
        String negotiatedString = Status.NEGOTIATED.toString();
        String implementedString = Status.IMPLEMENTED.toString();
        String doneString = Status.DONE.toString();

        // Assert
        assertEquals("PROPOSED", proposedString);
        assertEquals("NEGOTIATED", negotiatedString);
        assertEquals("IMPLEMENTED", implementedString);
        assertEquals("DONE", doneString);
    }

    @Test
    void testStatusIsEnum() {
        // Assert
        assertTrue(Status.class.isEnum());
    }

    @Test
    void testStatusAllArrayContainsAllValues() {
        // Arrange
        Status[] allStatuses = Status.ALL;

        // Assert
        assertTrue(containsStatus(allStatuses, Status.PROPOSED));
        assertTrue(containsStatus(allStatuses, Status.NEGOTIATED));
        assertTrue(containsStatus(allStatuses, Status.IMPLEMENTED));
        assertTrue(containsStatus(allStatuses, Status.DONE));
    }

    @Test
    void testStatusAllArrayLength() {
        // Assert
        assertEquals(Status.values().length, Status.ALL.length);
    }

    private boolean containsStatus(Status[] statuses, Status status) {
        for (Status s : statuses) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}

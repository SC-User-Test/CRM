package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void testStatusValues_containsAllExpectedValues() {
        // Arrange & Act
        Status[] values = Status.values();
        // Assert
        assertEquals(4, values.length);
    }

    @Test
    void testStatus_PROPOSED_exists() {
        // Arrange & Act
        Status status = Status.PROPOSED;
        // Assert
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void testStatus_NEGOTIATED_exists() {
        // Arrange & Act
        Status status = Status.NEGOTIATED;
        // Assert
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void testStatus_IMPLEMENTED_exists() {
        // Arrange & Act
        Status status = Status.IMPLEMENTED;
        // Assert
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void testStatus_DONE_exists() {
        // Arrange & Act
        Status status = Status.DONE;
        // Assert
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    void testStatusALL_containsAllValues() {
        // Arrange & Act
        Status[] all = Status.ALL;
        // Assert
        assertEquals(4, all.length);
        assertArrayEquals(Status.values(), all);
    }

    @Test
    void testValueOf_PROPOSED_returnsCorrectStatus() {
        // Arrange & Act
        Status status = Status.valueOf("PROPOSED");
        // Assert
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void testValueOf_NEGOTIATED_returnsCorrectStatus() {
        // Arrange & Act
        Status status = Status.valueOf("NEGOTIATED");
        // Assert
        assertEquals(Status.NEGOTIATED, status);
    }

    @Test
    void testValueOf_IMPLEMENTED_returnsCorrectStatus() {
        // Arrange & Act
        Status status = Status.valueOf("IMPLEMENTED");
        // Assert
        assertEquals(Status.IMPLEMENTED, status);
    }

    @Test
    void testValueOf_DONE_returnsCorrectStatus() {
        // Arrange & Act
        Status status = Status.valueOf("DONE");
        // Assert
        assertEquals(Status.DONE, status);
    }

    @Test
    void testValueOf_invalidValue_throwsException() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testOrdinal_PROPOSED_isZero() {
        // Arrange & Act & Assert
        assertEquals(0, Status.PROPOSED.ordinal());
    }

    @Test
    void testOrdinal_NEGOTIATED_isOne() {
        // Arrange & Act & Assert
        assertEquals(1, Status.NEGOTIATED.ordinal());
    }

    @Test
    void testOrdinal_IMPLEMENTED_isTwo() {
        // Arrange & Act & Assert
        assertEquals(2, Status.IMPLEMENTED.ordinal());
    }

    @Test
    void testOrdinal_DONE_isThree() {
        // Arrange & Act & Assert
        assertEquals(3, Status.DONE.ordinal());
    }
}

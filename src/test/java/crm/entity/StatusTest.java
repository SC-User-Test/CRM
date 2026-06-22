package crm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusTest {

    @Test
    void status_shouldHaveProposedValue() {
        // Act
        Status status = Status.PROPOSED;

        // Assert
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void status_shouldHaveNegotiatedValue() {
        // Act
        Status status = Status.NEGOTIATED;

        // Assert
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void status_shouldHaveImplementedValue() {
        // Act
        Status status = Status.IMPLEMENTED;

        // Assert
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void status_shouldHaveDoneValue() {
        // Act
        Status status = Status.DONE;

        // Assert
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    void status_allArray_shouldContainAllValues() {
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
    void status_valueOf_shouldReturnCorrectEnum() {
        // Act
        Status status = Status.valueOf("PROPOSED");

        // Assert
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void status_values_shouldReturnAllEnumValues() {
        // Act
        Status[] values = Status.values();

        // Assert
        assertNotNull(values);
        assertEquals(4, values.length);
    }

    @Test
    void status_shouldSupportComparison() {
        // Arrange
        Status status1 = Status.PROPOSED;
        Status status2 = Status.PROPOSED;

        // Assert
        assertEquals(status1, status2);
    }

    @Test
    void status_shouldSupportToString() {
        // Arrange
        Status status = Status.IMPLEMENTED;

        // Act
        String result = status.toString();

        // Assert
        assertEquals("IMPLEMENTED", result);
    }
}

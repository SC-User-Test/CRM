package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StatusTest {

    @Test
    void testEnumValues_ShouldContainFourStatuses() {
        // Arrange & Act
        Status[] statuses = Status.values();

        // Assert
        assertEquals(4, statuses.length);
    }

    @Test
    void testEnumValues_ShouldContainProposed() {
        // Arrange & Act
        Status status = Status.PROPOSED;

        // Assert
        assertNotNull(status);
        assertEquals("PROPOSED", status.name());
    }

    @Test
    void testEnumValues_ShouldContainNegotiated() {
        // Arrange & Act
        Status status = Status.NEGOTIATED;

        // Assert
        assertNotNull(status);
        assertEquals("NEGOTIATED", status.name());
    }

    @Test
    void testEnumValues_ShouldContainImplemented() {
        // Arrange & Act
        Status status = Status.IMPLEMENTED;

        // Assert
        assertNotNull(status);
        assertEquals("IMPLEMENTED", status.name());
    }

    @Test
    void testEnumValues_ShouldContainDone() {
        // Arrange & Act
        Status status = Status.DONE;

        // Assert
        assertNotNull(status);
        assertEquals("DONE", status.name());
    }

    @Test
    void testAllConstant_ShouldContainFourStatuses() {
        // Arrange & Act
        Status[] all = Status.ALL;

        // Assert
        assertEquals(4, all.length);
    }

    @Test
    void testAllConstant_ShouldContainProposedAtIndexZero() {
        // Arrange & Act
        Status status = Status.ALL[0];

        // Assert
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void testAllConstant_ShouldContainNegotiatedAtIndexOne() {
        // Arrange & Act
        Status status = Status.ALL[1];

        // Assert
        assertEquals(Status.NEGOTIATED, status);
    }

    @Test
    void testAllConstant_ShouldContainImplementedAtIndexTwo() {
        // Arrange & Act
        Status status = Status.ALL[2];

        // Assert
        assertEquals(Status.IMPLEMENTED, status);
    }

    @Test
    void testAllConstant_ShouldContainDoneAtIndexThree() {
        // Arrange & Act
        Status status = Status.ALL[3];

        // Assert
        assertEquals(Status.DONE, status);
    }

    @Test
    void testValueOf_WithValidName_ShouldReturnStatus() {
        // Arrange & Act
        Status status = Status.valueOf("PROPOSED");

        // Assert
        assertEquals(Status.PROPOSED, status);
    }

    @Test
    void testValueOf_WithInvalidName_ShouldThrowException() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf("INVALID"));
    }

    @Test
    void testEnumEquality_SameName_ShouldBeEqual() {
        // Arrange
        Status status1 = Status.PROPOSED;
        Status status2 = Status.valueOf("PROPOSED");

        // Act & Assert
        assertEquals(status1, status2);
    }

    @Test
    void testEnumOrder_ShouldPreserveDeclarationOrder() {
        // Arrange & Act
        Status[] statuses = Status.values();

        // Assert
        assertEquals(Status.PROPOSED, statuses[0]);
        assertEquals(Status.NEGOTIATED, statuses[1]);
        assertEquals(Status.IMPLEMENTED, statuses[2]);
        assertEquals(Status.DONE, statuses[3]);
    }
}

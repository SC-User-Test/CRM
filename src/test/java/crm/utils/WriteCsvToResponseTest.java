package crm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteCsvToResponseTest {

    @Test
    void testWriteCsvToResponseClassExists() {
        // Assert
        assertNotNull(WriteCsvToResponse.class);
    }

    @Test
    void testWriteCsvToResponseIsInstantiable() {
        // Act
        WriteCsvToResponse writeCsvToResponse = new WriteCsvToResponse();

        // Assert
        assertNotNull(writeCsvToResponse);
    }
}

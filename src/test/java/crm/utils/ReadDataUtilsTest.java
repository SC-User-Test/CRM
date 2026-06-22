package crm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testReadDataUtilsClassExists() {
        // Assert
        assertNotNull(ReadDataUtils.class);
    }

    @Test
    void testReadDataUtilsIsInstantiable() {
        // Act
        ReadDataUtils readDataUtils = new ReadDataUtils();

        // Assert
        assertNotNull(readDataUtils);
    }
}

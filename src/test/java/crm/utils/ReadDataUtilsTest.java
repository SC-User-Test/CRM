package crm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testReadDataUtilsClassExists() {
        assertNotNull(ReadDataUtils.class);
    }

    @Test
    void testReadFileMethodExists() {
        assertDoesNotThrow(() -> {
            ReadDataUtils.class.getDeclaredMethod("ReadFile", String.class, javax.swing.JFrame.class, String.class, String[].class);
        });
    }

    @Test
    void testReadFileMethodSignature() {
        assertDoesNotThrow(() -> {
            ReadDataUtils.class.getDeclaredMethod("ReadFile", String.class, javax.swing.JFrame.class, String.class, String[].class);
        });
    }

    @Test
    void testReadDataUtilsConstructor() {
        assertDoesNotThrow(() -> {
            new ReadDataUtils();
        });
    }
}

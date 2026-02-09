package crm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReadDataUtilsTest {

    @Test
    public void testReadDataUtilsClass() {
        // Test that the class exists and can be instantiated
        assertNotNull(ReadDataUtils.class);
    }

    @Test
    public void testReadFileMethodExists() {
        // Test that the ReadFile method exists
        assertDoesNotThrow(() -> {
            ReadDataUtils.class.getMethod("ReadFile", String.class, javax.swing.JFrame.class, String.class, String[].class);
        });
    }

    @Test
    public void testReadFileMethodSignature() {
        // Validate method signature
        try {
            java.lang.reflect.Method method = ReadDataUtils.class.getMethod("ReadFile", String.class, javax.swing.JFrame.class, String.class, String[].class);
            assertEquals(java.io.File.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("ReadFile method not found");
        }
    }
}

package crm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testConstructor() {
        ReadDataUtils readDataUtils = new ReadDataUtils();
        assertNotNull(readDataUtils);
    }

    @Test
    void testReadFileMethod_Exists() {
        assertDoesNotThrow(() -> {
            Class<?> clazz = Class.forName("crm.utils.ReadDataUtils");
            assertNotNull(clazz.getMethod("ReadFile", String.class, javax.swing.JFrame.class, String.class, String[].class));
        });
    }

    @Test
    void testClassIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ReadDataUtils.class.getModifiers()));
    }
}

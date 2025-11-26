package crm.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReadDataUtilsTest {

    @Test
    public void testConstructor() {
        ReadDataUtils utils = new ReadDataUtils();
        assertNotNull(utils);
    }

    @Test
    public void testReadDataUtilsInstantiation() {
        ReadDataUtils instance = new ReadDataUtils();
        assertNotNull(instance);
    }

    @Test
    public void testReadDataUtilsIsNotNull() {
        ReadDataUtils utils1 = new ReadDataUtils();
        ReadDataUtils utils2 = new ReadDataUtils();
        assertNotNull(utils1);
        assertNotNull(utils2);
        assertNotSame(utils1, utils2);
    }

    @Test
    public void testReadFileMethodExists() {
        try {
            ReadDataUtils.class.getMethod("ReadFile", String.class, javax.swing.JFrame.class, String.class, String[].class);
        } catch (NoSuchMethodException e) {
            fail("ReadFile method should exist");
        }
    }
}

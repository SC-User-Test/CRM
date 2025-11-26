package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CrmApplicationTest {

    @Test
    public void testConstructor() {
        CrmApplication app = new CrmApplication();
        assertNotNull(app);
    }

    @Test
    public void testApplicationInstantiation() {
        CrmApplication application = new CrmApplication();
        assertNotNull(application);
    }

    @Test
    public void testMainMethodExists() {
        try {
            CrmApplication.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("Main method should exist");
        }
    }

    @Test
    public void testClassIsNotNull() {
        Class<CrmApplication> clazz = CrmApplication.class;
        assertNotNull(clazz);
    }
}

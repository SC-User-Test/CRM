package crm.service;

import crm.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void testUserServiceInterface() {
        // Assert that the interface exists and has the expected methods
        assertNotNull(UserService.class);
        assertTrue(UserService.class.isInterface());
    }

    @Test
    void testFindByUsernameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(UserService.class.getMethod("findByUsername", String.class));
    }

    @Test
    void testListAllUsersMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(UserService.class.getMethod("listAllUsers"));
    }

    @Test
    void testShowUserMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(UserService.class.getMethod("showUser", Long.class));
    }

    @Test
    void testSaveUserMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(UserService.class.getMethod("saveUser", User.class));
    }

    @Test
    void testEditUserMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(UserService.class.getMethod("editUser", User.class));
    }

    @Test
    void testDeleteUserMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(UserService.class.getMethod("deleteUser", User.class));
    }

    @Test
    void testUserServiceMethodCount() {
        // Assert that the interface has exactly 6 methods
        assertEquals(6, UserService.class.getDeclaredMethods().length);
    }
}

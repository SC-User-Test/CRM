package crm.service;

import crm.entity.User;

/**
 * Interface test - verifies the interface structure
 */
class UserServiceTest {

    @org.junit.jupiter.api.Test
    void userService_shouldHaveFindByUsernameMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            UserService.class.getMethod("findByUsername", String.class)
        );
    }

    @org.junit.jupiter.api.Test
    void userService_shouldHaveListAllUsersMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            UserService.class.getMethod("listAllUsers")
        );
    }

    @org.junit.jupiter.api.Test
    void userService_shouldHaveShowUserMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            UserService.class.getMethod("showUser", Long.class)
        );
    }

    @org.junit.jupiter.api.Test
    void userService_shouldHaveSaveUserMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            UserService.class.getMethod("saveUser", User.class)
        );
    }

    @org.junit.jupiter.api.Test
    void userService_shouldHaveEditUserMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            UserService.class.getMethod("editUser", User.class)
        );
    }

    @org.junit.jupiter.api.Test
    void userService_shouldHaveDeleteUserMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            UserService.class.getMethod("deleteUser", User.class)
        );
    }
}

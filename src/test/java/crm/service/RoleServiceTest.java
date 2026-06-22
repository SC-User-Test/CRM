package crm.service;

/**
 * Interface test - verifies the interface structure
 */
class RoleServiceTest {

    @org.junit.jupiter.api.Test
    void roleService_shouldHaveListAllRolesMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            RoleService.class.getMethod("listAllRoles")
        );
    }
}

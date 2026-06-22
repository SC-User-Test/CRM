package crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest {

    @Test
    void testRoleRepositoryInterface() {
        // Assert that the interface exists and extends CrudRepository
        assertNotNull(RoleRepository.class);
        assertTrue(RoleRepository.class.isInterface());
        assertTrue(CrudRepository.class.isAssignableFrom(RoleRepository.class));
    }
}

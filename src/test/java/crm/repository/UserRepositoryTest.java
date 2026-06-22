package crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void testUserRepositoryInterface() {
        // Assert that the interface exists and extends CrudRepository
        assertNotNull(UserRepository.class);
        assertTrue(UserRepository.class.isInterface());
        assertTrue(CrudRepository.class.isAssignableFrom(UserRepository.class));
    }
}

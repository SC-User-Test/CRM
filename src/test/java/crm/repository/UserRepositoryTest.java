package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void testUserRepositoryInterfaceExists() {
        assertNotNull(UserRepository.class);
    }

    @Test
    void testFindByUsernameMethodExists() {
        assertDoesNotThrow(() -> {
            UserRepository.class.getDeclaredMethod("findByUsername", String.class);
        });
    }

    @Test
    void testFindAllByEnabledMethodExists() {
        assertDoesNotThrow(() -> {
            UserRepository.class.getDeclaredMethod("findAllByEnabled", int.class);
        });
    }

    @Test
    void testUserRepositoryExtendsJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class.isAssignableFrom(UserRepository.class));
    }
}

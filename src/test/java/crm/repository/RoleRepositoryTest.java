package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepositoryTest {

    @Test
    void testRoleRepositoryInterfaceExists() {
        assertNotNull(RoleRepository.class);
    }

    @Test
    void testFindByNameMethodExists() {
        assertDoesNotThrow(() -> {
            RoleRepository.class.getDeclaredMethod("findByName", String.class);
        });
    }

    @Test
    void testRoleRepositoryExtendsJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class.isAssignableFrom(RoleRepository.class));
    }
}

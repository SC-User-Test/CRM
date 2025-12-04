package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    @Test
    void testCategoryRepositoryInterfaceExists() {
        assertNotNull(CategoryRepository.class);
    }

    @Test
    void testFindByNameMethodExists() {
        assertDoesNotThrow(() -> {
            CategoryRepository.class.getDeclaredMethod("findByName", String.class);
        });
    }

    @Test
    void testCategoryRepositoryExtendsJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class.isAssignableFrom(CategoryRepository.class));
    }
}

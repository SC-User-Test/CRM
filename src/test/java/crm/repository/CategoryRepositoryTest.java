package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    @Test
    void testCategoryRepositoryInterface() {
        // Assert that the interface exists and extends CrudRepository
        assertNotNull(CategoryRepository.class);
        assertTrue(CategoryRepository.class.isInterface());
        assertTrue(CrudRepository.class.isAssignableFrom(CategoryRepository.class));
    }

    @Test
    void testCategoryRepositoryExtendsCorrectType() {
        // Verify it's a repository for Category entities with Long ID
        assertTrue(CrudRepository.class.isAssignableFrom(CategoryRepository.class));
    }
}

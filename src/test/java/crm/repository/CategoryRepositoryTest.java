package crm.repository;

import crm.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByName_withValidName_shouldReturnCategory() {
        // Arrange
        Category category = new Category();
        category.setName("VIP");
        categoryRepository.save(category);

        // Act
        Category result = categoryRepository.findByName("VIP");

        // Assert
        assertNotNull(result);
        assertEquals("VIP", result.getName());
    }

    @Test
    void findByName_withInvalidName_shouldReturnNull() {
        // Act
        Category result = categoryRepository.findByName("INVALID");

        // Assert
        assertNull(result);
    }

    @Test
    void save_shouldPersistCategory() {
        // Arrange
        Category category = new Category();
        category.setName("Premium");

        // Act
        Category saved = categoryRepository.save(category);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void categoryRepository_shouldBeInjected() {
        // Assert
        assertNotNull(categoryRepository);
    }
}

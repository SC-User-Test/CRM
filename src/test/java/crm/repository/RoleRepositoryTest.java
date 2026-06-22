package crm.repository;

import crm.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByName_withValidName_shouldReturnRole() {
        // Arrange
        Role role = new Role();
        role.setName("ROLE_TEST");
        roleRepository.save(role);

        // Act
        Role result = roleRepository.findByName("ROLE_TEST");

        // Assert
        assertNotNull(result);
        assertEquals("ROLE_TEST", result.getName());
    }

    @Test
    void findByName_withInvalidName_shouldReturnNull() {
        // Act
        Role result = roleRepository.findByName("INVALID_ROLE");

        // Assert
        assertNull(result);
    }

    @Test
    void save_shouldPersistRole() {
        // Arrange
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        // Act
        Role saved = roleRepository.save(role);

        // Assert
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void roleRepository_shouldBeInjected() {
        // Assert
        assertNotNull(roleRepository);
    }
}

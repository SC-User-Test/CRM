package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    @Test
    void testCustomerRepositoryInterfaceExists() {
        assertNotNull(CustomerRepository.class);
    }

    @Test
    void testGetMaxIdMethodExists() {
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getDeclaredMethod("getMaxId");
        });
    }

    @Test
    void testFindAllByEnabledMethodExists() {
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getDeclaredMethod("findAllByEnabled", int.class);
        });
    }

    @Test
    void testFindOneByEnabledAndNameMethodExists() {
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getDeclaredMethod("findOneByEnabledAndName", int.class, String.class);
        });
    }

    @Test
    void testFindByEmailMethodExists() {
        assertDoesNotThrow(() -> {
            CustomerRepository.class.getDeclaredMethod("findByEmail", String.class);
        });
    }

    @Test
    void testCustomerRepositoryExtendsJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class.isAssignableFrom(CustomerRepository.class));
    }
}

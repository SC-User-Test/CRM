package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContractRepositoryTest {

    @Test
    void testContractRepositoryInterfaceExists() {
        assertNotNull(ContractRepository.class);
    }

    @Test
    void testFindByNameMethodExists() {
        assertDoesNotThrow(() -> {
            ContractRepository.class.getDeclaredMethod("findByName", String.class);
        });
    }

    @Test
    void testFindAllByValueLessThanEqualMethodExists() {
        assertDoesNotThrow(() -> {
            ContractRepository.class.getDeclaredMethod("findAllByValueLessThanEqual", java.math.BigDecimal.class);
        });
    }

    @Test
    void testFindAllByBeginDateMethodExists() {
        assertDoesNotThrow(() -> {
            ContractRepository.class.getDeclaredMethod("findAllByBeginDate", java.time.LocalDate.class);
        });
    }

    @Test
    void testFindAllByStatusMethodExists() {
        assertDoesNotThrow(() -> {
            ContractRepository.class.getDeclaredMethod("findAllByStatus", crm.entity.Status.class);
        });
    }

    @Test
    void testContractRepositoryExtendsJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class.isAssignableFrom(ContractRepository.class));
    }
}

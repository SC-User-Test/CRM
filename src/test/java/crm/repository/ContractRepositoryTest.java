package crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.*;

class ContractRepositoryTest {

    @Test
    void testContractRepositoryInterface() {
        // Assert that the interface exists and extends CrudRepository
        assertNotNull(ContractRepository.class);
        assertTrue(ContractRepository.class.isInterface());
        assertTrue(CrudRepository.class.isAssignableFrom(ContractRepository.class));
    }
}

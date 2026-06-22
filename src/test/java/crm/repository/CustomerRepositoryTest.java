package crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.*;

class CustomerRepositoryTest {

    @Test
    void testCustomerRepositoryInterface() {
        // Assert that the interface exists and extends CrudRepository
        assertNotNull(CustomerRepository.class);
        assertTrue(CustomerRepository.class.isInterface());
        assertTrue(CrudRepository.class.isAssignableFrom(CustomerRepository.class));
    }
}

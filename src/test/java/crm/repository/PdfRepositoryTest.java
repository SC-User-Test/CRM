package crm.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.repository.CrudRepository;

import static org.junit.jupiter.api.Assertions.*;

class PdfRepositoryTest {

    @Test
    void testPdfRepositoryInterface() {
        // Assert that the interface exists and extends CrudRepository
        assertNotNull(PdfRepository.class);
        assertTrue(PdfRepository.class.isInterface());
        assertTrue(CrudRepository.class.isAssignableFrom(PdfRepository.class));
    }
}

package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfRepositoryTest {

    @Test
    void testPdfRepositoryInterfaceExists() {
        assertNotNull(PdfRepository.class);
    }

    @Test
    void testFindByNameMethodExists() {
        assertDoesNotThrow(() -> {
            PdfRepository.class.getDeclaredMethod("findByName", String.class);
        });
    }

    @Test
    void testPdfRepositoryExtendsJpaRepository() {
        assertTrue(org.springframework.data.jpa.repository.JpaRepository.class.isAssignableFrom(PdfRepository.class));
    }
}

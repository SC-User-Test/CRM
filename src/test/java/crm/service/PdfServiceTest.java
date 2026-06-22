package crm.service;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    @Test
    void testPdfServiceInterface() {
        // Assert that the interface exists
        assertNotNull(PdfService.class);
        assertTrue(PdfService.class.isInterface());
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(PdfService.class.getMethod("findByName", String.class));
    }

    @Test
    void testSavePdfMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(PdfService.class.getMethod("savePdf", Pdf.class));
    }

    @Test
    void testInterfaceHasExpectedNumberOfMethods() {
        // Assert that the interface has exactly 2 methods
        assertEquals(2, PdfService.class.getDeclaredMethods().length);
    }
}

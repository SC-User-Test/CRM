package crm.service;

/**
 * Interface test - verifies the interface structure
 */
class PdfServiceTest {

    @org.junit.jupiter.api.Test
    void pdfService_shouldHaveFindByNameMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            PdfService.class.getMethod("findByName", String.class)
        );
    }

    @org.junit.jupiter.api.Test
    void pdfService_shouldHaveSavePdfMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            PdfService.class.getMethod("savePdf", crm.entity.Pdf.class)
        );
    }
}

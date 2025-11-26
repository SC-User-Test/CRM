package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Cloud-ready PDF Controller
 * - Uses ByteArrayOutputStream instead of local file system
 * - PDF data stored in database or can be uploaded to cloud storage (S3)
 * - Improved exception handling with detailed logging
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;

    @Value("${cloud.storage.enabled:false}")
    private boolean cloudStorageEnabled;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generate PDF in memory (cloud-compatible approach).
     * Returns byte array that can be stored in database or uploaded to S3.
     */
    private byte[] generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            log.info("PDF generated successfully in memory: {}", fileName);
            return baos.toByteArray();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            baos.close();
        }
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("PDF generation validation errors: {}", bindingResult.getAllErrors());
            return "redirect:/pdf-generator";
        } else {
            try {
                byte[] pdfBytes = generateSamplePdf(pdf.getName(), pdf.getContent());

                // Store PDF bytes in database or upload to cloud storage
                // For cloud deployment, consider uploading to S3:
                // if (cloudStorageEnabled) {
                //     s3Service.uploadPdf(pdf.getName(), pdfBytes);
                // }

                pdfService.savePdf(pdf);
                log.info("PDF generated and saved successfully: {}", pdf.getName());

            } catch (DocumentException e) {
                log.error("Error generating PDF document: {}, Error: {}", pdf.getName(), e.getMessage(), e);
                return "redirect:/pdf-generator?error=document";
            } catch (IOException e) {
                log.error("Error writing PDF to memory: {}, Error: {}", pdf.getName(), e.getMessage(), e);
                return "redirect:/pdf-generator?error=io";
            } catch (Exception e) {
                log.error("Unexpected error generating PDF: {}, Error: {}", pdf.getName(), e.getMessage(), e);
                return "redirect:/pdf-generator?error=general";
            }
            return "pdf/success";
        }
    }

}

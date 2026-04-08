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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Cloud-ready PDF Controller.
 * Uses environment-configured persistent storage instead of local file system.
 * Compatible with AWS EFS, Azure Files, or GCP Filestore mounted volumes.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    /**
     * PDF storage directory from environment variable.
     * For cloud deployments, this should be a mounted persistent volume:
     * - AWS: /mnt/efs/pdfs (EFS mount)
     * - Azure: /mnt/azure/pdfs (Azure Files mount)
     * - GCP: /mnt/gcs/pdfs (GCS FUSE mount)
     * 
     * Set via environment variable: PDF_STORAGE_PATH
     */
    @Value("${pdf.storage.path:#{systemEnvironment['PDF_STORAGE_PATH'] ?: '/tmp/pdfs'}}")
    private String pdfStoragePath;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates PDF and stores it in cloud-compatible persistent storage.
     * Creates storage directory if it doesn't exist.
     * 
     * @param fileName Name of the PDF file
     * @param text Content to write to PDF
     * @throws IOException if storage directory cannot be created
     * @throws DocumentException if PDF generation fails
     */
    private void generateSamplePdf(String fileName, String text) throws IOException, DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Ensure storage directory exists
        Path storagePath = Paths.get(pdfStoragePath);
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
                log.info("Created PDF storage directory: {}", pdfStoragePath);
            } catch (IOException e) {
                log.error("Failed to create PDF storage directory: {}", pdfStoragePath, e);
                throw new IOException("Cannot create PDF storage directory", e);
            }
        }

        // Generate PDF in persistent storage location
        String fullPath = Paths.get(pdfStoragePath, fileName).toString();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            log.info("PDF generated successfully: {}", fullPath);
        } finally {
            document.close();
        }
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        } else {
            try {
                generateSamplePdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
                model.addAttribute("message", "PDF generated successfully");
                log.info("PDF saved to database: {}", pdf.getName());
            } catch (FileNotFoundException e) {
                log.error("File not found during PDF generation", e);
                model.addAttribute("error", "Failed to generate PDF: Storage location not accessible");
                return "pdf/generator";
            } catch (DocumentException e) {
                log.error("Document exception during PDF generation", e);
                model.addAttribute("error", "Failed to generate PDF: Document error");
                return "pdf/generator";
            } catch (IOException e) {
                log.error("IO exception during PDF generation", e);
                model.addAttribute("error", "Failed to generate PDF: Storage error");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}

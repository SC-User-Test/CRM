package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import crm.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Cloud-ready PDF Controller that uses Azure Blob Storage for PDF persistence.
 * Replaces local file system writes with cloud storage operations.
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final StorageService storageService;

    @Autowired
    public PdfController(PdfService pdfService, StorageService storageService) {
        this.pdfService = pdfService;
        this.storageService = storageService;
    }

    /**
     * Generates PDF content in memory and uploads to Azure Blob Storage.
     * This approach ensures data durability across container restarts and scaling events.
     * 
     * @param fileName the name of the PDF file
     * @param text the content to include in the PDF
     * @return the blob URL where the PDF was stored
     * @throws DocumentException if PDF generation fails
     * @throws IOException if upload to Azure Blob Storage fails
     */
    private String generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Generate PDF in memory using ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            
            // Upload to Azure Blob Storage
            byte[] pdfBytes = outputStream.toByteArray();
            String blobUrl = storageService.uploadFile(fileName, pdfBytes, "application/pdf");
            
            log.info("PDF generated and uploaded to Azure Blob Storage: {}", blobUrl);
            return blobUrl;
            
        } finally {
            outputStream.close();
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
                String blobUrl = generateSamplePdf(pdf.getName(), pdf.getContent());
                
                // Store blob URL in the entity for future reference
                pdf.setStorageUrl(blobUrl);
                pdfService.savePdf(pdf);
                
                model.addAttribute("blobUrl", blobUrl);
                log.info("PDF successfully generated and saved: {}", pdf.getName());
                
            } catch (DocumentException e) {
                log.error("Error generating PDF document: {}", e.getMessage(), e);
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (IOException e) {
                log.error("Error uploading PDF to Azure Blob Storage: {}", e.getMessage(), e);
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}

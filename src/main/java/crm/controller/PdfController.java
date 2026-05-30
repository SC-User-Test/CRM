package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import crm.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.time.Instant;

/**
 * PDF Controller with cloud-native S3 storage.
 * Replaces local file writes with Amazon S3 for durable storage.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    
    @Autowired
    private S3StorageService s3StorageService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generate PDF and store in Amazon S3 instead of local file system
     * @param fileName Name of the PDF file
     * @param text Content to be written in the PDF
     * @return S3 object key where the PDF is stored
     */
    private String generateSamplePdfToS3(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Generate PDF in memory
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();
        
        // Upload to S3 with timestamp to ensure uniqueness
        String s3Key = "pdfs/" + Instant.now().toEpochMilli() + "-" + fileName;
        String s3Url = s3StorageService.uploadFile(s3Key, outputStream.toByteArray(), "application/pdf");
        
        log.info("PDF generated and uploaded to S3: {}", s3Url);
        return s3Key;
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
                String s3Key = generateSamplePdfToS3(pdf.getName(), pdf.getContent());
                
                // Store S3 key reference in the database
                pdf.setName(s3Key); // Store S3 key instead of local file name
                pdfService.savePdf(pdf);
                
                model.addAttribute("s3Key", s3Key);
                model.addAttribute("message", "PDF successfully generated and stored in S3");
                log.info("PDF saved to database with S3 reference: {}", s3Key);
            } catch (DocumentException e) {
                log.error("Failed to generate PDF document: {}", e.getMessage());
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (Exception e) {
                log.error("Failed to upload PDF to S3: {}", e.getMessage());
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}

package crm.controller;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
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

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    
    @Value("${gcs.bucket.name:default-bucket}")
    private String bucketName;
    
    private final Storage storage;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
        // Initialize GCS client
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    /**
     * Generate PDF and store in Google Cloud Storage instead of local filesystem
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Create PDF in memory using ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            
            // Upload to Google Cloud Storage
            byte[] pdfBytes = outputStream.toByteArray();
            BlobId blobId = BlobId.of(bucketName, "pdfs/" + fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/pdf")
                    .build();
            
            storage.create(blobInfo, pdfBytes);
            log.info("PDF successfully uploaded to GCS: {}", fileName);
            
        } catch (DocumentException e) {
            log.error("Error creating PDF document", e);
            throw e;
        } catch (Exception e) {
            log.error("Error uploading PDF to GCS", e);
            throw new IOException("Failed to upload PDF to Google Cloud Storage", e);
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
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        } else {
            try {
                generateSamplePdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
            } catch (DocumentException e) {
                log.error("Document creation error", e);
            } catch (IOException e) {
                log.error("IO error during PDF generation", e);
            }
            return "pdf/success";
        }
    }

}

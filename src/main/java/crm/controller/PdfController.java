package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class PdfController {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-pdf-bucket");
    private static final String S3_PDF_PREFIX = System.getenv().getOrDefault("S3_PDF_PREFIX", "pdfs/");

    private PdfService pdfService;
    
    @Autowired(required = false)
    private S3Client s3Client;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF and stores it in Amazon S3 instead of local file system.
     * This ensures data durability and availability in cloud environments.
     */
    private void generateSamplePdfToS3(String fileName, String text) throws IOException, DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Generate PDF in memory
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            
            // Upload to S3
            byte[] pdfBytes = outputStream.toByteArray();
            String s3Key = S3_PDF_PREFIX + fileName;
            
            if (s3Client != null) {
                uploadToS3(s3Key, pdfBytes);
                log.info("PDF successfully uploaded to S3: {}/{}", S3_BUCKET_NAME, s3Key);
            } else {
                log.warn("S3Client not configured. PDF generated but not uploaded to S3.");
            }
        } catch (S3Exception e) {
            log.error("Failed to upload PDF to S3: {}", e.getMessage(), e);
            throw new IOException("Failed to upload PDF to S3", e);
        } finally {
            outputStream.close();
        }
    }

    private void uploadToS3(String s3Key, byte[] content) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(S3_BUCKET_NAME)
                .key(s3Key)
                .contentType("application/pdf")
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
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
                generateSamplePdfToS3(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
            } catch (IOException e) {
                log.error("Failed to generate or upload PDF", e);
            } catch (DocumentException e) {
                log.error("Document generation error", e);
            }
            return "pdf/success";
        }
    }

}

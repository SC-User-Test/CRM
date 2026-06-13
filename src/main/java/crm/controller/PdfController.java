package crm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Cloud-ready PDF Controller that uses Azure Blob Storage for persistent storage.
 * Replaces local file system writes with Azure Blob Storage operations.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${azure.storage.connection-string:#{null}}")
    private String connectionString;

    @Value("${azure.storage.container-name:pdf-files}")
    private String containerName;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF and uploads it to Azure Blob Storage.
     * 
     * @param fileName The name of the PDF file
     * @param text The content to include in the PDF
     * @return The blob URL of the uploaded PDF
     * @throws DocumentException if PDF generation fails
     */
    private String generateAndUploadPdf(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        if (connectionString == null || connectionString.isEmpty()) {
            log.error("Azure Storage connection string is not configured");
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set azure.storage.connection-string in application.properties");
        }

        try {
            // Generate PDF in memory
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload to Azure Blob Storage
            byte[] pdfBytes = outputStream.toByteArray();
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
                log.info("Created blob container: {}", containerName);
            }

            BlobClient blobClient = containerClient.getBlobClient(fileName);
            blobClient.upload(new ByteArrayInputStream(pdfBytes), pdfBytes.length, true);

            String blobUrl = blobClient.getBlobUrl();
            log.info("PDF uploaded successfully to Azure Blob Storage: {}", blobUrl);
            return blobUrl;

        } catch (Exception e) {
            log.error("Failed to upload PDF to Azure Blob Storage", e);
            throw new RuntimeException("Failed to upload PDF to Azure Blob Storage", e);
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
                String blobUrl = generateAndUploadPdf(pdf.getName(), pdf.getContent());
                pdf.setName(blobUrl); // Store the blob URL instead of local file path
                pdfService.savePdf(pdf);
                model.addAttribute("blobUrl", blobUrl);
                log.info("PDF generated and saved successfully: {}", pdf.getName());
            } catch (DocumentException e) {
                log.error("Document generation error", e);
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (Exception e) {
                log.error("Failed to upload PDF to cloud storage", e);
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}

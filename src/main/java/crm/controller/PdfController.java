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

    @Value("${azure.storage.container-name:crm-files}")
    private String containerName;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF and stores it in Azure Blob Storage instead of local file system.
     * 
     * @param fileName The name of the PDF file
     * @param text The content to write to the PDF
     * @return The blob URL of the uploaded PDF
     */
    private String generateSamplePdfToAzureBlob(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        if (connectionString == null || connectionString.isEmpty()) {
            throw new IllegalStateException("Azure Storage connection string is not configured. " +
                    "Please set azure.storage.connection-string in application.properties");
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

            // Upload to Azure Blob Storage
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
                log.info("Created Azure Blob Storage container: {}", containerName);
            }

            BlobClient blobClient = containerClient.getBlobClient(fileName);
            
            // Upload the PDF content
            byte[] pdfBytes = outputStream.toByteArray();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
            blobClient.upload(inputStream, pdfBytes.length, true);
            
            log.info("Successfully uploaded PDF to Azure Blob Storage: {}", fileName);
            return blobClient.getBlobUrl();
            
        } catch (Exception e) {
            log.error("Failed to generate and upload PDF to Azure Blob Storage", e);
            throw new RuntimeException("Failed to generate PDF", e);
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
                String blobUrl = generateSamplePdfToAzureBlob(pdf.getName(), pdf.getContent());
                pdf.setName(blobUrl); // Store the blob URL instead of local file path
                pdfService.savePdf(pdf);
                model.addAttribute("blobUrl", blobUrl);
                log.info("PDF generated and stored in Azure Blob Storage: {}", blobUrl);
            } catch (DocumentException e) {
                log.error("Document generation error", e);
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (Exception e) {
                log.error("Azure Blob Storage error", e);
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }
}

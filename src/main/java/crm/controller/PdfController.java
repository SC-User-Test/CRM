package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    private byte[] generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);

            log.info("PDF generated successfully in memory for file: {} at {}", fileName, LocalDateTime.now());

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        return outputStream.toByteArray();
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public ResponseEntity<Resource> generatePdf(@Valid Pdf pdf, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("PDF generation failed due to validation errors for file: {}", pdf.getName());
            return ResponseEntity.badRequest().build();
        }

        try {
            byte[] pdfBytes = generateSamplePdf(pdf.getName(), pdf.getContent());
            pdfService.savePdf(pdf);

            String fileName = pdf.getName();
            if (!fileName.endsWith(".pdf")) {
                fileName += ".pdf";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
            headers.setContentType(MediaType.APPLICATION_PDF);

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            log.info("PDF generated and returned successfully for file: {} at {}", fileName, LocalDateTime.now());
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(resource);

        } catch (DocumentException e) {
            log.error("PDF document generation failed for file: {} with error: {}", pdf.getName(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        } catch (IOException e) {
            log.error("IO error during PDF generation for file: {} with error: {}", pdf.getName(), e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}

package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.OutputStream;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    private void generateSamplePdf(String fileName, String text, OutputStream outputStream) throws DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        } else {
            try {
                String fileName = pdf.getName();
                if (!fileName.endsWith(".pdf")) {
                    fileName += ".pdf";
                }
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
                generateSamplePdf(pdf.getName(), pdf.getContent(), response.getOutputStream());
                pdfService.savePdf(pdf);
                return null;
            } catch (DocumentException e) {
                log.info("Document Exception: " + e.getMessage());
                return "redirect:/pdf-generator?error";
            } catch (Exception e) {
                log.info("Error: " + e.getMessage());
                return "redirect:/pdf-generator?error";
            }
        }
    }

}

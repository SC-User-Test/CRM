package crm.controller;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class CsvUploadController {

    @GetMapping("/csv-upload")
    public String csvUploadPage() {
        return "csv/upload";
    }

    @PostMapping("/csv-upload")
    public String handleCsvUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            return "csv/upload";
        }

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
            String[] line;
            List<Object[]> data = new ArrayList<>();

            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
            reader.close();

            model.addAttribute("message", "CSV file uploaded successfully. Processed " + data.size() + " rows.");
            model.addAttribute("data", data);
            return "csv/success";
        } catch (Exception e) {
            log.error("Error processing CSV file", e);
            model.addAttribute("message", "Error processing CSV file: " + e.getMessage());
            return "csv/upload";
        }
    }
}

package crm.controller;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/csv")
@Slf4j
public class CsvUploadController {

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No file provided"));
        }

        if (!file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only CSV files are allowed"));
        }

        try (CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream())))) {
            List<String[]> data = new ArrayList<>();
            String[] line;
            int rowCount = 0;

            while ((line = reader.readNext()) != null) {
                data.add(line);
                rowCount++;
                // Example processing: filter rows with "QUICK SUB" in second column
                if (line.length > 1 && "QUICK SUB".equals(line[1])) {
                    log.info("Found QUICK SUB row: {}", String.join(", ", line));
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("filename", file.getOriginalFilename());
            response.put("rowCount", rowCount);
            response.put("message", "CSV file processed successfully");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("Error processing CSV file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to process CSV file: " + e.getMessage()));
        }
    }

    @GetMapping("/upload-form")
    public ResponseEntity<String> getUploadForm() {
        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>CSV Upload</title>
                </head>
                <body>
                    <h2>Upload CSV File</h2>
                    <form action="/api/csv/upload" method="post" enctype="multipart/form-data">
                        <input type="file" name="file" accept=".csv" required>
                        <button type="submit">Upload</button>
                    </form>
                </body>
                </html>
                """;
        return ResponseEntity.ok().header("Content-Type", "text/html").body(html);
    }
}

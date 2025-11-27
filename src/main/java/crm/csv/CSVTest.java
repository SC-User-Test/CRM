package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CSVTest {

    public static void main(String[] args) {
        File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
        log.info("Selected CSV file: {}", document != null ? document.getName() : "No file selected");

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(document));
            String[] line;
            while ((line = reader.readNext()) != null) {
                log.debug("Processing CSV line: {} - {}", line.length > 1 ? line[1] : "N/A",
                         line.length > 2 ? line[2] : "N/A");
                data.add(line);
                if(line.length > 1 && "QUICK SUB".equals(line[1])){
                    log.info("Found QUICK SUB record: {} - {} - {}",
                            line.length > 0 ? line[0] : "N/A",
                            line.length > 1 ? line[1] : "N/A",
                            line.length > 2 ? line[2] : "N/A");
                }
            }
            log.info("Successfully processed {} CSV records", data.size());
        } catch (IOException e) {
            log.error("Failed to process CSV file: {}", e.getMessage(), e);
        }
    }

}

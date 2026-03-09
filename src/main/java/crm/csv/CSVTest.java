package crm.csv;

import com.opencsv.CSVReader;
import crm.utils.ReadDataUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated This class uses GUI-based file selection which is incompatible with containerized
 * environments. CSV import functionality has been migrated to CsvUploadController which provides
 * a REST API endpoint for file uploads (/api/csv/upload).
 *
 * This class should not be used in production. For CSV processing in containers, use:
 * POST /api/csv/upload with multipart/form-data containing the CSV file.
 */
@Deprecated
public class CSVTest {

    /**
     * @deprecated This main method uses JFileChooser which requires a graphical display.
     * It will fail with HeadlessException in container environments.
     * Use CsvUploadController REST API instead.
     */
    @Deprecated
    public static void main(String[] args) {
        System.err.println("WARNING: CSVTest is deprecated and incompatible with container environments.");
        System.err.println("Use CsvUploadController REST API instead: POST /api/csv/upload");
        System.err.println("This class will be removed in a future version.");

        // Original implementation kept for backward compatibility only
        // Will fail in headless environments
        File document = ReadDataUtils.ReadFile("Select CSV file", null, "Only CSV Files", "csv");
//        System.out.println(document.getName());

        CSVReader reader;
        List<Object[]> data = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(document));
            String[] line;
            while ((line = reader.readNext()) != null) {
//                System.out.println(line[1] + "\t" + line[2]);
                data.add(line);
                if(line[1].equals("QUICK SUB")){
                    System.out.println(line[0] + "\t" + line[1] + "\t" + line[2]);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		/*System.out.println(data.get(0)[1] + "\t" + data.get(0)[2]);
		System.out.println(data.get(1)[1] + "\t" + data.get(1)[2]);*/
    }

}

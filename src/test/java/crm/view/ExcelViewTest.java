package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelViewTest {

    private ExcelView excelView;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        excelView = new ExcelView();
    }

    @Test
    void testConstructor() {
        assertNotNull(excelView);
    }

    @Test
    void testBuildExcelDocumentWithValidUsers() throws Exception {
        Map<String, Object> model = new HashMap<>();
        List<User> users = new ArrayList<>();

        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("password123")
                .enabled(1)
                .role(role)
                .build();

        users.add(user);
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        excelView.buildExcelDocument(model, workbook, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"");
        assertNotNull(workbook.getSheet("User Detail"));
        assertEquals(1, workbook.getNumberOfSheets());
    }

    @Test
    void testBuildExcelDocumentWithMultipleUsers() throws Exception {
        Map<String, Object> model = new HashMap<>();
        List<User> users = new ArrayList<>();

        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("USER");

        User user1 = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("password123")
                .enabled(1)
                .role(role1)
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .username("janesmith")
                .email("jane@example.com")
                .password("password456")
                .enabled(0)
                .role(role2)
                .build();

        users.add(user1);
        users.add(user2);
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        excelView.buildExcelDocument(model, workbook, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"");
        assertNotNull(workbook.getSheet("User Detail"));
    }

    @Test
    void testBuildExcelDocumentCreatesSheetWithCorrectName() throws Exception {
        Map<String, Object> model = new HashMap<>();
        List<User> users = new ArrayList<>();

        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@example.com")
                .password("test123")
                .enabled(1)
                .role(role)
                .build();

        users.add(user);
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        excelView.buildExcelDocument(model, workbook, request, response);

        assertEquals("User Detail", workbook.getSheetName(0));
    }

    @Test
    void testBuildExcelDocumentSetsResponseHeader() throws Exception {
        Map<String, Object> model = new HashMap<>();
        List<User> users = new ArrayList<>();

        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        User user = User.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .email("test@example.com")
                .password("test123")
                .enabled(1)
                .role(role)
                .build();

        users.add(user);
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        excelView.buildExcelDocument(model, workbook, request, response);

        verify(response, times(1)).setHeader(eq("Content-Disposition"), anyString());
    }
}

package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    void testBuildExcelDocumentWithValidUsers() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

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

        List<User> users = new ArrayList<>();
        users.add(user);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        excelView.buildExcelDocument(model, workbook, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"");
        assertNotNull(workbook.getSheet("User Detail"));
        assertEquals(1, workbook.getNumberOfSheets());

        workbook.close();
    }

    @Test
    void testBuildExcelDocumentWithMultipleUsers() throws Exception {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        User user1 = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .email("alice@example.com")
                .password("pass1")
                .enabled(1)
                .role(role1)
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Jones")
                .username("bob")
                .email("bob@example.com")
                .password("pass2")
                .enabled(1)
                .role(role2)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        assertDoesNotThrow(() -> excelView.buildExcelDocument(model, workbook, request, response));
        assertNotNull(workbook.getSheet("User Detail"));

        workbook.close();
    }

    @Test
    void testBuildExcelDocumentCreatesCorrectSheet() throws Exception {
        Role role = new Role();
        role.setId(3);
        role.setName("ROLE_MANAGER");

        User user = User.builder()
                .id(5L)
                .firstName("Charlie")
                .lastName("Brown")
                .username("charlie")
                .email("charlie@example.com")
                .password("password")
                .enabled(0)
                .role(role)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        Workbook workbook = new HSSFWorkbook();

        excelView.buildExcelDocument(model, workbook, request, response);

        assertNotNull(workbook.getSheet("User Detail"));
        assertNotNull(workbook.getSheet("User Detail").getRow(0));

        workbook.close();
    }
}

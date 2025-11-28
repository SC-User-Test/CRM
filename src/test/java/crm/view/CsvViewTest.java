package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvViewTest {

    private CsvView csvView;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        csvView = new CsvView();
    }

    @Test
    void testConstructor() {
        assertNotNull(csvView);
        assertEquals("text/csv", csvView.getContentType());
    }

    @Test
    void testBuildCsvDocumentWithValidUsers() throws Exception {
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

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        csvView.buildCsvDocument(model, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
        verify(response).getWriter();
    }

    @Test
    void testBuildCsvDocumentWithMultipleUsers() throws Exception {
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

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        csvView.buildCsvDocument(model, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
    }

    @Test
    void testBuildCsvDocumentSetsResponseHeader() throws Exception {
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

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        csvView.buildCsvDocument(model, request, response);

        verify(response, times(1)).setHeader(eq("Content-Disposition"), anyString());
    }
}

package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    void testBuildCsvDocumentWithValidUsers() throws Exception {
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

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        csvView.buildCsvDocument(model, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
        verify(response).getWriter();
    }

    @Test
    void testBuildCsvDocumentWithMultipleUsers() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        User user1 = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .username("alice")
                .email("alice@example.com")
                .password("pass1")
                .enabled(1)
                .role(role)
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Jones")
                .username("bob")
                .email("bob@example.com")
                .password("pass2")
                .enabled(0)
                .role(role)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        assertDoesNotThrow(() -> csvView.buildCsvDocument(model, request, response));
    }

    @Test
    void testBuildCsvDocumentVerifiesHeader() throws Exception {
        Role role = new Role();
        role.setId(2);
        role.setName("ROLE_ADMIN");

        User user = User.builder()
                .id(3L)
                .firstName("Charlie")
                .lastName("Brown")
                .username("charlie")
                .email("charlie@example.com")
                .password("password")
                .enabled(1)
                .role(role)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        csvView.buildCsvDocument(model, request, response);

        verify(response).setHeader(eq("Content-Disposition"), contains("attachment"));
        verify(response).setHeader(eq("Content-Disposition"), contains(".csv"));
    }
}

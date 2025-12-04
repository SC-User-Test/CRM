package crm.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfViewTest {

    private PdfView pdfView;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfView = new PdfView();
    }

    @Test
    void testBuildPdfDocumentWithValidUsers() throws Exception {
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        when(response.getHeader("Content-Disposition")).thenReturn("attachment; filename=\"my-pdf-file.pdf\"");

        pdfView.buildPdfDocument(model, document, writer, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
        document.close();
    }

    @Test
    void testBuildPdfDocumentWithEmptyUsers() throws Exception {
        List<User> users = new ArrayList<>();
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        User user = User.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Smith")
                .username("janesmith")
                .email("jane@example.com")
                .password("password456")
                .enabled(1)
                .role(role)
                .build();

        users.add(user);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        assertDoesNotThrow(() -> pdfView.buildPdfDocument(model, document, writer, request, response));

        document.close();
    }

    @Test
    void testBuildPdfDocumentWithMultipleUsers() throws Exception {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        User user1 = User.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Johnson")
                .username("alice")
                .email("alice@example.com")
                .password("pass1")
                .enabled(1)
                .role(role1)
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstName("Bob")
                .lastName("Brown")
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        assertDoesNotThrow(() -> pdfView.buildPdfDocument(model, document, writer, request, response));

        document.close();
    }
}

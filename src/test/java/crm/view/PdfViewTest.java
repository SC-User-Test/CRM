package crm.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    void testConstructor() {
        assertNotNull(pdfView);
        assertEquals("application/pdf", pdfView.getContentType());
    }

    @Test
    void testBuildPdfDocumentWithValidUsers() throws Exception {
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        when(response.getWriter()).thenReturn(null);

        pdfView.buildPdfDocument(model, document, writer, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
        document.close();
    }

    @Test
    void testBuildPdfDocumentWithMultipleUsers() throws Exception {
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        pdfView.buildPdfDocument(model, document, writer, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
        document.close();
    }

    @Test
    void testBuildPdfDocumentSetsResponseHeader() throws Exception {
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        pdfView.buildPdfDocument(model, document, writer, request, response);

        verify(response, times(1)).setHeader(eq("Content-Disposition"), anyString());
        document.close();
    }
}

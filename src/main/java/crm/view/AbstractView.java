package crm.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * Abstract base class for custom views.
 * Extends Spring's AbstractView to provide common functionality.
 */
public abstract class AbstractView extends org.springframework.web.servlet.view.AbstractView {

    private String contentType;

    /**
     * Set the content type for the response.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
        super.setContentType(contentType);
    }

    /**
     * Get the content type.
     */
    @Override
    public String getContentType() {
        return this.contentType;
    }

    /**
     * Indicates whether this view generates download content.
     */
    protected boolean generatesDownloadContent() {
        return false;
    }

    /**
     * Create a temporary OutputStream for building the content.
     */
    protected ByteArrayOutputStream createTemporaryOutputStream() {
        return new ByteArrayOutputStream(4096);
    }

    /**
     * Write the given temporary OutputStream to the HTTP response.
     */
    protected void writeToResponse(HttpServletResponse response, ByteArrayOutputStream baos) throws java.io.IOException {
        response.setContentLength(baos.size());
        OutputStream out = response.getOutputStream();
        baos.writeTo(out);
        out.flush();
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
        // To be implemented by subclasses
    }
}

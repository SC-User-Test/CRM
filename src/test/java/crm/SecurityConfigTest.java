package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testCustomUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertNotNull(service);
        assertTrue(service instanceof SpringDataUserDetailsService);
    }

    @Test
    void testPasswordEncoderEncodesPassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoderMatchesIncorrectPassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);

        assertFalse(encoder.matches("wrongpassword", encodedPassword));
    }

    @Test
    void testSecurityConfigIsAnnotatedWithConfiguration() {
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void testSecurityConfigIsAnnotatedWithEnableWebSecurity() {
        assertTrue(securityConfig.getClass().isAnnotationPresent(org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class));
    }
}

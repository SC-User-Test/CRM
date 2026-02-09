package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    public void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    public void testSecurityConfigCreation() {
        assertNotNull(securityConfig);
    }

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    public void testPasswordEncoderEncryption() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";
        String encodedPassword = encoder.encode(rawPassword);
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    public void testCustomUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertNotNull(service);
    }

    @Test
    public void testPasswordEncoderConsistency() {
        BCryptPasswordEncoder encoder1 = securityConfig.passwordEncoder();
        BCryptPasswordEncoder encoder2 = securityConfig.passwordEncoder();
        assertNotNull(encoder1);
        assertNotNull(encoder2);
    }

    @Test
    public void testPasswordEncoderWithDifferentPasswords() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password1 = "password1";
        String password2 = "password2";
        String encoded1 = encoder.encode(password1);
        String encoded2 = encoder.encode(password2);
        assertFalse(encoder.matches(password1, encoded2));
        assertFalse(encoder.matches(password2, encoded1));
    }
}

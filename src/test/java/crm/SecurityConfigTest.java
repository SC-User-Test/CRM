package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testSecurityConfigConstructor() {
        assertNotNull(securityConfig);
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testPasswordEncoderEncodes() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testCustomUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertNotNull(service);
        assertTrue(service instanceof SpringDataUserDetailsService);
    }

    @Test
    void testAuthenticationProvider() {
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();
        assertNotNull(provider);
        assertTrue(provider instanceof DaoAuthenticationProvider);
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        when(authConfig.getAuthenticationManager()).thenReturn(null);

        securityConfig.authenticationManager(authConfig);

        verify(authConfig, times(1)).getAuthenticationManager();
    }

    @Test
    void testPasswordEncoderWithDifferentPasswords() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password1 = "password123";
        String password2 = "password456";

        String encoded1 = encoder.encode(password1);
        String encoded2 = encoder.encode(password2);

        assertNotEquals(encoded1, encoded2);
        assertTrue(encoder.matches(password1, encoded1));
        assertFalse(encoder.matches(password1, encoded2));
    }

    @Test
    void testPasswordEncoderWithEmptyString() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String emptyPassword = "";
        String encoded = encoder.encode(emptyPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(emptyPassword, encoded));
    }
}

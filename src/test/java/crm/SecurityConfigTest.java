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
    void testConstructor() {
        assertNotNull(securityConfig);
    }

    @Test
    void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
    }

    @Test
    void testCustomUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();

        assertNotNull(service);
    }

    @Test
    void testPasswordEncoder_IsBCryptPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void testCustomUserDetailsService_IsSpringDataUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();

        assertTrue(service instanceof SpringDataUserDetailsService);
    }
}

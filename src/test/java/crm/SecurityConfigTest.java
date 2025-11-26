package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        assertNotNull(encoder);

        String password = "testPassword";
        String encoded = encoder.encode(password);
        assertNotNull(encoded);
        assertTrue(encoder.matches(password, encoded));
    }

    @Test
    public void testCustomUserDetailsService() {
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();
        assertNotNull(service);
    }

    @Test
    public void testPasswordEncoderReturnsDifferentHashForSamePassword() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "samePassword";
        String encoded1 = encoder.encode(password);
        String encoded2 = encoder.encode(password);

        assertNotEquals(encoded1, encoded2);
        assertTrue(encoder.matches(password, encoded1));
        assertTrue(encoder.matches(password, encoded2));
    }

    @Test
    public void testPasswordEncoderWithEmptyString() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String emptyPassword = "";
        String encoded = encoder.encode(emptyPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(emptyPassword, encoded));
    }

    @Test
    public void testPasswordEncoderWithNullDoesNotMatch() {
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "testPassword";
        String encoded = encoder.encode(password);

        assertFalse(encoder.matches(null, encoded));
    }
}

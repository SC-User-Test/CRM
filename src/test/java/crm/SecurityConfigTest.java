package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        // Act
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void passwordEncoder_shouldEncodePassword() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // Act
        String encodedPassword = encoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void customUserDetailsService_shouldReturnSpringDataUserDetailsService() {
        // Act
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();

        // Assert
        assertNotNull(service);
        assertInstanceOf(SpringDataUserDetailsService.class, service);
    }

    @Test
    void securityConfig_shouldHaveConfigurationAnnotation() {
        // Assert
        assertTrue(SecurityConfig.class.isAnnotationPresent(org.springframework.context.annotation.Configuration.class));
    }

    @Test
    void securityConfig_shouldHaveEnableWebSecurityAnnotation() {
        // Assert
        assertTrue(SecurityConfig.class.isAnnotationPresent(org.springframework.security.config.annotation.web.configuration.EnableWebSecurity.class));
    }

    @Test
    void securityConfig_shouldHaveEnableMethodSecurityAnnotation() {
        // Assert
        assertTrue(SecurityConfig.class.isAnnotationPresent(org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity.class));
    }

    @Test
    void passwordEncoder_shouldProduceDifferentHashesForSamePassword() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "samePassword";

        // Act
        String hash1 = encoder.encode(password);
        String hash2 = encoder.encode(password);

        // Assert
        assertNotEquals(hash1, hash2, "BCrypt should produce different hashes for same password");
        assertTrue(encoder.matches(password, hash1));
        assertTrue(encoder.matches(password, hash2));
    }

    @Test
    void passwordEncoder_shouldNotMatchIncorrectPassword() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String correctPassword = "correctPassword";
        String wrongPassword = "wrongPassword";

        // Act
        String encodedPassword = encoder.encode(correctPassword);

        // Assert
        assertFalse(encoder.matches(wrongPassword, encodedPassword));
    }
}

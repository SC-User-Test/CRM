package crm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void securityConfig_shouldBeLoaded() {
        // Assert
        assertNotNull(securityConfig);
    }

    @Test
    void passwordEncoder_shouldBeConfigured() {
        // Act
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
    }

    @Test
    void passwordEncoder_shouldEncodePasswords() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        // Act
        String encoded = encoder.encode(rawPassword);

        // Assert
        assertNotNull(encoded);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void customUserDetailsService_shouldBeConfigured() {
        // Act
        var userDetailsService = securityConfig.customUserDetailsService();

        // Assert
        assertNotNull(userDetailsService);
    }

    @Test
    void authenticationProvider_shouldBeConfigured() {
        // Act
        var authProvider = securityConfig.authenticationProvider();

        // Assert
        assertNotNull(authProvider);
    }
}

package crm;

import crm.service.SpringDataUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    void testConstructor_ShouldCreateInstance() {
        // Arrange & Act
        SecurityConfig config = new SecurityConfig();

        // Assert
        assertNotNull(config);
    }

    @Test
    void testPasswordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Arrange & Act
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder);
        assertInstanceOf(BCryptPasswordEncoder.class, encoder);
    }

    @Test
    void testCustomUserDetailsService_ShouldReturnSpringDataUserDetailsService() {
        // Arrange & Act
        SpringDataUserDetailsService service = securityConfig.customUserDetailsService();

        // Assert
        assertNotNull(service);
        assertInstanceOf(SpringDataUserDetailsService.class, service);
    }

    @Test
    void testAuthenticationProvider_ShouldReturnDaoAuthenticationProvider() {
        // Arrange & Act
        DaoAuthenticationProvider provider = securityConfig.authenticationProvider();

        // Assert
        assertNotNull(provider);
        assertInstanceOf(DaoAuthenticationProvider.class, provider);
    }

    @Test
    void testAuthenticationManager_ShouldReturnAuthenticationManager() throws Exception {
        // Arrange
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(authConfig.getAuthenticationManager()).thenReturn(mockManager);

        // Act
        AuthenticationManager manager = securityConfig.authenticationManager(authConfig);

        // Assert
        assertNotNull(manager);
        assertEquals(mockManager, manager);
    }

    @Test
    void testPasswordEncoder_ShouldEncodePassword() {
        // Arrange
        BCryptPasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testpassword";

        // Act
        String encodedPassword = encoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoder_MultipleCallsShouldReturnNewInstances() {
        // Arrange & Act
        BCryptPasswordEncoder encoder1 = securityConfig.passwordEncoder();
        BCryptPasswordEncoder encoder2 = securityConfig.passwordEncoder();

        // Assert
        assertNotNull(encoder1);
        assertNotNull(encoder2);
        assertNotSame(encoder1, encoder2);
    }
}

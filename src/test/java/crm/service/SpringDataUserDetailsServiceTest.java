package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .enabled(1)
                .role(testRole)
                .build();
    }

    @Test
    void loadUserByUsername_withValidUsername_shouldReturnUserDetails() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertTrue(result instanceof CurrentUser);
        verify(userService).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_withInvalidUsername_shouldThrowException() {
        // Arrange
        when(userService.findByUsername("invalid")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("invalid");
        });
        verify(userService).findByUsername("invalid");
    }

    @Test
    void loadUserByUsername_shouldSetAuthorities() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_shouldSetUserInCurrentUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertTrue(result instanceof CurrentUser);
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(testUser, currentUser.getUser());
    }

    @Test
    void loadUserByUsername_withNullUsername_shouldThrowException() {
        // Arrange
        when(userService.findByUsername(null)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
    }

    @Test
    void springDataUserDetailsService_shouldBeInstantiable() {
        // Assert
        assertNotNull(userDetailsService);
    }
}

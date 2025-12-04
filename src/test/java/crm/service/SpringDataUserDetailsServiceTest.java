package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testLoadUserByUsername_WithValidUsername_ShouldReturnUserDetails() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
        verify(userService).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_WithValidUsername_ShouldReturnCurrentUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertInstanceOf(CurrentUser.class, result);
    }

    @Test
    void testLoadUserByUsername_WithValidUsername_ShouldSetAuthorities() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
        assertEquals(1, result.getAuthorities().size());
    }

    @Test
    void testLoadUserByUsername_WithNonExistentUsername_ShouldThrowException() {
        // Arrange
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testLoadUserByUsername_WithNullUsername_ShouldThrowException() {
        // Arrange
        when(userService.findByUsername(null)).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
    }

    @Test
    void testLoadUserByUsername_WithEmptyUsername_ShouldThrowException() {
        // Arrange
        when(userService.findByUsername("")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });
    }

    @Test
    void testLoadUserByUsername_WithAdminRole_ShouldLoadCorrectly() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testLoadUserByUsername_ShouldCallUserServiceOnce() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);

        // Act
        userDetailsService.loadUserByUsername("testuser");

        // Assert
        verify(userService, times(1)).findByUsername("testuser");
    }
}

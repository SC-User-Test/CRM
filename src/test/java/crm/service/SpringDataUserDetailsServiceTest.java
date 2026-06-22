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

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setRole(testRole);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password123", result.getPassword());
        assertTrue(result.getAuthorities().size() > 0);
        verify(userService).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_shouldReturnCurrentUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertInstanceOf(CurrentUser.class, result);
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(testUser, currentUser.getUser());
    }

    @Test
    void loadUserByUsername_shouldSetAuthorities() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertNotNull(result.getAuthorities());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_withNonExistentUser_shouldThrowException() {
        // Arrange
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
        verify(userService).findByUsername("nonexistent");
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
    void loadUserByUsername_withAdminRole_shouldSetAdminAuthority() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        testUser.setRole(adminRole);
        when(userService.findByUsername("admin")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("admin");

        // Assert
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_shouldReturnEnabledUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        // Act
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        // Assert
        assertTrue(result.isEnabled());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isCredentialsNonExpired());
    }
}

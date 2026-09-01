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
    private SpringDataUserDetailsService springDataUserDetailsService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("encodedPassword")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testLoadUserByUsername_existingUser_returnsCurrentUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);
        // Act
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        // Assert
        assertNotNull(result);
        assertInstanceOf(CurrentUser.class, result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void testLoadUserByUsername_existingUser_hasCorrectAuthority() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);
        // Act
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        // Assert
        assertNotNull(result.getAuthorities());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testLoadUserByUsername_nonExistingUser_throwsUsernameNotFoundException() {
        // Arrange
        when(userService.findByUsername("unknown")).thenReturn(null);
        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> springDataUserDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    void testLoadUserByUsername_adminUser_hasAdminAuthority() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        User adminUser = User.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .password("adminPass")
                .role(adminRole)
                .build();

        when(userService.findByUsername("admin")).thenReturn(adminUser);
        // Act
        UserDetails result = springDataUserDetailsService.loadUserByUsername("admin");
        // Assert
        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsername_setsUserOnCurrentUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(user);
        // Act
        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");
        // Assert
        assertInstanceOf(CurrentUser.class, result);
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testLoadUserByUsername_usernameNotFoundMessage_containsUsername() {
        // Arrange
        when(userService.findByUsername("missinguser")).thenReturn(null);
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> springDataUserDetailsService.loadUserByUsername("missinguser"));
        assertEquals("missinguser", exception.getMessage());
    }
}

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

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("encodedPassword");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testLoadUserByUsername_Success() {
        when(userService.findByUsername("johndoe")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("johndoe");
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userService).findByUsername("johndoe");
    }

    @Test
    void testLoadUserByUsername_ReturnsCurrentUser() {
        when(userService.findByUsername("johndoe")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("johndoe");
        assertTrue(result instanceof CurrentUser);
    }

    @Test
    void testLoadUserByUsername_HasAuthorities() {
        when(userService.findByUsername("johndoe")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("johndoe");
        assertNotNull(result.getAuthorities());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    void testLoadUserByUsername_AuthorityMatchesRole() {
        when(userService.findByUsername("johndoe")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("johndoe");
        boolean hasRole = result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        assertTrue(hasRole);
    }

    @Test
    void testLoadUserByUsername_UserNotFound_ThrowsException() {
        when(userService.findByUsername("unknown")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class,
                () -> springDataUserDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    void testLoadUserByUsername_AdminRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);

        when(userService.findByUsername("admin")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("admin");
        boolean hasAdminRole = result.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        assertTrue(hasAdminRole);
    }

    @Test
    void testLoadUserByUsername_PasswordPreserved() {
        when(userService.findByUsername("johndoe")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("johndoe");
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void testLoadUserByUsername_UserSetInCurrentUser() {
        when(userService.findByUsername("johndoe")).thenReturn(user);
        UserDetails result = springDataUserDetailsService.loadUserByUsername("johndoe");
        CurrentUser currentUser = (CurrentUser) result;
        assertEquals(user, currentUser.getUser());
    }
}

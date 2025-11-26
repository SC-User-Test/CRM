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
public class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

    private User user;
    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole(role);
    }

    @Test
    public void testLoadUserByUsername() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertTrue(result instanceof CurrentUser);
        assertEquals("testuser", result.getUsername());
        verify(userService).findByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userService.findByUsername("notfound")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound");
        });
        verify(userService).findByUsername("notfound");
    }

    @Test
    public void testLoadUserByUsernameReturnsCurrentUser() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals(CurrentUser.class, result.getClass());
    }

    @Test
    public void testLoadUserByUsernameAuthorities() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(result.getAuthorities());
        assertEquals(1, result.getAuthorities().size());
    }

    @Test
    public void testLoadUserByUsernameWithAdminRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);

        when(userService.findByUsername("admin")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("admin");

        assertNotNull(result);
        assertTrue(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void testLoadUserByUsernamePassword() {
        when(userService.findByUsername("testuser")).thenReturn(user);
        UserDetails result = userDetailsService.loadUserByUsername("testuser");

        assertEquals("password123", result.getPassword());
    }

    @Test
    public void testLoadUserByUsernameNullUsername() {
        when(userService.findByUsername(null)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
    }

    @Test
    public void testLoadUserByUsernameEmptyUsername() {
        when(userService.findByUsername("")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });
    }
}

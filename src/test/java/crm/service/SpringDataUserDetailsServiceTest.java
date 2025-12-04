package crm.service;

import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService springDataUserDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        Role role = new Role();
        role.setName("ROLE_USER");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(role);

        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails result = springDataUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            springDataUserDetailsService.loadUserByUsername("nonexistent");
        });

        verify(userService, times(1)).findByUsername("nonexistent");
    }

    @Test
    void testLoadUserByUsernameWithAuthorities() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");

        User user = new User();
        user.setUsername("admin");
        user.setPassword("adminpass");
        user.setRole(role);

        when(userService.findByUsername("admin")).thenReturn(user);

        UserDetails result = springDataUserDetailsService.loadUserByUsername("admin");

        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertFalse(result.getAuthorities().isEmpty());
    }
}

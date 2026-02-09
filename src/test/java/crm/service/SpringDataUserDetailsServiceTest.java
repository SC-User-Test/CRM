package crm.service;

import crm.entity.CurrentUser;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpringDataUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SpringDataUserDetailsService springDataUserDetailsService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .enabled(1)
                .role(testRole)
                .build();
    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        UserDetails userDetails = springDataUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertTrue(userDetails instanceof CurrentUser);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            springDataUserDetailsService.loadUserByUsername("nonexistent");
        });
        verify(userService, times(1)).findByUsername("nonexistent");
    }

    @Test
    public void testLoadUserByUsernameWithAdminRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        User adminUser = User.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .password("adminpass")
                .enabled(1)
                .role(adminRole)
                .build();

        when(userService.findByUsername("admin")).thenReturn(adminUser);

        UserDetails userDetails = springDataUserDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void testLoadUserByUsernameAuthorities() {
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        UserDetails userDetails = springDataUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails.getAuthorities());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void testLoadUserByUsernameMultipleCalls() {
        when(userService.findByUsername("testuser")).thenReturn(testUser);

        UserDetails userDetails1 = springDataUserDetailsService.loadUserByUsername("testuser");
        UserDetails userDetails2 = springDataUserDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails1);
        assertNotNull(userDetails2);
        verify(userService, times(2)).findByUsername("testuser");
    }
}

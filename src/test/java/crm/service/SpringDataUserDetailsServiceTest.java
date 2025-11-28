package crm.service;

import crm.entity.CurrentUser;
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

    @InjectMocks
    private SpringDataUserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .enabled(1)
                .role(role)
                .build();

        when(userService.findByUsername("testuser")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails instanceof CurrentUser);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userService.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    void testLoadUserByUsernameWithAdminRole() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");

        User user = User.builder()
                .id(1L)
                .username("admin")
                .password("adminpass")
                .email("admin@example.com")
                .enabled(1)
                .role(role)
                .build();

        when(userService.findByUsername("admin")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testLoadUserByUsernameReturnsCurrentUser() {
        Role role = new Role();
        role.setId(2);
        role.setName("ROLE_MANAGER");

        User user = User.builder()
                .id(2L)
                .username("manager")
                .password("managerpass")
                .email("manager@example.com")
                .enabled(1)
                .role(role)
                .build();

        when(userService.findByUsername("manager")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("manager");

        assertTrue(userDetails instanceof CurrentUser);
        CurrentUser currentUser = (CurrentUser) userDetails;
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testLoadUserByUsernameVerifyServiceCall() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .role(role)
                .build();

        when(userService.findByUsername("testuser")).thenReturn(user);

        userDetailsService.loadUserByUsername("testuser");

        verify(userService, times(1)).findByUsername("testuser");
    }
}

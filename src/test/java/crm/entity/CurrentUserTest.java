package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;
    private Set<GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser();
        user = new User();
        authorities = new HashSet<>();
    }

    @Test
    void testCurrentUserConstructor() {
        assertNotNull(currentUser);
    }

    @Test
    void testSetAndGetUser() {
        user.setId(1L);
        user.setUsername("testuser");
        currentUser.setUser(user);

        assertEquals(user, currentUser.getUser());
        assertEquals("testuser", currentUser.getUser().getUsername());
    }

    @Test
    void testSetAndGetAuthorities() {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);

        assertEquals(2, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testGetPassword() {
        user.setPassword("securePassword");
        currentUser.setUser(user);

        assertEquals("securePassword", currentUser.getPassword());
    }

    @Test
    void testGetUsername() {
        user.setUsername("johndoe");
        currentUser.setUser(user);

        assertEquals("johndoe", currentUser.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    void testGetAuthoritiesReturnsEmptySet() {
        currentUser.setAuthorities(new HashSet<>());
        assertNotNull(currentUser.getAuthorities());
        assertEquals(0, currentUser.getAuthorities().size());
    }

    @Test
    void testGetAuthoritiesWithMultipleRoles() {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        currentUser.setAuthorities(authorities);

        assertEquals(3, currentUser.getAuthorities().size());
    }

    @Test
    void testGetPasswordWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> {
            currentUser.getPassword();
        });
    }

    @Test
    void testGetUsernameWithNullUser() {
        currentUser.setUser(null);
        assertThrows(NullPointerException.class, () -> {
            currentUser.getUsername();
        });
    }

    @Test
    void testCurrentUserWithFullUserDetails() {
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("admin123");
        user.setEmail("admin@example.com");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setEnabled(1);
        user.setRole(role);

        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);

        assertEquals("admin", currentUser.getUsername());
        assertEquals("admin123", currentUser.getPassword());
        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.isEnabled());
        assertTrue(currentUser.isAccountNonExpired());
        assertTrue(currentUser.isAccountNonLocked());
        assertTrue(currentUser.isCredentialsNonExpired());
    }
}

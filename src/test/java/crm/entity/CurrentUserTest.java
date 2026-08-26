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
    private Role role;
    private Set<GrantedAuthority> authorities;

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

        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser = new CurrentUser();
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    void testDefaultConstructor() {
        CurrentUser cu = new CurrentUser();
        assertNotNull(cu);
    }

    @Test
    void testSetAndGetUser() {
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testSetAndGetAuthorities() {
        assertEquals(authorities, currentUser.getAuthorities());
    }

    @Test
    void testGetPassword() {
        assertEquals("encodedPassword", currentUser.getPassword());
    }

    @Test
    void testGetUsername() {
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
    void testAuthoritiesNotEmpty() {
        assertFalse(currentUser.getAuthorities().isEmpty());
    }

    @Test
    void testAuthoritiesContainsRoleUser() {
        boolean hasRole = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        assertTrue(hasRole);
    }

    @Test
    void testSetUserNull() {
        currentUser.setUser(null);
        assertNull(currentUser.getUser());
    }

    @Test
    void testSetAuthoritiesEmpty() {
        currentUser.setAuthorities(new HashSet<>());
        assertTrue(currentUser.getAuthorities().isEmpty());
    }

    @Test
    void testMultipleAuthorities() {
        Set<GrantedAuthority> multiAuth = new HashSet<>();
        multiAuth.add(new SimpleGrantedAuthority("ROLE_USER"));
        multiAuth.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(multiAuth);
        assertEquals(2, currentUser.getAuthorities().size());
    }

    @Test
    void testEqualsAndHashCode() {
        CurrentUser cu1 = new CurrentUser();
        cu1.setUser(user);
        cu1.setAuthorities(authorities);

        CurrentUser cu2 = new CurrentUser();
        cu2.setUser(user);
        cu2.setAuthorities(authorities);

        assertEquals(cu1, cu2);
        assertEquals(cu1.hashCode(), cu2.hashCode());
    }

    @Test
    void testToString() {
        String str = currentUser.toString();
        assertNotNull(str);
    }
}

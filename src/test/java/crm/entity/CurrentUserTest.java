package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser();
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(1);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(currentUser);
    }

    @Test
    void testSetAndGetUser_returnsCorrectUser() {
        currentUser.setUser(user);
        assertNotNull(currentUser.getUser());
        assertEquals("testuser", currentUser.getUser().getUsername());
    }

    @Test
    void testSetAndGetAuthorities_returnsCorrectAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);
        assertNotNull(currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
    }

    @Test
    void testGetAuthorities_returnsAuthoritiesCollection() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);
        Collection<? extends GrantedAuthority> result = currentUser.getAuthorities();
        assertNotNull(result);
        assertTrue(result.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testGetPassword_returnsUserPassword() {
        currentUser.setUser(user);
        assertEquals("password123", currentUser.getPassword());
    }

    @Test
    void testGetUsername_returnsUserUsername() {
        currentUser.setUser(user);
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testIsAccountNonExpired_returnsTrue() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked_returnsTrue() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired_returnsTrue() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled_returnsTrue() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    void testSetAuthorities_withMultipleRoles_returnsAllRoles() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);
        assertEquals(2, currentUser.getAuthorities().size());
    }

    @Test
    void testSetAuthorities_withEmptySet_returnsEmptyCollection() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        currentUser.setAuthorities(authorities);
        assertTrue(currentUser.getAuthorities().isEmpty());
    }

    @Test
    void testEquals_sameUser_returnsTrue() {
        CurrentUser cu1 = new CurrentUser();
        cu1.setUser(user);
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        cu1.setAuthorities(authorities);

        CurrentUser cu2 = new CurrentUser();
        cu2.setUser(user);
        cu2.setAuthorities(authorities);

        assertEquals(cu1, cu2);
    }

    @Test
    void testToString_notNull() {
        currentUser.setUser(user);
        assertNotNull(currentUser.toString());
    }
}

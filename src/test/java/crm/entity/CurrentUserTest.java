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
        user.setUsername("testuser");
        user.setPassword("password");
        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    void testCurrentUserSettersAndGetters() {
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);

        assertEquals(user, currentUser.getUser());
        assertEquals(authorities, currentUser.getAuthorities());
    }

    @Test
    void testGetUsername() {
        currentUser.setUser(user);
        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testGetPassword() {
        currentUser.setUser(user);
        assertEquals("password", currentUser.getPassword());
    }

    @Test
    void testGetAuthorities() {
        currentUser.setAuthorities(authorities);
        assertEquals(1, currentUser.getAuthorities().size());
        assertTrue(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
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
    void testCurrentUserWithMultipleAuthorities() {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        currentUser.setAuthorities(authorities);

        assertEquals(3, currentUser.getAuthorities().size());
    }

    @Test
    void testCurrentUserWithNullUser() {
        currentUser.setUser(null);
        assertNull(currentUser.getUser());
    }

    @Test
    void testCurrentUserWithEmptyAuthorities() {
        currentUser.setAuthorities(new HashSet<>());
        assertNotNull(currentUser.getAuthorities());
        assertTrue(currentUser.getAuthorities().isEmpty());
    }
}

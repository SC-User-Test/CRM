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
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(1);

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        user.setRole(role);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        assertNotNull(currentUser);
    }

    @Test
    void testSetAndGetUser_returnsCorrectUser() {
        currentUser.setUser(user);
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testSetAndGetAuthorities_returnsCorrectAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);
        assertEquals(authorities, currentUser.getAuthorities());
    }

    @Test
    void testGetAuthorities_returnsCollection() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);
        Collection<? extends GrantedAuthority> result = currentUser.getAuthorities();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetPassword_returnsUserPassword() {
        currentUser.setUser(user);
        assertEquals("encodedPassword", currentUser.getPassword());
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
        CurrentUser cu2 = new CurrentUser();
        cu2.setUser(user);
        assertEquals(cu1, cu2);
    }

    @Test
    void testToString_notNull() {
        currentUser.setUser(user);
        assertNotNull(currentUser.toString());
    }
}

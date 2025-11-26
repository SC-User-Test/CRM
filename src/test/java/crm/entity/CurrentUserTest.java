package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrentUserTest {

    private CurrentUser currentUser;
    private User mockUser;
    private Set<GrantedAuthority> authorities;

    @BeforeEach
    public void setUp() {
        currentUser = new CurrentUser();
        mockUser = mock(User.class);
        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    public void testConstructor() {
        CurrentUser user = new CurrentUser();
        assertNotNull(user);
    }

    @Test
    public void testSetAndGetUser() {
        currentUser.setUser(mockUser);
        assertEquals(mockUser, currentUser.getUser());
    }

    @Test
    public void testSetAndGetAuthorities() {
        currentUser.setAuthorities(authorities);
        assertEquals(authorities, currentUser.getAuthorities());
    }

    @Test
    public void testGetPassword() {
        when(mockUser.getPassword()).thenReturn("password123");
        currentUser.setUser(mockUser);

        assertEquals("password123", currentUser.getPassword());
        verify(mockUser).getPassword();
    }

    @Test
    public void testGetUsername() {
        when(mockUser.getUsername()).thenReturn("testuser");
        currentUser.setUser(mockUser);

        assertEquals("testuser", currentUser.getUsername());
        verify(mockUser).getUsername();
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(currentUser.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(currentUser.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(currentUser.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(currentUser.isEnabled());
    }

    @Test
    public void testGetAuthoritiesReturnsCorrectSet() {
        currentUser.setAuthorities(authorities);
        Set<GrantedAuthority> result = (Set<GrantedAuthority>) currentUser.getAuthorities();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    public void testNullUser() {
        currentUser.setUser(null);
        assertNull(currentUser.getUser());
    }

    @Test
    public void testEmptyAuthorities() {
        Set<GrantedAuthority> emptySet = new HashSet<>();
        currentUser.setAuthorities(emptySet);

        assertEquals(0, currentUser.getAuthorities().size());
    }
}

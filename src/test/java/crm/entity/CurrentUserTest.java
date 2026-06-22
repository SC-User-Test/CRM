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

    @BeforeEach
    void setUp() {
        currentUser = new CurrentUser();
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
    }

    @Test
    void testCurrentUserCreation() {
        // Assert
        assertNotNull(currentUser);
    }

    @Test
    void testSetAndGetUser() {
        // Act
        currentUser.setUser(user);

        // Assert
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void testSetAndGetAuthorities() {
        // Arrange
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Act
        currentUser.setAuthorities(authorities);

        // Assert
        assertEquals(authorities, currentUser.getAuthorities());
        assertEquals(1, currentUser.getAuthorities().size());
    }

    @Test
    void testGetPassword() {
        // Arrange
        currentUser.setUser(user);

        // Act
        String password = currentUser.getPassword();

        // Assert
        assertEquals("password123", password);
    }

    @Test
    void testGetUsername() {
        // Arrange
        currentUser.setUser(user);

        // Act
        String username = currentUser.getUsername();

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void testIsAccountNonExpired() {
        // Act
        boolean isNonExpired = currentUser.isAccountNonExpired();

        // Assert
        assertTrue(isNonExpired);
    }

    @Test
    void testIsAccountNonLocked() {
        // Act
        boolean isNonLocked = currentUser.isAccountNonLocked();

        // Assert
        assertTrue(isNonLocked);
    }

    @Test
    void testIsCredentialsNonExpired() {
        // Act
        boolean isCredentialsNonExpired = currentUser.isCredentialsNonExpired();

        // Assert
        assertTrue(isCredentialsNonExpired);
    }

    @Test
    void testIsEnabled() {
        // Act
        boolean isEnabled = currentUser.isEnabled();

        // Assert
        assertTrue(isEnabled);
    }

    @Test
    void testGetAuthorities_withMultipleRoles() {
        // Arrange
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);

        // Act
        var result = currentUser.getAuthorities();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testGetPassword_withNullUser() {
        // Arrange
        currentUser.setUser(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            currentUser.getPassword();
        });
    }

    @Test
    void testGetUsername_withNullUser() {
        // Arrange
        currentUser.setUser(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            currentUser.getUsername();
        });
    }

    @Test
    void testImplementsUserDetails() {
        // Assert
        assertTrue(org.springframework.security.core.userdetails.UserDetails.class.isAssignableFrom(CurrentUser.class));
    }

    @Test
    void testCurrentUserEquality() {
        // Arrange
        CurrentUser user1 = new CurrentUser();
        user1.setUser(user);

        CurrentUser user2 = new CurrentUser();
        user2.setUser(user);

        // Assert
        assertEquals(user1, user2);
    }

    @Test
    void testCurrentUserHashCode() {
        // Arrange
        currentUser.setUser(user);

        // Act
        int hashCode = currentUser.hashCode();

        // Assert
        assertNotEquals(0, hashCode);
    }

    @Test
    void testCurrentUserToString() {
        // Arrange
        currentUser.setUser(user);

        // Act
        String toString = currentUser.toString();

        // Assert
        assertNotNull(toString);
        assertTrue(toString.contains("CurrentUser"));
    }

    @Test
    void testSetNullAuthorities() {
        // Act
        currentUser.setAuthorities(null);

        // Assert
        assertNull(currentUser.getAuthorities());
    }

    @Test
    void testEmptyAuthorities() {
        // Arrange
        Set<GrantedAuthority> emptyAuthorities = new HashSet<>();

        // Act
        currentUser.setAuthorities(emptyAuthorities);

        // Assert
        assertNotNull(currentUser.getAuthorities());
        assertEquals(0, currentUser.getAuthorities().size());
    }
}

package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrentUserTest {

    private CurrentUser currentUser;
    private User user;
    private Set<GrantedAuthority> authorities;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("test@example.com");

        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser = new CurrentUser();
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    void testGetAuthorities_ShouldReturnSetAuthorities() {
        // Arrange & Act
        var result = currentUser.getAuthorities();

        // Assert
        assertNotNull(result);
        assertEquals(authorities, result);
    }

    @Test
    void testGetPassword_ShouldReturnUserPassword() {
        // Arrange & Act
        String result = currentUser.getPassword();

        // Assert
        assertEquals("testpassword", result);
    }

    @Test
    void testGetUsername_ShouldReturnUserUsername() {
        // Arrange & Act
        String result = currentUser.getUsername();

        // Assert
        assertEquals("testuser", result);
    }

    @Test
    void testIsAccountNonExpired_ShouldReturnTrue() {
        // Arrange & Act
        boolean result = currentUser.isAccountNonExpired();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsAccountNonLocked_ShouldReturnTrue() {
        // Arrange & Act
        boolean result = currentUser.isAccountNonLocked();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsCredentialsNonExpired_ShouldReturnTrue() {
        // Arrange & Act
        boolean result = currentUser.isCredentialsNonExpired();

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsEnabled_ShouldReturnTrue() {
        // Arrange & Act
        boolean result = currentUser.isEnabled();

        // Assert
        assertTrue(result);
    }

    @Test
    void testGetPassword_WithNullUser_ShouldThrowException() {
        // Arrange
        currentUser.setUser(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> currentUser.getPassword());
    }

    @Test
    void testGetUsername_WithNullUser_ShouldThrowException() {
        // Arrange
        currentUser.setUser(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> currentUser.getUsername());
    }

    @Test
    void testSetUser_ShouldUpdateUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");

        // Act
        currentUser.setUser(newUser);

        // Assert
        assertEquals("newuser", currentUser.getUsername());
    }

    @Test
    void testSetAuthorities_ShouldUpdateAuthorities() {
        // Arrange
        Set<GrantedAuthority> newAuthorities = new HashSet<>();
        newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // Act
        currentUser.setAuthorities(newAuthorities);

        // Assert
        assertEquals(newAuthorities, currentUser.getAuthorities());
    }
}

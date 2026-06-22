package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .enabled(1)
                .build();
        
        authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Test
    void currentUser_shouldBeCreated() {
        // Assert
        assertNotNull(currentUser);
    }

    @Test
    void setUser_shouldSetUserCorrectly() {
        // Act
        currentUser.setUser(user);

        // Assert
        assertEquals(user, currentUser.getUser());
    }

    @Test
    void setAuthorities_shouldSetAuthoritiesCorrectly() {
        // Act
        currentUser.setAuthorities(authorities);

        // Assert
        assertEquals(authorities, currentUser.getAuthorities());
    }

    @Test
    void getAuthorities_shouldReturnAuthorities() {
        // Arrange
        currentUser.setAuthorities(authorities);

        // Act
        var result = currentUser.getAuthorities();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPassword_shouldReturnUserPassword() {
        // Arrange
        currentUser.setUser(user);

        // Act
        String password = currentUser.getPassword();

        // Assert
        assertEquals("password123", password);
    }

    @Test
    void getUsername_shouldReturnUserUsername() {
        // Arrange
        currentUser.setUser(user);

        // Act
        String username = currentUser.getUsername();

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        // Act
        boolean result = currentUser.isAccountNonExpired();

        // Assert
        assertTrue(result);
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        // Act
        boolean result = currentUser.isAccountNonLocked();

        // Assert
        assertTrue(result);
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        // Act
        boolean result = currentUser.isCredentialsNonExpired();

        // Assert
        assertTrue(result);
    }

    @Test
    void isEnabled_shouldReturnTrue() {
        // Act
        boolean result = currentUser.isEnabled();

        // Assert
        assertTrue(result);
    }

    @Test
    void currentUser_withMultipleAuthorities_shouldStoreAll() {
        // Arrange
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);

        // Act
        var result = currentUser.getAuthorities();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void currentUser_shouldImplementUserDetails() {
        // Assert
        assertTrue(currentUser instanceof org.springframework.security.core.userdetails.UserDetails);
    }
}

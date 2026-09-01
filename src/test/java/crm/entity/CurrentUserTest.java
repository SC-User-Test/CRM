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
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(1);
        user.setRole(role);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        currentUser = new CurrentUser();
        currentUser.setUser(user);
        currentUser.setAuthorities(authorities);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        // Arrange & Act
        CurrentUser cu = new CurrentUser();
        // Assert
        assertNotNull(cu);
    }

    @Test
    void testGetPassword_returnsUserPassword() {
        // Arrange & Act
        String password = currentUser.getPassword();
        // Assert
        assertEquals("encodedPassword", password);
    }

    @Test
    void testGetUsername_returnsUserUsername() {
        // Arrange & Act
        String username = currentUser.getUsername();
        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void testGetAuthorities_returnsCorrectAuthorities() {
        // Arrange & Act
        var authorities = currentUser.getAuthorities();
        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testIsAccountNonExpired_returnsTrue() {
        // Arrange & Act
        boolean result = currentUser.isAccountNonExpired();
        // Assert
        assertTrue(result);
    }

    @Test
    void testIsAccountNonLocked_returnsTrue() {
        // Arrange & Act
        boolean result = currentUser.isAccountNonLocked();
        // Assert
        assertTrue(result);
    }

    @Test
    void testIsCredentialsNonExpired_returnsTrue() {
        // Arrange & Act
        boolean result = currentUser.isCredentialsNonExpired();
        // Assert
        assertTrue(result);
    }

    @Test
    void testIsEnabled_returnsTrue() {
        // Arrange & Act
        boolean result = currentUser.isEnabled();
        // Assert
        assertTrue(result);
    }

    @Test
    void testSetAndGetUser_returnsCorrectUser() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("newpass");
        // Act
        currentUser.setUser(newUser);
        // Assert
        assertEquals(newUser, currentUser.getUser());
    }

    @Test
    void testSetAndGetAuthorities_returnsCorrectAuthorities() {
        // Arrange
        Set<GrantedAuthority> newAuthorities = new HashSet<>();
        newAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        // Act
        currentUser.setAuthorities(newAuthorities);
        // Assert
        assertEquals(newAuthorities, currentUser.getAuthorities());
    }

    @Test
    void testGetAuthorities_withMultipleRoles_returnsAll() {
        // Arrange
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        currentUser.setAuthorities(authorities);
        // Act
        var result = currentUser.getAuthorities();
        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void testEquals_sameObject_returnsTrue() {
        // Arrange & Act & Assert
        assertEquals(currentUser, currentUser);
    }

    @Test
    void testToString_notNull() {
        // Arrange & Act
        String result = currentUser.toString();
        // Assert
        assertNotNull(result);
    }
}

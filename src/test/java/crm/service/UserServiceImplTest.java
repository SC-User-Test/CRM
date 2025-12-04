package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SpringDataUserDetailsService springDataUserDetailsService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setId(1);
        userRole.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(1);
        user.setRole(userRole);
    }

    @Test
    void testSetUserRepository_ShouldSetRepository() {
        // Arrange & Act
        userService.setUserRepository(userRepository);

        // Assert - No exception thrown
        assertDoesNotThrow(() -> userService.setUserRepository(userRepository));
    }

    @Test
    void testSetRoleRepository_ShouldSetRepository() {
        // Arrange & Act
        userService.setRoleRepository(roleRepository);

        // Assert - No exception thrown
        assertDoesNotThrow(() -> userService.setRoleRepository(roleRepository));
    }

    @Test
    void testSetPasswordEncoder_ShouldSetEncoder() {
        // Arrange & Act
        userService.setPasswordEncoder(passwordEncoder);

        // Assert - No exception thrown
        assertDoesNotThrow(() -> userService.setPasswordEncoder(passwordEncoder));
    }

    @Test
    void testFindByUsername_ShouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        // Act
        User result = userService.findByUsername("testuser");

        // Assert
        assertEquals(user, result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testListAllUsers_ShouldReturnEnabledUsers() {
        // Arrange
        when(userRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(user));

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void testShowUser_ShouldReturnUserById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.showUser(1L);

        // Assert
        assertEquals(user, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void testShowUser_WithNonExistentId_ShouldReturnNull() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        User result = userService.showUser(999L);

        // Assert
        assertNull(result);
        verify(userRepository).findById(999L);
    }

    @Test
    void testSaveUser_ShouldEncodePasswordAndSave() {
        // Arrange
        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));

        // Act
        userService.saveUser(user);

        // Assert
        verify(passwordEncoder).encode("password123");
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    void testSaveUser_WithIdOne_ShouldSetAdminRole() {
        // Arrange
        user.setId(1L);
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(mock(UserDetails.class));

        // Act
        userService.saveUser(user);

        // Assert
        verify(roleRepository).findByName("ROLE_ADMIN");
        verify(userRepository, times(2)).save(user);
    }

    @Test
    void testEditUser_ShouldEncodePasswordAndSave() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(roleRepository.findById(1)).thenReturn(Optional.of(userRole));

        // Act
        userService.editUser(user);

        // Assert
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    @Test
    void testEditUser_WithNullRole_ShouldSetDefaultRole() {
        // Arrange
        user.setRole(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);

        // Act
        userService.editUser(user);

        // Assert
        verify(roleRepository).findByName("ROLE_USER");
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser_ShouldSetEnabledToZeroAndNullifyPassword() {
        // Arrange & Act
        userService.deleteUser(user);

        // Assert
        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser_ShouldCallRepositorySave() {
        // Arrange & Act
        userService.deleteUser(user);

        // Assert
        verify(userRepository, times(1)).save(user);
    }
}

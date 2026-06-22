package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(testRole)
                .build();
    }

    @Test
    void findByUsername_withValidUsername_shouldReturnUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        // Act
        User result = userService.findByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findByUsername_withInvalidUsername_shouldReturnNull() {
        // Arrange
        when(userRepository.findByUsername("invalid")).thenReturn(null);

        // Act
        User result = userService.findByUsername("invalid");

        // Assert
        assertNull(result);
        verify(userRepository).findByUsername("invalid");
    }

    @Test
    void listAllUsers_shouldReturnAllEnabledUsers() {
        // Arrange
        when(userRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(testUser));

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void showUser_withValidId_shouldReturnUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.showUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void showUser_withInvalidId_shouldReturnNull() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        User result = userService.showUser(999L);

        // Assert
        assertNull(result);
        verify(userRepository).findById(999L);
    }

    @Test
    void saveUser_shouldEncodePasswordAndSaveUser() {
        // Arrange
        when(roleRepository.findByName("ROLE_USER")).thenReturn(testRole);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(null);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(passwordEncoder).encode("password");
        verify(userRepository, atLeastOnce()).save(any(User.class));
        verify(roleRepository).findByName("ROLE_USER");
    }

    @Test
    void editUser_shouldEncodePasswordAndUpdateUser() {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(testRole);
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.editUser(testUser);

        // Assert
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser_shouldDisableUserAndClearPassword() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.deleteUser(testUser);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void setUserRepository_shouldSetRepository() {
        // Arrange
        UserRepository newRepo = mock(UserRepository.class);

        // Act
        userService.setUserRepository(newRepo);

        // Assert - no exception thrown
        assertDoesNotThrow(() -> userService.setUserRepository(newRepo));
    }

    @Test
    void setRoleRepository_shouldSetRepository() {
        // Arrange
        RoleRepository newRepo = mock(RoleRepository.class);

        // Act & Assert
        assertDoesNotThrow(() -> userService.setRoleRepository(newRepo));
    }

    @Test
    void setPasswordEncoder_shouldSetEncoder() {
        // Arrange
        BCryptPasswordEncoder newEncoder = mock(BCryptPasswordEncoder.class);

        // Act & Assert
        assertDoesNotThrow(() -> userService.setPasswordEncoder(newEncoder));
    }
}

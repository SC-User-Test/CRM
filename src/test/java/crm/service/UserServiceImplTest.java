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
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");

        testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");
    }

    @Test
    void findByUsername_shouldReturnUser() {
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
    void findByUsername_withNullUsername_shouldReturnNull() {
        // Arrange
        when(userRepository.findByUsername(null)).thenReturn(null);

        // Act
        User result = userService.findByUsername(null);

        // Assert
        assertNull(result);
        verify(userRepository).findByUsername(null);
    }

    @Test
    void listAllUsers_shouldReturnAllEnabledUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        when(userRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(user1, user2));

        // Act
        Iterable<User> result = userService.listAllUsers();

        // Assert
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void showUser_shouldReturnUserById() {
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
    void showUser_withNonExistentId_shouldReturnNull() {
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
        
        UserDetails userDetails = mock(UserDetails.class);
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(roleRepository).findByName("ROLE_USER");
        verify(passwordEncoder).encode("password123");
        verify(userRepository, atLeastOnce()).save(testUser);
        assertEquals(1, testUser.getEnabled());
    }

    @Test
    void saveUser_withFirstUser_shouldAssignAdminRole() {
        // Arrange
        testUser.setId(1L);
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        
        when(roleRepository.findByName("ROLE_USER")).thenReturn(testRole);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(adminRole);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        UserDetails userDetails = mock(UserDetails.class);
        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(null);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(roleRepository).findByName("ROLE_ADMIN");
        verify(userRepository, times(2)).save(testUser);
    }

    @Test
    void editUser_shouldUpdateUser() {
        // Arrange
        testUser.setRole(testRole);
        when(roleRepository.findById(1)).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.editUser(testUser);

        // Assert
        verify(roleRepository).findById(1);
        verify(userRepository).save(testUser);
        assertEquals(1, testUser.getEnabled());
    }

    @Test
    void editUser_withNonExistentRole_shouldHandleGracefully() {
        // Arrange
        testUser.setRole(testRole);
        when(roleRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.editUser(testUser);

        // Assert
        verify(roleRepository).findById(1);
        verify(userRepository).save(testUser);
    }

    @Test
    void deleteUser_shouldDisableUserAndClearPassword() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.deleteUser(testUser);

        // Assert
        assertEquals(0, testUser.getEnabled());
        assertNull(testUser.getPassword());
        verify(userRepository).save(testUser);
    }

    @Test
    void setUserRepository_shouldSetRepository() {
        // Arrange
        UserRepository newRepository = mock(UserRepository.class);

        // Act
        userService.setUserRepository(newRepository);

        // Assert - No exception thrown
        assertDoesNotThrow(() -> userService.setUserRepository(newRepository));
    }

    @Test
    void setRoleRepository_shouldSetRepository() {
        // Arrange
        RoleRepository newRepository = mock(RoleRepository.class);

        // Act & Assert
        assertDoesNotThrow(() -> userService.setRoleRepository(newRepository));
    }

    @Test
    void setPasswordEncoder_shouldSetEncoder() {
        // Arrange
        BCryptPasswordEncoder newEncoder = mock(BCryptPasswordEncoder.class);

        // Act & Assert
        assertDoesNotThrow(() -> userService.setPasswordEncoder(newEncoder));
    }

    @Test
    void setAuthenticationManager_shouldSetManager() {
        // Arrange
        AuthenticationManager newManager = mock(AuthenticationManager.class);

        // Act & Assert
        assertDoesNotThrow(() -> userService.setAuthenticationManager(newManager));
    }

    @Test
    void setSpringDataUserDetailsService_shouldSetService() {
        // Arrange
        SpringDataUserDetailsService newService = mock(SpringDataUserDetailsService.class);

        // Act & Assert
        assertDoesNotThrow(() -> userService.setSpringDataUserDetailsService(newService));
    }
}

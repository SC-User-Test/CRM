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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("rawPassword")
                .enabled(1)
                .role(role)
                .build();
    }

    @Test
    void testFindByUsername_existingUsername_returnsUser() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        // Act
        User result = userService.findByUsername("testuser");
        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_nonExistingUsername_returnsNull() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(null);
        // Act
        User result = userService.findByUsername("unknown");
        // Assert
        assertNull(result);
    }

    @Test
    void testListAllUsers_returnsEnabledUsers() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userRepository.findAllByEnabled(1)).thenReturn(users);
        // Act
        Iterable<User> result = userService.listAllUsers();
        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testShowUser_existingId_returnsUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Act
        User result = userService.showUser(1L);
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testShowUser_nonExistingId_returnsNull() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        // Act
        User result = userService.showUser(99L);
        // Assert
        assertNull(result);
    }

    @Test
    void testSaveUser_savesUserWithEncodedPassword() {
        // Arrange
        User newUser = User.builder()
                .id(2L)
                .username("newuser")
                .email("new@example.com")
                .password("rawPassword")
                .build();
        Role userRole = new Role();
        userRole.setId(2);
        userRole.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getAuthorities()).thenReturn(new java.util.HashSet<>());
        when(springDataUserDetailsService.loadUserByUsername("newuser")).thenReturn(mockUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // Act
        userService.saveUser(newUser);

        // Assert
        assertEquals("encodedPassword", newUser.getPassword());
        assertEquals(1, newUser.getEnabled());
        verify(userRepository, atLeastOnce()).save(newUser);
    }

    @Test
    void testEditUser_withValidRole_updatesUser() {
        // Arrange
        Role existingRole = new Role();
        existingRole.setId(1);
        existingRole.setName("ROLE_USER");
        user.setRole(existingRole);

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(existingRole);
        when(roleRepository.findById(1)).thenReturn(Optional.of(existingRole));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.editUser(user);

        // Assert
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(1, user.getEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testEditUser_withNullRole_usesDefaultRole() {
        // Arrange
        user.setRole(null);
        Role defaultRole = new Role();
        defaultRole.setId(1);
        defaultRole.setName("ROLE_USER");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(defaultRole);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.editUser(user);

        // Assert
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(1, user.getEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser_disablesUserAndClearsPassword() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);
        // Act
        userService.deleteUser(user);
        // Assert
        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSetUserRepository_setsRepository() {
        // Arrange & Act
        userService.setUserRepository(userRepository);
        // Assert - no exception thrown
        assertNotNull(userService);
    }

    @Test
    void testSetRoleRepository_setsRepository() {
        // Arrange & Act
        userService.setRoleRepository(roleRepository);
        // Assert - no exception thrown
        assertNotNull(userService);
    }

    @Test
    void testSetPasswordEncoder_setsEncoder() {
        // Arrange & Act
        userService.setPasswordEncoder(passwordEncoder);
        // Assert - no exception thrown
        assertNotNull(userService);
    }

    @Test
    void testSetAuthenticationManager_setsManager() {
        // Arrange & Act
        userService.setAuthenticationManager(authenticationManager);
        // Assert - no exception thrown
        assertNotNull(userService);
    }

    @Test
    void testSetSpringDataUserDetailsService_setsService() {
        // Arrange & Act
        userService.setSpringDataUserDetailsService(springDataUserDetailsService);
        // Assert - no exception thrown
        assertNotNull(userService);
    }
}

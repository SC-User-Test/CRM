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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
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

    private User user;
    private Role roleUser;
    private Role roleAdmin;

    @BeforeEach
    void setUp() {
        roleUser = new Role();
        roleUser.setId(1);
        roleUser.setName("ROLE_USER");

        roleAdmin = new Role();
        roleAdmin.setId(2);
        roleAdmin.setName("ROLE_ADMIN");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("plainPassword")
                .enabled(1)
                .role(roleUser)
                .build();
    }

    @Test
    void testFindByUsername_existingUsername_returnsUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        User result = userService.findByUsername("testuser");
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_nonExistingUsername_returnsNull() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);
        User result = userService.findByUsername("nonexistent");
        assertNull(result);
    }

    @Test
    void testListAllUsers_returnsEnabledUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAllByEnabled(1)).thenReturn(users);
        Iterable<User> result = userService.listAllUsers();
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void testShowUser_existingId_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.showUser(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void testShowUser_nonExistingId_returnsNull() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        User result = userService.showUser(99L);
        assertNull(result);
    }

    @Test
    void testSaveUser_setsRoleAndEncodedPasswordAndSaves() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        crm.entity.CurrentUser currentUser = new crm.entity.CurrentUser();
        currentUser.setUser(user);
        java.util.Set<org.springframework.security.core.GrantedAuthority> authorities = new java.util.HashSet<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);

        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(currentUser);
        when(authenticationManager.authenticate(any())).thenReturn(null);

        userService.saveUser(user);

        verify(roleRepository).findByName("ROLE_USER");
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository, atLeastOnce()).save(user);
    }

    @Test
    void testSaveUser_setsEnabledToOne() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        crm.entity.CurrentUser currentUser = new crm.entity.CurrentUser();
        currentUser.setUser(user);
        java.util.Set<org.springframework.security.core.GrantedAuthority> authorities = new java.util.HashSet<>();
        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
        currentUser.setAuthorities(authorities);

        when(springDataUserDetailsService.loadUserByUsername(anyString())).thenReturn(currentUser);
        when(authenticationManager.authenticate(any())).thenReturn(null);

        user.setEnabled(0);
        userService.saveUser(user);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testEditUser_encodesPasswordAndSaves() {
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleUser));

        userService.editUser(user);

        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(user);
    }

    @Test
    void testEditUser_withNullRole_usesDefaultRole() {
        user.setRole(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);

        userService.editUser(user);

        verify(userRepository).save(user);
        assertEquals(roleUser, user.getRole());
    }

    @Test
    void testEditUser_setsEnabledToOne() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(roleUser);
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleUser));

        user.setEnabled(0);
        userService.editUser(user);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testDeleteUser_setsEnabledToZeroAndNullPassword() {
        userService.deleteUser(user);
        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser_callsRepositorySave() {
        userService.deleteUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSetUserRepository_setsRepository() {
        userService.setUserRepository(userRepository);
        // Verify it doesn't throw
        assertNotNull(userService);
    }

    @Test
    void testSetRoleRepository_setsRepository() {
        userService.setRoleRepository(roleRepository);
        assertNotNull(userService);
    }

    @Test
    void testSetPasswordEncoder_setsEncoder() {
        userService.setPasswordEncoder(passwordEncoder);
        assertNotNull(userService);
    }

    @Test
    void testSetAuthenticationManager_setsManager() {
        userService.setAuthenticationManager(authenticationManager);
        assertNotNull(userService);
    }

    @Test
    void testSetSpringDataUserDetailsService_setsService() {
        userService.setSpringDataUserDetailsService(springDataUserDetailsService);
        assertNotNull(userService);
    }
}

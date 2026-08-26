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
import java.util.Collections;
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
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password123");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("johndoe")).thenReturn(user);
        User result = userService.findByUsername("johndoe");
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userRepository).findByUsername("johndoe");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);
        User result = userService.findByUsername("unknown");
        assertNull(result);
    }

    @Test
    void testListAllUsers() {
        when(userRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(user));
        Iterable<User> result = userService.listAllUsers();
        assertNotNull(result);
        verify(userRepository).findAllByEnabled(1);
    }

    @Test
    void testListAllUsersEmpty() {
        when(userRepository.findAllByEnabled(1)).thenReturn(Collections.emptyList());
        Iterable<User> result = userService.listAllUsers();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testShowUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.showUser(1L);
        assertNotNull(result);
        assertEquals("johndoe", result.getUsername());
        verify(userRepository).findById(1L);
    }

    @Test
    void testShowUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        User result = userService.showUser(99L);
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(user);
        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUserSetsEnabledToZero() {
        user.setEnabled(1);
        userService.deleteUser(user);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testDeleteUserSetsPasswordToNull() {
        user.setPassword("somepassword");
        userService.deleteUser(user);
        assertNull(user.getPassword());
    }

    @Test
    void testEditUserWithValidRole() {
        Role adminRole = new Role();
        adminRole.setId(2);
        adminRole.setName("ROLE_ADMIN");
        user.setRole(adminRole);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findById(2)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        userService.editUser(user);

        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testEditUserWithNullRole() {
        user.setRole(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        userService.editUser(user);

        verify(userRepository).save(user);
        assertEquals(role, user.getRole());
    }

    @Test
    void testSetUserRepository() {
        UserServiceImpl service = new UserServiceImpl();
        service.setUserRepository(userRepository);
        // No exception means success
    }

    @Test
    void testSetRoleRepository() {
        UserServiceImpl service = new UserServiceImpl();
        service.setRoleRepository(roleRepository);
        // No exception means success
    }

    @Test
    void testSetPasswordEncoder() {
        UserServiceImpl service = new UserServiceImpl();
        service.setPasswordEncoder(passwordEncoder);
        // No exception means success
    }

    @Test
    void testSetAuthenticationManager() {
        UserServiceImpl service = new UserServiceImpl();
        service.setAuthenticationManager(authenticationManager);
        // No exception means success
    }

    @Test
    void testSetSpringDataUserDetailsService() {
        UserServiceImpl service = new UserServiceImpl();
        service.setSpringDataUserDetailsService(springDataUserDetailsService);
        // No exception means success
    }
}

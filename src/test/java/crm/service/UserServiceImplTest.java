package crm.service;

import crm.entity.Role;
import crm.entity.User;
import crm.repository.RoleRepository;
import crm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(2L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testListAllUsers() {
        when(userRepository.findAllByEnabled(1)).thenReturn(Collections.singletonList(user));

        Iterable<User> result = userService.listAllUsers();

        assertNotNull(result);
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testShowUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.showUser(1L);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testShowUser_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User result = userService.showUser(999L);

        assertNull(result);
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testEditUser() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        userService.editUser(user);

        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testEditUser_WithNullRole() {
        user.setRole(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        userService.editUser(user);

        verify(roleRepository, times(2)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(user);

        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
}

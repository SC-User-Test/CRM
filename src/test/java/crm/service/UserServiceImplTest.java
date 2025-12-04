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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testListAllUsers() {
        List<User> users = new ArrayList<>();
        when(userRepository.findAllByEnabled(1)).thenReturn(users);

        Iterable<User> result = userService.listAllUsers();

        assertNotNull(result);
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testShowUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.showUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setPassword("password");
        when(userRepository.save(user)).thenReturn(user);

        assertDoesNotThrow(() -> userService.deleteUser(user));
        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        User result = userService.findByUsername("nonexistent");

        assertNull(result);
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}

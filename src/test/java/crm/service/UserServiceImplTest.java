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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testListAllUsers() {
        User user1 = User.builder().id(1L).username("user1").enabled(1).build();
        User user2 = User.builder().id(2L).username("user2").enabled(1).build();

        when(userRepository.findAllByEnabled(1)).thenReturn(Arrays.asList(user1, user2));

        Iterable<User> users = userService.listAllUsers();

        assertNotNull(users);
        verify(userRepository, times(1)).findAllByEnabled(1);
    }

    @Test
    void testShowUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.showUser(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testShowUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        User result = userService.showUser(999L);

        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .enabled(1)
                .build();

        userService.deleteUser(user);

        assertEquals(0, user.getEnabled());
        assertNull(user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testSetUserRepository() {
        UserRepository mockRepo = mock(UserRepository.class);
        userService.setUserRepository(mockRepo);
        assertNotNull(userService);
    }

    @Test
    void testSetRoleRepository() {
        RoleRepository mockRepo = mock(RoleRepository.class);
        userService.setRoleRepository(mockRepo);
        assertNotNull(userService);
    }

    @Test
    void testSetPasswordEncoder() {
        BCryptPasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);
        userService.setPasswordEncoder(mockEncoder);
        assertNotNull(userService);
    }

    @Test
    void testSetAuthenticationManager() {
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        userService.setAuthenticationManager(mockManager);
        assertNotNull(userService);
    }

    @Test
    void testSetSpringDataUserDetailsService() {
        SpringDataUserDetailsService mockService = mock(SpringDataUserDetailsService.class);
        userService.setSpringDataUserDetailsService(mockService);
        assertNotNull(userService);
    }

    @Test
    void testFindByUsernameWithNull() {
        when(userRepository.findByUsername(null)).thenReturn(null);

        User result = userService.findByUsername(null);

        assertNull(result);
    }
}

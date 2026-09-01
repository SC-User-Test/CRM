package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

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
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);
        // Assert
        assertNotNull(service);
    }

    @Test
    void testListAllRoles_returnsAllRoles() {
        // Arrange
        List<Role> roles = Arrays.asList(roleUser, roleAdmin);
        when(roleRepository.findAll()).thenReturn(roles);
        // Act
        Iterable<Role> result = roleService.listAllRoles();
        // Assert
        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRoles_emptyList_returnsEmptyIterable() {
        // Arrange
        List<Role> roles = Arrays.asList();
        when(roleRepository.findAll()).thenReturn(roles);
        // Act
        Iterable<Role> result = roleService.listAllRoles();
        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testListAllRoles_singleRole_returnsSingleRole() {
        // Arrange
        List<Role> roles = Arrays.asList(roleUser);
        when(roleRepository.findAll()).thenReturn(roles);
        // Act
        Iterable<Role> result = roleService.listAllRoles();
        // Assert
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        assertEquals("ROLE_USER", result.iterator().next().getName());
    }
}

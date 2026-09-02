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
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);
        assertNotNull(service);
    }

    @Test
    void testListAllRoles_returnsAllRoles() {
        List<Role> roles = Arrays.asList(roleUser, roleAdmin);
        when(roleRepository.findAll()).thenReturn(roles);
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    void testListAllRoles_returnsEmptyList_whenNoRoles() {
        List<Role> emptyList = Arrays.asList();
        when(roleRepository.findAll()).thenReturn(emptyList);
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    void testListAllRoles_returnsSingleRole() {
        List<Role> roles = Arrays.asList(roleUser);
        when(roleRepository.findAll()).thenReturn(roles);
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void testListAllRoles_callsRepositoryFindAll() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList());
        roleService.listAllRoles();
        verify(roleRepository, times(1)).findAll();
    }
}

package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRoleServiceImplConstructor() {
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);
        assertNotNull(service);
    }

    @Test
    void testListAllRoles() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_USER");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        Iterable<Role> roles = roleService.listAllRoles();

        assertNotNull(roles);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesEmpty() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList());

        Iterable<Role> roles = roleService.listAllRoles();

        assertNotNull(roles);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesMultipleCalls() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList());

        roleService.listAllRoles();
        roleService.listAllRoles();

        verify(roleRepository, times(2)).findAll();
    }
}

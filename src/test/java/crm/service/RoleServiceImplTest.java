package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleServiceImpl roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleServiceImpl(roleRepository);
    }

    @Test
    public void testRoleServiceCreation() {
        assertNotNull(roleService);
    }

    @Test
    public void testListAllRoles() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testListAllRolesEmpty() {
        List<Role> emptyList = Arrays.asList();
        when(roleRepository.findAll()).thenReturn(emptyList);

        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testListAllRolesWithMultipleRoles() {
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        Role role3 = new Role();
        role3.setId(3);
        role3.setName("ROLE_MANAGER");

        List<Role> roles = Arrays.asList(role1, role2, role3);
        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        verify(roleRepository, times(1)).findAll();
    }
}

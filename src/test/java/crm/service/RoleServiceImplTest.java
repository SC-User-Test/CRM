package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAllRoles() {
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");
        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");
        roles.add(role1);
        roles.add(role2);

        when(roleRepository.findAll()).thenReturn(roles);

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        assertEquals(2, ((List<Role>) result).size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesEmpty() {
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());

        Iterable<Role> result = roleService.listAllRoles();

        assertNotNull(result);
        assertEquals(0, ((List<Role>) result).size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testListAllRolesNull() {
        when(roleRepository.findAll()).thenReturn(null);

        Iterable<Role> result = roleService.listAllRoles();

        assertNull(result);
        verify(roleRepository, times(1)).findAll();
    }
}

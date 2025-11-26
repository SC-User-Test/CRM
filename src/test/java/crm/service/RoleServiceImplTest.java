package crm.service;

import crm.entity.Role;
import crm.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    public void testConstructor() {
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);
        assertNotNull(service);
    }

    @Test
    public void testListAllRoles() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    public void testListAllRolesEmpty() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());
        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
        verify(roleRepository).findAll();
    }

    @Test
    public void testListAllRolesMultiple() {
        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role, role2));

        Iterable<Role> result = roleService.listAllRoles();
        assertNotNull(result);
        int count = 0;
        for (Role r : result) {
            count++;
        }
        assertEquals(2, count);
        verify(roleRepository).findAll();
    }

    @Test
    public void testListAllRolesCalledOnce() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));
        roleService.listAllRoles();
        verify(roleRepository, times(1)).findAll();
    }
}

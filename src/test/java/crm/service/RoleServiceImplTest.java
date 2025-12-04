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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
    }

    @Test
    void testConstructor_ShouldInitializeWithRepository() {
        // Arrange & Act
        RoleServiceImpl service = new RoleServiceImpl(roleRepository);

        // Assert
        assertNotNull(service);
    }

    @Test
    void testListAllRoles_ShouldReturnAllRoles() {
        // Arrange
        List<Role> roles = Arrays.asList(role);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertEquals(roles, result);
        verify(roleRepository).findAll();
    }

    @Test
    void testListAllRoles_WithEmptyList_ShouldReturnEmptyList() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    void testListAllRoles_WithMultipleRoles_ShouldReturnAllRoles() {
        // Arrange
        Role role1 = new Role();
        role1.setId(1);
        role1.setName("ROLE_USER");

        Role role2 = new Role();
        role2.setId(2);
        role2.setName("ROLE_ADMIN");

        List<Role> roles = Arrays.asList(role1, role2);
        when(roleRepository.findAll()).thenReturn(roles);

        // Act
        Iterable<Role> result = roleService.listAllRoles();

        // Assert
        assertNotNull(result);
        verify(roleRepository).findAll();
    }

    @Test
    void testListAllRoles_ShouldCallRepositoryOnce() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

        // Act
        roleService.listAllRoles();

        // Assert
        verify(roleRepository, times(1)).findAll();
    }
}

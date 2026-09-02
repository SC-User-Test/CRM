package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");
        user.setEnabled(1);
        user.setRole(role);
    }

    @Test
    void testDefaultConstructor_createsInstance() {
        User u = new User();
        assertNotNull(u);
    }

    @Test
    void testAllArgsConstructor_createsInstanceWithValues() {
        User u = new User(2L, "janedoe", "jane@example.com", "Jane", "Doe", "pass", 1, role);
        assertNotNull(u);
        assertEquals(2L, u.getId());
        assertEquals("janedoe", u.getUsername());
        assertEquals("jane@example.com", u.getEmail());
        assertEquals("Jane", u.getFirstName());
        assertEquals("Doe", u.getLastName());
        assertEquals("pass", u.getPassword());
        assertEquals(1, u.getEnabled());
        assertEquals(role, u.getRole());
    }

    @Test
    void testBuilder_createsUserWithAllFields() {
        User u = User.builder()
                .id(3L)
                .username("builder_user")
                .email("builder@example.com")
                .firstName("Builder")
                .lastName("User")
                .password("builderpass")
                .enabled(1)
                .role(role)
                .build();

        assertEquals(3L, u.getId());
        assertEquals("builder_user", u.getUsername());
        assertEquals("builder@example.com", u.getEmail());
        assertEquals("Builder", u.getFirstName());
        assertEquals("User", u.getLastName());
        assertEquals("builderpass", u.getPassword());
        assertEquals(1, u.getEnabled());
        assertEquals(role, u.getRole());
    }

    @Test
    void testSetAndGetId_returnsCorrectId() {
        user.setId(10L);
        assertEquals(10L, user.getId());
    }

    @Test
    void testSetAndGetUsername_returnsCorrectUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testSetAndGetEmail_returnsCorrectEmail() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testSetAndGetFirstName_returnsCorrectFirstName() {
        user.setFirstName("Alice");
        assertEquals("Alice", user.getFirstName());
    }

    @Test
    void testSetAndGetLastName_returnsCorrectLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void testSetAndGetPassword_returnsCorrectPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetAndGetEnabled_withOne_returnsOne() {
        user.setEnabled(1);
        assertEquals(1, user.getEnabled());
    }

    @Test
    void testSetAndGetEnabled_withZero_returnsZero() {
        user.setEnabled(0);
        assertEquals(0, user.getEnabled());
    }

    @Test
    void testSetAndGetRole_returnsCorrectRole() {
        Role newRole = new Role();
        newRole.setId(2);
        newRole.setName("ROLE_ADMIN");
        user.setRole(newRole);
        assertEquals(newRole, user.getRole());
    }

    @Test
    void testGetName_returnsFirstNameAndLastName() {
        user.setFirstName("John");
        user.setLastName("Doe");
        assertEquals("John Doe", user.getName());
    }

    @Test
    void testGetName_withDifferentNames_returnsCorrectFullName() {
        user.setFirstName("Alice");
        user.setLastName("Wonderland");
        assertEquals("Alice Wonderland", user.getName());
    }

    @Test
    void testGetRole_id_returnsRoleId() {
        assertEquals(1, user.getRole_id());
    }

    @Test
    void testGetRole_name_returnsRoleName() {
        assertEquals("ROLE_USER", user.getRole_name());
    }

    @Test
    void testGetColumnCount_returnsPositiveCount() {
        int count = user.getColumnCount();
        assertTrue(count > 0);
    }

    @Test
    void testEquals_equalUsers_returnsTrue() {
        User u1 = User.builder().id(1L).username("user1").email("u1@example.com").build();
        User u2 = User.builder().id(1L).username("user1").email("u1@example.com").build();
        assertEquals(u1, u2);
    }

    @Test
    void testEquals_differentUsers_returnsFalse() {
        User u1 = User.builder().id(1L).username("user1").build();
        User u2 = User.builder().id(2L).username("user2").build();
        assertNotEquals(u1, u2);
    }

    @Test
    void testHashCode_equalUsers_sameHashCode() {
        User u1 = User.builder().id(1L).username("user1").build();
        User u2 = User.builder().id(1L).username("user1").build();
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToString_notNull() {
        assertNotNull(user.toString());
    }

    @Test
    void testSetId_withNull_returnsNull() {
        user.setId(null);
        assertNull(user.getId());
    }

    @Test
    void testSetPassword_withNull_returnsNull() {
        user.setPassword(null);
        assertNull(user.getPassword());
    }
}

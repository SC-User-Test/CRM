package crm.service;

import crm.entity.Category;
import crm.entity.Customer;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    void testCustomerServiceInterface() {
        // Assert that the interface exists
        assertNotNull(CustomerService.class);
        assertTrue(CustomerService.class.isInterface());
    }

    @Test
    void testGetMaxIdMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("getMaxId"));
    }

    @Test
    void testListAllCustomersMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("listAllCustomers"));
    }

    @Test
    void testShowCustomerMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("showCustomer", Long.class));
    }

    @Test
    void testFindAllByEnabledTrueMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findAllByEnabledTrue"));
    }

    @Test
    void testFindAllByEnabledFalseMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findAllByEnabledFalse"));
    }

    @Test
    void testFindOneByEnabledTrueAndNameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findOneByEnabledTrueAndName", String.class));
    }

    @Test
    void testFindByEmailMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEmail", String.class));
    }

    @Test
    void testFindByPhoneMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByPhone", int.class));
    }

    @Test
    void testFindByCategoriesMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByCategories", Set.class));
    }

    @Test
    void testFindByFirstNameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByFirstName", String.class));
    }

    @Test
    void testFindByLastNameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByLastName", String.class));
    }

    @Test
    void testFindByFirstNameAndLastNameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByFirstNameAndLastName", String.class, String.class));
    }

    @Test
    void testFindByCityMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByCity", String.class));
    }

    @Test
    void testFindByCityAndAddressMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByCityAndAddress", String.class, String.class));
    }

    @Test
    void testSaveCustomerMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("saveCustomer", Customer.class));
    }

    @Test
    void testFindByEnabledTrueAndEmailMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEnabledTrueAndEmail", String.class));
    }

    @Test
    void testFindByEnabledFalseAndEmailMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEnabledFalseAndEmail", String.class));
    }

    @Test
    void testFindByEnabledTrueAndPhoneMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEnabledTrueAndPhone", int.class));
    }

    @Test
    void testFindByEnabledFalseAndPhoneMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEnabledFalseAndPhone", int.class));
    }

    @Test
    void testFindByEnabledTrueAndCategoriesMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEnabledTrueAndCategories", Set.class));
    }

    @Test
    void testFindByEnabledFalseAndCategoriesMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(CustomerService.class.getMethod("findByEnabledFalseAndCategories", Set.class));
    }

    @Test
    void testInterfaceHasExpectedNumberOfMethods() {
        // Assert that the interface has the expected number of methods
        int methodCount = CustomerService.class.getDeclaredMethods().length;
        assertTrue(methodCount > 20, "CustomerService should have more than 20 methods");
    }
}

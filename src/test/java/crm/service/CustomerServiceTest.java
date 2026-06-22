package crm.service;

import crm.entity.Category;

/**
 * Interface test - verifies the interface structure
 */
class CustomerServiceTest {

    @org.junit.jupiter.api.Test
    void customerService_shouldHaveGetMaxIdMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            CustomerService.class.getMethod("getMaxId")
        );
    }

    @org.junit.jupiter.api.Test
    void customerService_shouldHaveListAllCustomersMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            CustomerService.class.getMethod("listAllCustomers")
        );
    }

    @org.junit.jupiter.api.Test
    void customerService_shouldHaveShowCustomerMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            CustomerService.class.getMethod("showCustomer", Long.class)
        );
    }

    @org.junit.jupiter.api.Test
    void customerService_shouldHaveSaveCustomerMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            CustomerService.class.getMethod("saveCustomer", crm.entity.Customer.class)
        );
    }

    @org.junit.jupiter.api.Test
    void customerService_shouldHaveFindByEmailMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            CustomerService.class.getMethod("findByEmail", String.class)
        );
    }

    @org.junit.jupiter.api.Test
    void customerService_shouldHaveFindByPhoneMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            CustomerService.class.getMethod("findByPhone", int.class)
        );
    }
}

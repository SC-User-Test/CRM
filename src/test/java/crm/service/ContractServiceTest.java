package crm.service;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ContractServiceTest {

    @Test
    void testContractServiceInterface() {
        // Assert that the interface exists
        assertNotNull(ContractService.class);
        assertTrue(ContractService.class.isInterface());
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findByName", String.class));
    }

    @Test
    void testListAllContractsMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("listAllContracts"));
    }

    @Test
    void testShowContractMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("showContract", Long.class));
    }

    @Test
    void testFindAllByValueLessThanEqualMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByValueLessThanEqual", BigDecimal.class));
    }

    @Test
    void testFindAllByValueGreaterThanEqualMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByValueGreaterThanEqual", BigDecimal.class));
    }

    @Test
    void testFindAllByBeginDateMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByBeginDate", LocalDate.class));
    }

    @Test
    void testFindAllByBeginDateBeforeMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByBeginDateBefore", LocalDate.class));
    }

    @Test
    void testFindAllByBeginDateAfterMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByBeginDateAfter", LocalDate.class));
    }

    @Test
    void testFindAllByEndDateMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByEndDate", LocalDate.class));
    }

    @Test
    void testFindAllByEndDateBeforeMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByEndDateBefore", LocalDate.class));
    }

    @Test
    void testFindAllByEndDateAfterMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByEndDateAfter", LocalDate.class));
    }

    @Test
    void testFindAllByStatusMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByStatus", Status.class));
    }

    @Test
    void testFindAllByCustomerMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByCustomer", Customer.class));
    }

    @Test
    void testFindAllByCustomerAndUserMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByCustomerAndUser", Customer.class, User.class));
    }

    @Test
    void testFindAllByUserMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("findAllByUser", User.class));
    }

    @Test
    void testSaveContractMethodExists() throws NoSuchMethodException {
        // Assert
        assertNotNull(ContractService.class.getMethod("saveContract", Contract.class));
    }

    @Test
    void testInterfaceHasExpectedNumberOfMethods() {
        // Assert that the interface has exactly 17 methods
        assertEquals(17, ContractService.class.getDeclaredMethods().length);
    }
}

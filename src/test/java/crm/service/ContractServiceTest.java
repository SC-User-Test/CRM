package crm.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Interface test - verifies the interface structure
 */
class ContractServiceTest {

    @org.junit.jupiter.api.Test
    void contractService_shouldHaveFindByNameMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("findByName", String.class)
        );
    }

    @org.junit.jupiter.api.Test
    void contractService_shouldHaveListAllContractsMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("listAllContracts")
        );
    }

    @org.junit.jupiter.api.Test
    void contractService_shouldHaveShowContractMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("showContract", Long.class)
        );
    }

    @org.junit.jupiter.api.Test
    void contractService_shouldHaveSaveContractMethod() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("saveContract", crm.entity.Contract.class)
        );
    }

    @org.junit.jupiter.api.Test
    void contractService_shouldHaveFindByValueMethods() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("findAllByValueLessThanEqual", BigDecimal.class)
        );
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("findAllByValueGreaterThanEqual", BigDecimal.class)
        );
    }

    @org.junit.jupiter.api.Test
    void contractService_shouldHaveFindByDateMethods() throws NoSuchMethodException {
        // Assert
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("findAllByBeginDate", LocalDate.class)
        );
        org.junit.jupiter.api.Assertions.assertNotNull(
            ContractService.class.getMethod("findAllByEndDate", LocalDate.class)
        );
    }
}

package crm.controller;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContractController contractController;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setEnabled(1);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        contract = new Contract();
        contract.setId(1L);
        contract.setName("Test Contract");
        contract.setValue(BigDecimal.valueOf(10000.0));
        contract.setBeginDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusYears(1));
        contract.setStatus(Status.PROPOSED);
        contract.setCustomer(customer);
        contract.setUser(user);
    }

    @Test
    void testConstructor_ShouldInitializeWithServices() {
        // Arrange & Act
        ContractController controller = new ContractController(contractService, customerService, userService);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowAllContracts_ShouldReturnListView() {
        // Arrange
        when(contractService.listAllContracts()).thenReturn(Arrays.asList(contract));

        // Act
        String result = contractController.showAllContracts(model);

        // Assert
        assertEquals("contract/list", result);
    }

    @Test
    void testShowAllContracts_ShouldAddContractsToModel() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.listAllContracts()).thenReturn(contracts);

        // Act
        contractController.showAllContracts(model);

        // Assert
        verify(model).addAttribute("contracts", contracts);
    }

    @Test
    void testShowFormAddContract_ShouldReturnAddView() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Collections.singletonList(customer));
        when(userService.listAllUsers()).thenReturn(Collections.singletonList(user));

        // Act
        String result = contractController.showFormAddContract(model);

        // Assert
        assertEquals("contract/add", result);
    }

    @Test
    void testShowFormAddContract_ShouldAddAttributesToModel() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Collections.singletonList(customer));
        when(userService.listAllUsers()).thenReturn(Collections.singletonList(user));

        // Act
        contractController.showFormAddContract(model);

        // Assert
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute(eq("customers"), any());
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testProcessRequestAddContract_WithValidationErrors_ShouldRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = contractController.processRequestAddContract(contract, bindingResult);

        // Assert
        assertEquals("redirect:/contract/add", result);
    }

    @Test
    void testProcessRequestAddContract_WithoutErrors_ShouldSaveContract() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        contractController.processRequestAddContract(contract, bindingResult);

        // Assert
        verify(contractService).saveContract(contract);
    }

    @Test
    void testProcessRequestAddContract_WithoutErrors_ShouldReturnSuccessView() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = contractController.processRequestAddContract(contract, bindingResult);

        // Assert
        assertEquals("contract/success", result);
    }

    @Test
    void testShowFormEditContract_ShouldReturnEditView() {
        // Arrange
        when(contractService.showContract(1L)).thenReturn(contract);

        // Act
        String result = contractController.showFormEditContract(model, 1L);

        // Assert
        assertEquals("contract/edit", result);
    }

    @Test
    void testProcessRequestEditContract_WithValidationErrors_ShouldRedirect() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String result = contractController.processRequestEditContract(1L, contract, bindingResult);

        // Assert
        assertEquals("redirect:/contract/edit/1", result);
    }

    @Test
    void testProcessRequestEditContract_WithoutErrors_ShouldRedirectToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String result = contractController.processRequestEditContract(1L, contract, bindingResult);

        // Assert
        assertEquals("redirect:/contract/list", result);
    }

    @Test
    void testShowNameSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showNameSearchForm(model);

        // Assert
        assertEquals("contract/name-search", result);
    }

    @Test
    void testProcessRequestNameSearch_ShouldFindByName() {
        // Arrange
        when(contractService.findByName("Test Contract")).thenReturn(contract);

        // Act
        String result = contractController.processRequestNameSearch(contract, model);

        // Assert
        assertEquals("contract/show-one", result);
        verify(contractService).findByName("Test Contract");
    }

    @Test
    void testShowValueLessThanEqualSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showValueLeesThanEqualSearchForm(model);

        // Assert
        assertEquals("contract/value-le-search", result);
    }

    @Test
    void testProcessRequestValueLessThanEqualSearch_ShouldFindByValue() {
        // Arrange
        when(contractService.findAllByValueLessThanEqual(BigDecimal.valueOf(10000.0)))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestValueLessThanEqualSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowValueGreaterThanEqualSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showValueGreaterThanEqualSearchForm(model);

        // Assert
        assertEquals("contract/value-ge-search", result);
    }

    @Test
    void testProcessRequestValueGreaterThanEqualSearch_ShouldFindByValue() {
        // Arrange
        when(contractService.findAllByValueGreaterThanEqual(BigDecimal.valueOf(10000.0)))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestValueGreaterThanEqualSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowBeginDateSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showBeginDateSearchForm(model);

        // Assert
        assertEquals("contract/begin-date-search", result);
    }

    @Test
    void testProcessRequestBeginDateSearch_ShouldFindByBeginDate() {
        // Arrange
        LocalDate date = LocalDate.now();
        contract.setBeginDate(date);
        when(contractService.findAllByBeginDate(date))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestBeginDateSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowBeginDateBeforeSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showBeginDateBeforeSearchForm(model);

        // Assert
        assertEquals("contract/begin-date-before-search", result);
    }

    @Test
    void testProcessRequestBeginDateBeforeSearch_ShouldFindByBeginDateBefore() {
        // Arrange
        LocalDate date = LocalDate.now();
        contract.setBeginDate(date);
        when(contractService.findAllByBeginDateBefore(date))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestBeginDateBeforeSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowBeginDateAfterSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showBeginDateAfterSearchForm(model);

        // Assert
        assertEquals("contract/begin-date-after-search", result);
    }

    @Test
    void testProcessRequestBeginDateAfterSearch_ShouldFindByBeginDateAfter() {
        // Arrange
        LocalDate date = LocalDate.now();
        contract.setBeginDate(date);
        when(contractService.findAllByBeginDateAfter(date))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestBeginDateAfterSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowEndDateSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showEndDateSearchForm(model);

        // Assert
        assertEquals("contract/end-date-search", result);
    }

    @Test
    void testProcessRequestEndDateSearch_ShouldFindByEndDate() {
        // Arrange
        LocalDate date = LocalDate.now();
        contract.setEndDate(date);
        when(contractService.findAllByEndDate(date))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestEndDateSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowStatusSearchForm_ShouldReturnSearchView() {
        // Arrange & Act
        String result = contractController.showStatusSearchForm(model);

        // Assert
        assertEquals("contract/status-search", result);
    }

    @Test
    void testProcessRequestStatusSearch_ShouldFindByStatus() {
        // Arrange
        when(contractService.findAllByStatus(Status.PROPOSED))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestStatusSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowCustomerSearchForm_ShouldReturnSearchView() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Collections.singletonList(customer));

        // Act
        String result = contractController.showCustomerSearchForm(model);

        // Assert
        assertEquals("contract/customer-search", result);
    }

    @Test
    void testProcessRequestCustomerSearch_ShouldFindByCustomer() {
        // Arrange
        when(contractService.findAllByCustomer(customer))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestCustomerSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowCustomerUserSearchForm_ShouldReturnSearchView() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Collections.singletonList(customer));
        when(userService.listAllUsers()).thenReturn(Collections.singletonList(user));

        // Act
        String result = contractController.showCustomerUserSearchForm(model);

        // Assert
        assertEquals("contract/customer-user-search", result);
    }

    @Test
    void testProcessRequestCustomerUserSearch_ShouldFindByCustomerAndUser() {
        // Arrange
        when(contractService.findAllByCustomerAndUser(customer, user))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestCustomerUserSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowUserSearchForm_ShouldReturnSearchView() {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Collections.singletonList(user));

        // Act
        String result = contractController.showUserSearchForm(model);

        // Assert
        assertEquals("contract/user-search", result);
    }

    @Test
    void testProcessRequestUserSearch_ShouldFindByUser() {
        // Arrange
        when(contractService.findAllByUser(user))
                .thenReturn(Collections.singletonList(contract));

        // Act
        String result = contractController.processRequestUserSearch(contract, model);

        // Assert
        assertEquals("contract/show-list", result);
    }
}

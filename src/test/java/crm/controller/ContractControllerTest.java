package crm.controller;

import crm.entity.*;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractControllerTest {

    @Mock
    private ContractService contractService;

    @Mock
    private CustomerService customerService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ContractController contractController;

    private Contract contract;
    private Customer customer;
    private User user;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .role(role)
                .build();

        customer = Customer.builder()
                .id(1L)
                .name("Acme Corp")
                .email("acme@example.com")
                .build();

        contract = Contract.builder()
                .id(1L)
                .name("Contract A")
                .value(new BigDecimal("1000.00"))
                .beginDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        // Arrange & Act
        ContractController controller = new ContractController(contractService, customerService, userService);
        // Assert
        assertNotNull(controller);
    }

    @Test
    void testShowAllContracts_returnsContractListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.listAllContracts()).thenReturn(contracts);
        // Act
        String viewName = contractController.showAllContracts(model);
        // Assert
        assertEquals("contract/list", viewName);
        verify(model).addAttribute(eq("contracts"), any());
    }

    @Test
    void testShowFormAddContract_returnsAddView() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));
        // Act
        String viewName = contractController.showFormAddContract(model);
        // Assert
        assertEquals("contract/add", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute(eq("customers"), any());
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testProcessRequestAddContract_withNoErrors_returnsSuccessView() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        // Act
        String viewName = contractController.processRequestAddContract(contract, bindingResult);
        // Assert
        assertEquals("contract/success", viewName);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testProcessRequestAddContract_withErrors_redirectsToAdd() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = contractController.processRequestAddContract(contract, bindingResult);
        // Assert
        assertEquals("redirect:/contract/add", viewName);
        verify(contractService, never()).saveContract(any());
    }

    @Test
    void testShowFormEditContract_returnsEditView() {
        // Arrange
        when(contractService.showContract(1L)).thenReturn(contract);
        // Act
        String viewName = contractController.showFormEditContract(model, 1L);
        // Assert
        assertEquals("contract/edit", viewName);
        verify(model).addAttribute(eq("contract"), eq(contract));
    }

    @Test
    void testProcessRequestEditContract_withNoErrors_redirectsToList() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        // Act
        String viewName = contractController.processRequestEditContract(1L, contract, bindingResult);
        // Assert
        assertEquals("redirect:/contract/list", viewName);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testProcessRequestEditContract_withErrors_redirectsToEdit() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);
        // Act
        String viewName = contractController.processRequestEditContract(1L, contract, bindingResult);
        // Assert
        assertEquals("redirect:/contract/edit/1", viewName);
        verify(contractService, never()).saveContract(any());
    }

    @Test
    void testShowNameSearchForm_returnsNameSearchView() {
        // Arrange & Act
        String viewName = contractController.showNameSearchForm(model);
        // Assert
        assertEquals("contract/name-search", viewName);
    }

    @Test
    void testProcessRequestNameSearch_returnsShowOneView() {
        // Arrange
        when(contractService.findByName("Contract A")).thenReturn(contract);
        // Act
        String viewName = contractController.processRequestNameSearch(contract, model);
        // Assert
        assertEquals("contract/show-one", viewName);
        verify(model).addAttribute(eq("contract"), eq(contract));
    }

    @Test
    void testShowValueLessThanEqualSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showValueLeesThanEqualSearchForm(model);
        // Assert
        assertEquals("contract/value-le-search", viewName);
    }

    @Test
    void testProcessRequestValueLessThanEqualSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByValueLessThanEqual(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestValueLessThanEqualSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowValueGreaterThanEqualSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showValueGreaterThanEqualSearchForm(model);
        // Assert
        assertEquals("contract/value-ge-search", viewName);
    }

    @Test
    void testProcessRequestValueGreaterThanEqualSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByValueGreaterThanEqual(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestValueGreaterThanEqualSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowBeginDateSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showBeginDateSearchForm(model);
        // Assert
        assertEquals("contract/begin-date-search", viewName);
    }

    @Test
    void testProcessRequestBeginDateSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByBeginDate(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestBeginDateSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowBeginDateBeforeSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showBeginDateBeforeSearchForm(model);
        // Assert
        assertEquals("contract/begin-date-before-search", viewName);
    }

    @Test
    void testProcessRequestBeginDateBeforeSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByBeginDateBefore(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestBeginDateBeforeSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowBeginDateAfterSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showBeginDateAfterSearchForm(model);
        // Assert
        assertEquals("contract/begin-date-after-search", viewName);
    }

    @Test
    void testProcessRequestBeginDateAfterSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByBeginDateAfter(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestBeginDateAfterSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowEndDateSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showEndDateSearchForm(model);
        // Assert
        assertEquals("contract/end-date-search", viewName);
    }

    @Test
    void testProcessRequestEndDateSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByEndDate(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestEndDateSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowEndDateBeforeSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showEndDateBeforeSearchForm(model);
        // Assert
        assertEquals("contract/end-date-before-search", viewName);
    }

    @Test
    void testProcessRequestEndDateBeforeSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByEndDateBefore(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestEndDateBeforeSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowEndDateAfterSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showEndDateAfterSearchForm(model);
        // Assert
        assertEquals("contract/end-date-after-search", viewName);
    }

    @Test
    void testProcessRequestEndDateAfterSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByEndDateAfter(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestEndDateAfterSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowStatusSearchForm_returnsView() {
        // Arrange & Act
        String viewName = contractController.showStatusSearchForm(model);
        // Assert
        assertEquals("contract/status-search", viewName);
    }

    @Test
    void testProcessRequestStatusSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestStatusSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowCustomerSearchForm_returnsView() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        // Act
        String viewName = contractController.showCustomerSearchForm(model);
        // Assert
        assertEquals("contract/customer-search", viewName);
    }

    @Test
    void testProcessRequestCustomerSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByCustomer(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestCustomerSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowCustomerUserSearchForm_returnsView() {
        // Arrange
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));
        // Act
        String viewName = contractController.showCustomerUserSearchForm(model);
        // Assert
        assertEquals("contract/customer-user-search", viewName);
    }

    @Test
    void testProcessRequestCustomerUserSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByCustomerAndUser(any(), any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestCustomerUserSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }

    @Test
    void testShowUserSearchForm_returnsView() {
        // Arrange
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));
        // Act
        String viewName = contractController.showUserSearchForm(model);
        // Assert
        assertEquals("contract/user-search", viewName);
    }

    @Test
    void testProcessRequestUserSearch_returnsShowListView() {
        // Arrange
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByUser(any())).thenReturn(contracts);
        // Act
        String viewName = contractController.processRequestUserSearch(contract, model);
        // Assert
        assertEquals("contract/show-list", viewName);
    }
}

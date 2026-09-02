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
        customer = Customer.builder().id(1L).name("TestCo").build();
        user = User.builder().id(1L).username("testuser").build();
        contract = Contract.builder()
                .id(1L)
                .name("TestContract")
                .value(BigDecimal.valueOf(1000))
                .status(Status.PROPOSED)
                .customer(customer)
                .user(user)
                .build();
    }

    @Test
    void testConstructor_createsInstance() {
        ContractController controller = new ContractController(contractService, customerService, userService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllContracts_returnsContractListView() {
        when(contractService.listAllContracts()).thenReturn(Arrays.asList(contract));
        String view = contractController.showAllContracts(model);
        assertEquals("contract/list", view);
        verify(model).addAttribute(eq("contracts"), any());
    }

    @Test
    void testShowFormAddContract_returnsAddView() {
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));
        String view = contractController.showFormAddContract(model);
        assertEquals("contract/add", view);
    }

    @Test
    void testProcessRequestAddContract_withErrors_redirectsToAdd() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = contractController.processRequestAddContract(contract, bindingResult);
        assertEquals("redirect:/contract/add", view);
    }

    @Test
    void testProcessRequestAddContract_withoutErrors_returnsSuccessView() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = contractController.processRequestAddContract(contract, bindingResult);
        assertEquals("contract/success", view);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testShowFormEditContract_returnsEditView() {
        when(contractService.showContract(1L)).thenReturn(contract);
        String view = contractController.showFormEditContract(model, 1L);
        assertEquals("contract/edit", view);
    }

    @Test
    void testProcessRequestEditContract_withErrors_redirectsToEdit() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = contractController.processRequestEditContract(1L, contract, bindingResult);
        assertEquals("redirect:/contract/edit/1", view);
    }

    @Test
    void testProcessRequestEditContract_withoutErrors_redirectsToList() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String view = contractController.processRequestEditContract(1L, contract, bindingResult);
        assertEquals("redirect:/contract/list", view);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testShowNameSearchForm_returnsNameSearchView() {
        String view = contractController.showNameSearchForm(model);
        assertEquals("contract/name-search", view);
    }

    @Test
    void testProcessRequestNameSearch_returnsShowOneView() {
        when(contractService.findByName("TestContract")).thenReturn(contract);
        String view = contractController.processRequestNameSearch(contract, model);
        assertEquals("contract/show-one", view);
    }

    @Test
    void testShowValueLessThanEqualSearchForm_returnsView() {
        String view = contractController.showValueLeesThanEqualSearchForm(model);
        assertEquals("contract/value-le-search", view);
    }

    @Test
    void testProcessRequestValueLessThanEqualSearch_returnsShowListView() {
        when(contractService.findAllByValueLessThanEqual(BigDecimal.valueOf(1000))).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestValueLessThanEqualSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowValueGreaterThanEqualSearchForm_returnsView() {
        String view = contractController.showValueGreaterThanEqualSearchForm(model);
        assertEquals("contract/value-ge-search", view);
    }

    @Test
    void testProcessRequestValueGreaterThanEqualSearch_returnsShowListView() {
        when(contractService.findAllByValueGreaterThanEqual(BigDecimal.valueOf(1000))).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestValueGreaterThanEqualSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowBeginDateSearchForm_returnsView() {
        String view = contractController.showBeginDateSearchForm(model);
        assertEquals("contract/begin-date-search", view);
    }

    @Test
    void testProcessRequestBeginDateSearch_returnsShowListView() {
        contract.setBeginDate(LocalDate.of(2023, 1, 1));
        when(contractService.findAllByBeginDate(any())).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestBeginDateSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowBeginDateBeforeSearchForm_returnsView() {
        String view = contractController.showBeginDateBeforeSearchForm(model);
        assertEquals("contract/begin-date-before-search", view);
    }

    @Test
    void testProcessRequestBeginDateBeforeSearch_returnsShowListView() {
        contract.setBeginDate(LocalDate.of(2023, 6, 1));
        when(contractService.findAllByBeginDateBefore(any())).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestBeginDateBeforeSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowBeginDateAfterSearchForm_returnsView() {
        String view = contractController.showBeginDateAfterSearchForm(model);
        assertEquals("contract/begin-date-after-search", view);
    }

    @Test
    void testProcessRequestBeginDateAfterSearch_returnsShowListView() {
        contract.setBeginDate(LocalDate.of(2022, 1, 1));
        when(contractService.findAllByBeginDateAfter(any())).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestBeginDateAfterSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowEndDateSearchForm_returnsView() {
        String view = contractController.showEndDateSearchForm(model);
        assertEquals("contract/end-date-search", view);
    }

    @Test
    void testProcessRequestEndDateSearch_returnsShowListView() {
        contract.setEndDate(LocalDate.of(2023, 12, 31));
        when(contractService.findAllByEndDate(any())).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestEndDateSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowEndDateBeforeSearchForm_returnsView() {
        String view = contractController.showEndDateBeforeSearchForm(model);
        assertEquals("contract/end-date-before-search", view);
    }

    @Test
    void testProcessRequestEndDateBeforeSearch_returnsShowListView() {
        contract.setEndDate(LocalDate.of(2024, 1, 1));
        when(contractService.findAllByEndDateBefore(any())).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestEndDateBeforeSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowEndDateAfterSearchForm_returnsView() {
        String view = contractController.showEndDateAfterSearchForm(model);
        assertEquals("contract/end-date-after-search", view);
    }

    @Test
    void testProcessRequestEndDateAfterSearch_returnsShowListView() {
        contract.setEndDate(LocalDate.of(2022, 12, 31));
        when(contractService.findAllByEndDateAfter(any())).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestEndDateAfterSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowStatusSearchForm_returnsView() {
        String view = contractController.showStatusSearchForm(model);
        assertEquals("contract/status-search", view);
    }

    @Test
    void testProcessRequestStatusSearch_returnsShowListView() {
        when(contractService.findAllByStatus(Status.PROPOSED)).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestStatusSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowCustomerSearchForm_returnsView() {
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        String view = contractController.showCustomerSearchForm(model);
        assertEquals("contract/customer-search", view);
    }

    @Test
    void testProcessRequestCustomerSearch_returnsShowListView() {
        when(contractService.findAllByCustomer(customer)).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestCustomerSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowCustomerUserSearchForm_returnsView() {
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList(customer));
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));
        String view = contractController.showCustomerUserSearchForm(model);
        assertEquals("contract/customer-user-search", view);
    }

    @Test
    void testProcessRequestCustomerUserSearch_returnsShowListView() {
        when(contractService.findAllByCustomerAndUser(customer, user)).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestCustomerUserSearch(contract, model);
        assertEquals("contract/show-list", view);
    }

    @Test
    void testShowUserSearchForm_returnsView() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList(user));
        String view = contractController.showUserSearchForm(model);
        assertEquals("contract/user-search", view);
    }

    @Test
    void testProcessRequestUserSearch_returnsShowListView() {
        when(contractService.findAllByUser(user)).thenReturn(Arrays.asList(contract));
        String view = contractController.processRequestUserSearch(contract, model);
        assertEquals("contract/show-list", view);
    }
}

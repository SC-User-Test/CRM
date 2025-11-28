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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ContractControllerTest {

    @InjectMocks
    private ContractController contractController;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testContractControllerConstructor() {
        ContractController controller = new ContractController(contractService, customerService, userService);
        assertNotNull(controller);
    }

    @Test
    void testShowAllContracts() {
        when(contractService.listAllContracts()).thenReturn(Arrays.asList());

        String viewName = contractController.showAllContracts(model);

        assertEquals("contract/list", viewName);
        verify(model).addAttribute(eq("contracts"), any());
        verify(contractService).listAllContracts();
    }

    @Test
    void testShowFormAddContract() {
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList());
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        String viewName = contractController.showFormAddContract(model);

        assertEquals("contract/add", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute(eq("customers"), any());
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testProcessRequestAddContractWithValidData() {
        Contract contract = Contract.builder()
                .name("Test Contract")
                .value(new BigDecimal("10000"))
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = contractController.processRequestAddContract(contract, bindingResult);

        assertEquals("contract/success", viewName);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testProcessRequestAddContractWithErrors() {
        Contract contract = Contract.builder().build();

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = contractController.processRequestAddContract(contract, bindingResult);

        assertEquals("redirect:/contract/add", viewName);
        verify(contractService, never()).saveContract(any());
    }

    @Test
    void testShowFormEditContract() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Edit Contract")
                .build();

        when(contractService.showContract(1L)).thenReturn(contract);

        String viewName = contractController.showFormEditContract(model, 1L);

        assertEquals("contract/edit", viewName);
        verify(model).addAttribute("contract", contract);
    }

    @Test
    void testProcessRequestEditContractWithValidData() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Updated Contract")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = contractController.processRequestEditContract(1L, contract, bindingResult);

        assertEquals("redirect:/contract/list", viewName);
        verify(contractService).saveContract(contract);
    }

    @Test
    void testShowNameSearchForm() {
        String viewName = contractController.showNameSearchForm(model);

        assertEquals("contract/name-search", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
    }

    @Test
    void testProcessRequestNameSearch() {
        Contract searchContract = Contract.builder().name("Test").build();
        Contract foundContract = Contract.builder().id(1L).name("Test").build();

        when(contractService.findByName("Test")).thenReturn(foundContract);

        String viewName = contractController.processRequestNameSearch(searchContract, model);

        assertEquals("contract/show-one", viewName);
        verify(model).addAttribute("contract", foundContract);
    }

    @Test
    void testProcessRequestValueLessThanEqualSearch() {
        Contract searchContract = Contract.builder().value(new BigDecimal("10000")).build();

        when(contractService.findAllByValueLessThanEqual(any())).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestValueLessThanEqualSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByValueLessThanEqual(searchContract.getValue());
    }

    @Test
    void testProcessRequestValueGreaterThanEqualSearch() {
        Contract searchContract = Contract.builder().value(new BigDecimal("50000")).build();

        when(contractService.findAllByValueGreaterThanEqual(any())).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestValueGreaterThanEqualSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByValueGreaterThanEqual(searchContract.getValue());
    }

    @Test
    void testProcessRequestBeginDateSearch() {
        LocalDate beginDate = LocalDate.of(2025, 1, 1);
        Contract searchContract = Contract.builder().beginDate(beginDate).build();

        when(contractService.findAllByBeginDate(beginDate)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestBeginDateSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByBeginDate(beginDate);
    }

    @Test
    void testProcessRequestBeginDateBeforeSearch() {
        LocalDate beforeDate = LocalDate.of(2025, 6, 1);
        Contract searchContract = Contract.builder().beginDate(beforeDate).build();

        when(contractService.findAllByBeginDateBefore(beforeDate)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestBeginDateBeforeSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByBeginDateBefore(beforeDate);
    }

    @Test
    void testProcessRequestBeginDateAfterSearch() {
        LocalDate afterDate = LocalDate.of(2024, 12, 31);
        Contract searchContract = Contract.builder().beginDate(afterDate).build();

        when(contractService.findAllByBeginDateAfter(afterDate)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestBeginDateAfterSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByBeginDateAfter(afterDate);
    }

    @Test
    void testProcessRequestEndDateSearch() {
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        Contract searchContract = Contract.builder().endDate(endDate).build();

        when(contractService.findAllByEndDate(endDate)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestEndDateSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByEndDate(endDate);
    }

    @Test
    void testProcessRequestStatusSearch() {
        Contract searchContract = Contract.builder().status(Status.PROPOSED).build();

        when(contractService.findAllByStatus(Status.PROPOSED)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestStatusSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByStatus(Status.PROPOSED);
    }

    @Test
    void testShowCustomerSearchForm() {
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList());

        String viewName = contractController.showCustomerSearchForm(model);

        assertEquals("contract/customer-search", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute(eq("customers"), any());
    }

    @Test
    void testProcessRequestCustomerSearch() {
        Customer customer = Customer.builder().id(1L).build();
        Contract searchContract = Contract.builder().customer(customer).build();

        when(contractService.findAllByCustomer(customer)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestCustomerSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByCustomer(customer);
    }

    @Test
    void testShowCustomerUserSearchForm() {
        when(customerService.findAllByEnabledTrue()).thenReturn(Arrays.asList());
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        String viewName = contractController.showCustomerUserSearchForm(model);

        assertEquals("contract/customer-user-search", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute(eq("customers"), any());
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testProcessRequestCustomerUserSearch() {
        Customer customer = Customer.builder().id(1L).build();
        User user = User.builder().id(1L).build();
        Contract searchContract = Contract.builder().customer(customer).user(user).build();

        when(contractService.findAllByCustomerAndUser(customer, user)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestCustomerUserSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByCustomerAndUser(customer, user);
    }

    @Test
    void testShowUserSearchForm() {
        when(userService.listAllUsers()).thenReturn(Arrays.asList());

        String viewName = contractController.showUserSearchForm(model);

        assertEquals("contract/user-search", viewName);
        verify(model).addAttribute(eq("contract"), any(Contract.class));
        verify(model).addAttribute(eq("users"), any());
    }

    @Test
    void testProcessRequestUserSearch() {
        User user = User.builder().id(1L).build();
        Contract searchContract = Contract.builder().user(user).build();

        when(contractService.findAllByUser(user)).thenReturn(Arrays.asList());

        String viewName = contractController.processRequestUserSearch(searchContract, model);

        assertEquals("contract/show-list", viewName);
        verify(contractService).findAllByUser(user);
    }
}

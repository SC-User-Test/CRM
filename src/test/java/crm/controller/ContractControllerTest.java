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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        when(contractService.listAllContracts()).thenReturn(contracts);

        String result = contractController.showAllContracts(model);

        assertEquals("contract/list", result);
        verify(model, times(1)).addAttribute("contracts", contracts);
    }

    @Test
    void testShowFormAddContract() {
        List<Customer> customers = new ArrayList<>();
        List<User> users = new ArrayList<>();
        when(customerService.findAllByEnabledTrue()).thenReturn(customers);
        when(userService.listAllUsers()).thenReturn(users);

        String result = contractController.showFormAddContract(model);

        assertEquals("contract/add", result);
        verify(model, times(1)).addAttribute(eq("contract"), any(Contract.class));
        verify(model, times(1)).addAttribute("customers", customers);
        verify(model, times(1)).addAttribute("users", users);
    }

    @Test
    void testProcessRequestAddContractSuccess() {
        Contract contract = new Contract();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = contractController.processRequestAddContract(contract, bindingResult);

        assertEquals("contract/success", result);
        verify(contractService, times(1)).saveContract(contract);
    }

    @Test
    void testProcessRequestAddContractWithErrors() {
        Contract contract = new Contract();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = contractController.processRequestAddContract(contract, bindingResult);

        assertEquals("redirect:/contract/add", result);
    }

    @Test
    void testShowFormEditContract() {
        Contract contract = new Contract();
        contract.setId(1L);
        when(contractService.showContract(1L)).thenReturn(contract);

        String result = contractController.showFormEditContract(model, 1L);

        assertEquals("contract/edit", result);
        verify(model, times(1)).addAttribute("contract", contract);
    }

    @Test
    void testProcessRequestEditContractSuccess() {
        Contract contract = new Contract();
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = contractController.processRequestEditContract(1L, contract, bindingResult);

        assertEquals("redirect:/contract/list", result);
        verify(contractService, times(1)).saveContract(contract);
    }

    @Test
    void testShowNameSearchForm() {
        String result = contractController.showNameSearchForm(model);

        assertEquals("contract/name-search", result);
        verify(model, times(1)).addAttribute(eq("contract"), any(Contract.class));
    }

    @Test
    void testProcessRequestNameSearch() {
        Contract contract = new Contract();
        contract.setName("TestContract");
        when(contractService.findByName("TestContract")).thenReturn(contract);

        String result = contractController.processRequestNameSearch(contract, model);

        assertEquals("contract/show-one", result);
        verify(model, times(1)).addAttribute("contract", contract);
    }

    @Test
    void testShowValueLessThanEqualSearchForm() {
        String result = contractController.showValueLeesThanEqualSearchForm(model);

        assertEquals("contract/value-le-search", result);
    }

    @Test
    void testProcessRequestValueLessThanEqualSearch() {
        Contract contract = new Contract();
        contract.setValue(new BigDecimal("1000"));
        List<Contract> contracts = new ArrayList<>();
        when(contractService.findAllByValueLessThanEqual(contract.getValue())).thenReturn(contracts);

        String result = contractController.processRequestValueLessThanEqualSearch(contract, model);

        assertEquals("contract/show-list", result);
        verify(model, times(1)).addAttribute("contracts", contracts);
    }

    @Test
    void testShowBeginDateSearchForm() {
        String result = contractController.showBeginDateSearchForm(model);

        assertEquals("contract/begin-date-search", result);
    }

    @Test
    void testProcessRequestBeginDateSearch() {
        Contract contract = new Contract();
        contract.setBeginDate(LocalDate.now());
        List<Contract> contracts = new ArrayList<>();
        when(contractService.findAllByBeginDate(contract.getBeginDate())).thenReturn(contracts);

        String result = contractController.processRequestBeginDateSearch(contract, model);

        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowStatusSearchForm() {
        String result = contractController.showStatusSearchForm(model);

        assertEquals("contract/status-search", result);
    }

    @Test
    void testProcessRequestStatusSearch() {
        Contract contract = new Contract();
        contract.setStatus(Status.PROPOSED);
        List<Contract> contracts = new ArrayList<>();
        when(contractService.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        String result = contractController.processRequestStatusSearch(contract, model);

        assertEquals("contract/show-list", result);
    }

    @Test
    void testShowCustomerSearchForm() {
        List<Customer> customers = new ArrayList<>();
        when(customerService.findAllByEnabledTrue()).thenReturn(customers);

        String result = contractController.showCustomerSearchForm(model);

        assertEquals("contract/customer-search", result);
        verify(model, times(1)).addAttribute("customers", customers);
    }

    @Test
    void testShowUserSearchForm() {
        List<User> users = new ArrayList<>();
        when(userService.listAllUsers()).thenReturn(users);

        String result = contractController.showUserSearchForm(model);

        assertEquals("contract/user-search", result);
        verify(model, times(1)).addAttribute("users", users);
    }
}

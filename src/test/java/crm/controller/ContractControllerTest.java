package crm.controller;

import crm.entity.Contract;
import crm.entity.Customer;
import crm.entity.Status;
import crm.entity.User;
import crm.service.ContractService;
import crm.service.CustomerService;
import crm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContractControllerTest {

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

    private ContractController contractController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        contractController = new ContractController(contractService, customerService, userService);
    }

    @Test
    public void testContractControllerCreation() {
        assertNotNull(contractController);
    }

    @Test
    public void testShowAllContracts() {
        List<Contract> contracts = Arrays.asList(
                Contract.builder().id(1L).name("Contract1").build(),
                Contract.builder().id(2L).name("Contract2").build()
        );
        when(contractService.listAllContracts()).thenReturn(contracts);

        String result = contractController.showAllContracts(model);
        assertEquals("contract/list", result);
        verify(contractService, times(1)).listAllContracts();
        verify(model, times(1)).addAttribute("contracts", contracts);
    }

    @Test
    public void testShowFormAddContract() {
        List<Customer> customers = Arrays.asList(Customer.builder().id(1L).build());
        List<User> users = Arrays.asList(User.builder().id(1L).build());

        when(customerService.findAllByEnabledTrue()).thenReturn(customers);
        when(userService.listAllUsers()).thenReturn(users);

        String result = contractController.showFormAddContract(model);
        assertEquals("contract/add", result);
        verify(customerService, times(1)).findAllByEnabledTrue();
        verify(userService, times(1)).listAllUsers();
    }

    @Test
    public void testProcessRequestAddContractSuccess() {
        Contract contract = Contract.builder()
                .name("New Contract")
                .value(new BigDecimal("10000"))
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = contractController.processRequestAddContract(contract, bindingResult);
        assertEquals("contract/success", result);
        verify(contractService, times(1)).saveContract(contract);
    }

    @Test
    public void testProcessRequestAddContractValidationErrors() {
        Contract contract = Contract.builder().build();
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = contractController.processRequestAddContract(contract, bindingResult);
        assertEquals("redirect:/contract/add", result);
        verify(contractService, never()).saveContract(contract);
    }

    @Test
    public void testShowFormEditContract() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .build();
        when(contractService.showContract(1L)).thenReturn(contract);

        String result = contractController.showFormEditContract(model, 1L);
        assertEquals("contract/edit", result);
        verify(contractService, times(1)).showContract(1L);
    }

    @Test
    public void testProcessRequestEditContractSuccess() {
        Contract contract = Contract.builder()
                .id(1L)
                .name("Updated Contract")
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = contractController.processRequestEditContract(1L, contract, bindingResult);
        assertEquals("redirect:/contract/list", result);
        verify(contractService, times(1)).saveContract(contract);
    }

    @Test
    public void testShowNameSearchForm() {
        String result = contractController.showNameSearchForm(model);
        assertEquals("contract/name-search", result);
        verify(model, times(1)).addAttribute(eq("contract"), any(Contract.class));
    }

    @Test
    public void testProcessRequestNameSearch() {
        Contract contract = Contract.builder().name("Test Contract").build();
        Contract foundContract = Contract.builder()
                .id(1L)
                .name("Test Contract")
                .build();
        when(contractService.findByName("Test Contract")).thenReturn(foundContract);

        String result = contractController.processRequestNameSearch(contract, model);
        assertEquals("contract/show-one", result);
        verify(contractService, times(1)).findByName("Test Contract");
    }

    @Test
    public void testProcessRequestValueLessThanEqualSearch() {
        Contract contract = Contract.builder()
                .value(new BigDecimal("10000"))
                .build();
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByValueLessThanEqual(new BigDecimal("10000"))).thenReturn(contracts);

        String result = contractController.processRequestValueLessThanEqualSearch(contract, model);
        assertEquals("contract/show-list", result);
        verify(contractService, times(1)).findAllByValueLessThanEqual(new BigDecimal("10000"));
    }

    @Test
    public void testProcessRequestBeginDateSearch() {
        LocalDate beginDate = LocalDate.of(2024, 1, 1);
        Contract contract = Contract.builder()
                .beginDate(beginDate)
                .build();
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByBeginDate(beginDate)).thenReturn(contracts);

        String result = contractController.processRequestBeginDateSearch(contract, model);
        assertEquals("contract/show-list", result);
        verify(contractService, times(1)).findAllByBeginDate(beginDate);
    }

    @Test
    public void testProcessRequestStatusSearch() {
        Contract contract = Contract.builder()
                .status(Status.PROPOSED)
                .build();
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByStatus(Status.PROPOSED)).thenReturn(contracts);

        String result = contractController.processRequestStatusSearch(contract, model);
        assertEquals("contract/show-list", result);
        verify(contractService, times(1)).findAllByStatus(Status.PROPOSED);
    }

    @Test
    public void testShowCustomerSearchForm() {
        List<Customer> customers = Arrays.asList(Customer.builder().id(1L).build());
        when(customerService.findAllByEnabledTrue()).thenReturn(customers);

        String result = contractController.showCustomerSearchForm(model);
        assertEquals("contract/customer-search", result);
    }

    @Test
    public void testProcessRequestCustomerSearch() {
        Customer customer = Customer.builder().id(1L).build();
        Contract contract = Contract.builder()
                .customer(customer)
                .build();
        List<Contract> contracts = Arrays.asList(contract);
        when(contractService.findAllByCustomer(customer)).thenReturn(contracts);

        String result = contractController.processRequestCustomerSearch(contract, model);
        assertEquals("contract/show-list", result);
        verify(contractService, times(1)).findAllByCustomer(customer);
    }
}

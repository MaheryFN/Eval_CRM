package site.easy.to.build.crm.controller;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;
import site.easy.to.build.crm.util.FileUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/budget")
public class BudgetCustomerController {
    
    private final AuthenticationUtils authenticationUtils;
    private final CustomerService customerService;
    private final BudgetService budgetService;
    private final UserService userService;
    
    @Autowired
    public BudgetCustomerController(AuthenticationUtils authenticationUtils, CustomerService customerService, BudgetService budgetService, UserService userService) {
        this.authenticationUtils = authenticationUtils;
        this.customerService = customerService;
        this.budgetService = budgetService;
        this.userService = userService;
    }
    
    @GetMapping("/list_budget")
    public String listBudget(Model model, Authentication authentication) {
        int profileId = authenticationUtils.getLoggedInUserId(authentication);
        Customer customer = customerService.findByProfileId(profileId);
        List<Budget> all_budgets = budgetService.findByCustomerCustomerId(customer.getCustomerId());
        BigDecimal totalAmount = budgetService.getTotalMontant(customer.getCustomerId());
        model.addAttribute("all_budgets", all_budgets);
        model.addAttribute("totalAmount", totalAmount);
        return "budgetCustomer/show-my-budget";
    }
    
    @GetMapping("/create_budget")
    public String createBudget(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "budgetCustomer/create-budget";
    }
    
    @PostMapping("/budget-create/create")
    public String createBudget(@ModelAttribute("budget") @Validated Budget budget,
                               @RequestParam("montant") BigDecimal montant,
                               @RequestParam("customerId") int customerId,
                               Model model,
                               Authentication authentication
                               ) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User manager = userService.findById(userId);
        if(manager == null) {
            return "error/500";
        }
        if(manager.isInactiveUser()) {
            return "error/account-inactive";
        }
        Customer customer = customerService.findByCustomerId(customerId);
        
        budget.setCustomer(customer);
        budget.setDateAjout(LocalDate.now());
        budget.setMontant(montant);
        
        budgetService.save(budget);
        
        return "redirect:/budget/create_budget";
    }
}


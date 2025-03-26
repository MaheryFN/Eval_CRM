package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/budget")
public class BudgetCustomerController {
    
    private final AuthenticationUtils authenticationUtils;
    private final CustomerService customerService;
    private final BudgetService budgetService;
    private final UserService userService;
    
    private final DepenseLeadService depenseLeadService;
    private final DepenseTicketService depenseTicketService;
    
    @Autowired
    public BudgetCustomerController(AuthenticationUtils authenticationUtils, CustomerService customerService, BudgetService budgetService, UserService userService, DepenseLeadService depenseLeadService, DepenseTicketService depenseTicketService, LeadService leadService) {
        this.authenticationUtils = authenticationUtils;
        this.customerService = customerService;
        this.budgetService = budgetService;
        this.userService = userService;
        this.depenseLeadService = depenseLeadService;
        this.depenseTicketService = depenseTicketService;
    }
    
    @GetMapping("/list_budget")
    public String listBudget(Model model, Authentication authentication) {
        int profileId = authenticationUtils.getLoggedInUserId(authentication);
        Customer customer = customerService.findByProfileId(profileId);
        List<Budget> all_budgets = budgetService.findByCustomerCustomerId(customer.getCustomerId());
        BigDecimal totalAmountWithDepense = BigDecimal.valueOf(budgetService.calculate_remaining_amount(budgetService, depenseLeadService, depenseTicketService, customer.getCustomerId()));
        totalAmountWithDepense = totalAmountWithDepense.setScale(2, BigDecimal.ROUND_HALF_UP);
        
        BigDecimal totalAmount = budgetService.getTotalMontant(customer.getCustomerId());
        totalAmount = totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        model.addAttribute("all_budgets", all_budgets);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalAmountWithDepense", totalAmountWithDepense);
        return "budgetCustomer/show-my-budget";
    }
    
    @GetMapping("/all-list_budget")
    public String all_list_Budget(Model model, Authentication authentication) {
        List<Budget> all_budgets = budgetService.findAll();
        model.addAttribute("all_budgets", all_budgets);
        return "budgetCustomer/all-budget";
    }
    
    @GetMapping("/create_budget")
    public String createBudget(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "budgetCustomer/create-budget";
    }
    
    @PostMapping("/budget-create/create")
    public String createBudget(@ModelAttribute("budget") @Validated Budget budget,
                               @RequestParam(value = "montant", required = false) BigDecimal montant,
                               @RequestParam("customerId") int customerId,
                               RedirectAttributes redirectAttributes,
                               Authentication authentication,
                               Model model
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
        budget.setDateAjout(LocalDateTime.now());
        
        if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("error", "The amount cannot be empty or negative!");
            return "redirect:/budget/create_budget";
        }
        
        budget.setMontant(montant);
        
        budgetService.save(budget);
        redirectAttributes.addFlashAttribute("message", "The budget has been created!");
        return "redirect:/budget/create_budget";
    }
}


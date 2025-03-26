package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.taux.TauxService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/depenseLead")
public class DepenseLeadController {
    private final AuthenticationUtils authenticationUtils;
    private final DepenseLeadService depenseLeadService;
    private final DepenseTicketService depenseTicketService;
    private final BudgetService budgetCustomerService;
    private final LeadService leadService;
    private final TauxService tauxService;
    private final CustomerService customerService;
    private final BudgetService budgetService;
    
    @Autowired
    public DepenseLeadController(AuthenticationUtils authenticationUtils, DepenseLeadService depenseLeadService,
                                 DepenseTicketService depenseTicketService,
                                 BudgetService budgetCustomerService,
                                 LeadService leadService, TauxService tauxService, CustomerService customerService, BudgetService budgetService) {
        this.authenticationUtils = authenticationUtils;
        this.depenseLeadService = depenseLeadService;
        this.depenseTicketService = depenseTicketService;
        this.budgetCustomerService = budgetCustomerService;
        this.leadService = leadService;
        this.tauxService = tauxService;
        this.customerService = customerService;
        this.budgetService = budgetService;
    }
    
    @GetMapping("/list-depense-lead")
    public String showAllDepenseLead(Model model) {
        List<DepenseLead> depenseLeadList = depenseLeadService.findAll();
        model.addAttribute("depenseLeads", depenseLeadList);
        return "depenseLead/list-depense-lead";
    }
    
    @GetMapping("/my-depense-lead")
    public String listBudget(Model model, Authentication authentication) {
        int profileId = authenticationUtils.getLoggedInUserId(authentication);
        Customer customer = customerService.findByProfileId(profileId);
        List<DepenseLead> depenseLeadList = depenseLeadService.findByCustomerId(customer.getCustomerId());
        double totalAmount = depenseLeadService.getTotalMontantCustomerId(customer.getCustomerId());
        model.addAttribute("all_depenseLeads", depenseLeadList);
        model.addAttribute("totalAmount", totalAmount);
        return "depenseLead/show-my-depense-lead";
    }
    
    @GetMapping("/create-depense-lead")
    public String showDepenseLeadForm(Model model) {
        model.addAttribute("leads", leadService.findLeadWithoutDepense(leadService.findAll(), depenseLeadService.findAll()));
        return "depenseLead/create-depense-lead";
    }
    
    @PostMapping("/check-depense-lead")
    public String checkDepenseLead(Model model,
                                   @RequestParam("leadId") int leadId,
                                   @RequestParam(value="montant", required = false) BigDecimal montant,
                                   RedirectAttributes redirectAttributes) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("error", "The amount cannot be empty or negative!");
            return "redirect:/depenseLead/create-depense-lead";
        }
        
        Lead lead = leadService.findByLeadId(leadId);
        BigDecimal budget = BigDecimal.valueOf(budgetService.calculate_remaining_amount(budgetCustomerService, depenseLeadService, depenseTicketService, lead.getCustomer().getCustomerId()));
        BigDecimal remaining_amount = budget.subtract(montant);
        
        if (remaining_amount.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("leadId", leadId);
            model.addAttribute("montant", montant);
            model.addAttribute("budget", budget);
            model.addAttribute("remaining_amount", remaining_amount);
            
            return "/depenseLead/confirmation-depense-lead";
        }
        
        DepenseLead leadDepense = new DepenseLead();
        leadDepense.setLead(lead);
        leadDepense.setMontant(montant);
        leadDepense.setDateAjout(LocalDateTime.now());
        depenseLeadService.save(leadDepense);
        
        model.addAttribute("leads", leadService.findLeadWithoutDepense(leadService.findAll(), depenseLeadService.findAll()));
        model.addAttribute("message", "DepenseLead inserted successfully !");
        
        Taux taux = tauxService.findLastTaux();
        double pourcentage = taux.getPourcentage().doubleValue() / 100;
        lead = leadService.findByLeadId(leadId);
        double total_budget = (budgetCustomerService.getTotalMontant(lead.getCustomer().getCustomerId()).doubleValue());
        
        double total = total_budget * pourcentage;
        System.out.println("T :" + total + " B : " + remaining_amount);
        if (total >= remaining_amount.doubleValue()) {
            model.addAttribute("taux", "The client " + lead.getCustomer().getName() + " has reached " + taux.getPourcentage()
                + "% of their budget (Remaining: " + budget + ")");
        }
        
        return "depenseLead/create-depense-lead";
    }
    
    @PostMapping("/save-depense-lead")
    public String insertDepenseLead(RedirectAttributes redirectAttributes,
                                    @RequestParam("leadId") int leadId,
                                    @RequestParam("montant") double montant) {
        Lead lead = leadService.findByLeadId(leadId);
        
        DepenseLead leadDepense = new DepenseLead();
        leadDepense.setLead(lead);
        leadDepense.setMontant(BigDecimal.valueOf(montant));
        leadDepense.setDateAjout(LocalDateTime.now());
        
        System.out.println(leadDepense.getDepenseLeadId());
        System.out.println(leadDepense.getMontant());
        System.out.println(leadDepense.getDateAjout());
        
        depenseLeadService.save(leadDepense);
        
        redirectAttributes.addFlashAttribute("message", "DepenseLead inserted successfully !");
        
        BigDecimal budget = BigDecimal.valueOf(budgetService.calculate_remaining_amount(budgetCustomerService, depenseLeadService, depenseTicketService, lead.getCustomer().getCustomerId()));
       // BigDecimal remaining_amount = budget.subtract(BigDecimal.valueOf(montant));
        Taux taux = tauxService.findLastTaux();
        double pourcentage = taux.getPourcentage().doubleValue() / 100;
        lead = leadService.findByLeadId(leadId);
        double total_budget = (budgetCustomerService.getTotalMontant(lead.getCustomer().getCustomerId()).doubleValue());
        
        double total = total_budget * pourcentage;
        if (total >= budget.doubleValue()) {
            redirectAttributes.addFlashAttribute("taux1", "The client " + lead.getCustomer().getName() + " has reached " + taux.getPourcentage().doubleValue()
                + "% of their budget (Remaining: " + budget + ")");
        }
        
        return "redirect:/depenseLead/create-depense-lead";
    }
    
}

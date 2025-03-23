package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.DepenseLead;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.lead.LeadService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/depenseLead")
public class DepenseLeadController {
    private final DepenseLeadService depenseLeadService;
    private final DepenseTicketService depenseTicketService;
    private final BudgetService budgetCustomerService;
    private final LeadService leadService;
    
    @Autowired
    public DepenseLeadController(DepenseLeadService depenseLeadService,
                                 DepenseTicketService depenseTicketService,
                                 BudgetService budgetCustomerService,
                                 LeadService leadService) {
        this.depenseLeadService = depenseLeadService;
        this.depenseTicketService = depenseTicketService;
        this.budgetCustomerService = budgetCustomerService;
        this.leadService = leadService;
    }
    
    @GetMapping("/list-depense-lead")
    public String showAllDepenseLead(Model model) {
        List<DepenseLead> depenseLeadList = depenseLeadService.findAll();
        model.addAttribute("depenseLeads", depenseLeadList);
        return "depenseLead/list-depense-lead";
    }
    
    @GetMapping("/create-depense-lead")
    public String showDepenseLeadForm(Model model) {
        model.addAttribute("leads", Lead.findLeadWithoutDepense(leadService.findAll(), depenseLeadService.findAll()));
        return "depenseLead/create-depense-lead";
    }
    
    @PostMapping("/check-depense-lead")
    public String checkDepenseLead(Model model,
                                   @RequestParam("leadId") int leadId,
                                   @RequestParam("montant") BigDecimal montant) {
        
        if (montant.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("leads", leadService.findAll());
            model.addAttribute("message", "Invalid amount !");
            return "depenseLead/create-depense-lead";
        }
        
        Lead lead = leadService.findByLeadId(leadId);
        BigDecimal budget = BigDecimal.valueOf(Budget.calculate_remaining_amount(budgetCustomerService, depenseLeadService, depenseTicketService, lead.getCustomer().getCustomerId()));
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
        leadDepense.setDateAjout(LocalDate.now());
        depenseLeadService.save(leadDepense);
        
        model.addAttribute("leads", Lead.findLeadWithoutDepense(leadService.findAll(), depenseLeadService.findAll()));
        model.addAttribute("message", "DepenseLead inserted successfully !");
        
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
        leadDepense.setDateAjout(LocalDate.now());
        
        System.out.println(leadDepense.getDepenseLeadId());
        System.out.println(leadDepense.getMontant());
        System.out.println(leadDepense.getDateAjout());
        
        depenseLeadService.save(leadDepense);
        
        redirectAttributes.addFlashAttribute("message", "DepenseLead inserted successfully !");
        return "redirect:/depenseLead/create-depense-lead";
    }
    
}

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
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.taux.TauxService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/depenseTicket")
public class DepenseTicketController {
    private final AuthenticationUtils authenticationUtils;
    private final DepenseLeadService depenseLeadService;
    private final DepenseTicketService depenseTicketService;
    private final BudgetService budgetCustomerService;
    private final TicketService ticketService;
    private final TauxService tauxService;
    private final CustomerService customerService;
    private final BudgetService budgetService;
    
    @Autowired
    public DepenseTicketController(AuthenticationUtils authenticationUtils, DepenseLeadService depenseLeadService,
                                   DepenseTicketService depenseTicketService,
                                   BudgetService budgetCustomerService,
                                   TicketService ticketService, TauxService tauxService, CustomerService customerService, BudgetService budgetService) {
        this.authenticationUtils = authenticationUtils;
        this.depenseLeadService = depenseLeadService;
        this.depenseTicketService = depenseTicketService;
        this.budgetCustomerService = budgetCustomerService;
        this.ticketService = ticketService;
        this.tauxService = tauxService;
        this.customerService = customerService;
        this.budgetService = budgetService;
    }
    
    @GetMapping("/list-depense-ticket")
    public String showAllDepenseTicket(Model model) {
        List<DepenseTicket> depenseTicketList = depenseTicketService.findAll();
        model.addAttribute("depenseTickets", depenseTicketList);
        return "depenseTicket/list-depense-ticket";
    }
    
    @GetMapping("/my-depense-ticket")
    public String listBudget(Model model, Authentication authentication) {
        int profileId = authenticationUtils.getLoggedInUserId(authentication);
        Customer customer = customerService.findByProfileId(profileId);
        List<DepenseTicket> depenseTicketList = depenseTicketService.findByCustomerId(customer.getCustomerId());
        double totalAmount = depenseTicketService.getTotalMontantCustomerId(customer.getCustomerId());
        model.addAttribute("all_depenseTickets", depenseTicketList);
        model.addAttribute("totalAmount", totalAmount);
        return "depenseTicket/show-my-depense-ticket";
    }
    
    @GetMapping("/create-depense-ticket")
    public String showDepenseTicketForm(Model model) {
        model.addAttribute("tickets", ticketService.findTicketWithoutDepense(ticketService.findAll(), depenseTicketService.findAll()));
        return "depenseTicket/create-depense-ticket";
    }
    
    @PostMapping("/check-depense-ticket")
    public String checkDepenseTicket(Model model,
                                    @RequestParam("ticketId") int ticketId,
                                    @RequestParam(value="montant", required = false) BigDecimal montant,
                                    RedirectAttributes redirectAttributes) {
        
        if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
            redirectAttributes.addFlashAttribute("error", "The amount cannot be empty or negative!");
            return "redirect:/depenseTicket/create-depense-ticket";
        }
        
        Ticket ticket = ticketService.findByTicketId(ticketId);
        BigDecimal budget = BigDecimal.valueOf(budgetService.calculate_remaining_amount(budgetCustomerService, depenseLeadService, depenseTicketService, ticket.getCustomer().getCustomerId()));
        BigDecimal remaining_amount = budget.subtract(montant);
        
        if (remaining_amount.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("ticketId", ticketId);
            model.addAttribute("montant", montant);
            model.addAttribute("budget", budget);
            
            return "depenseTicket/confirmation-depense-ticket";
        }
        
        DepenseTicket ticketDepense = new DepenseTicket();
        ticketDepense.setTicket(ticket);
        ticketDepense.setMontant(montant);
        ticketDepense.setDateAjout(LocalDateTime.now());
        depenseTicketService.save(ticketDepense);
        
        model.addAttribute("tickets", ticketService.findTicketWithoutDepense(ticketService.findAll(), depenseTicketService.findAll()));
        model.addAttribute("message", "DepenseTicket inserted successfully !");
        
        Taux taux = tauxService.findLastTaux();
        System.out.println("taux : " + taux.getPourcentage() + " , Date : " + taux.getDateAjout());
        double pourcentage = taux.getPourcentage().doubleValue() / 100;
        ticket = ticketService.findByTicketId(ticketId);
        double total_budget = (budgetCustomerService.getTotalMontant(ticket.getCustomer().getCustomerId()).doubleValue());
        
        double total = total_budget * pourcentage;
        System.out.println("T :" + total + " B : " + budget);
        if (total >= remaining_amount.doubleValue()) {
            model.addAttribute("taux", "The client " + ticket.getCustomer().getName() + " has reached " + taux.getPourcentage()
                + "% of their budget (Remaining: " + budget + ")");
        }
        
        return "depenseTicket/create-depense-ticket";
        
    }
    
    @PostMapping("/save-depense-ticket")
    public String insertDepenseTicket(RedirectAttributes redirectAttributes,
                                      @RequestParam("ticketId") int ticketId,
                                      @RequestParam("montant") double montant) {
       
        Ticket ticket = ticketService.findByTicketId(ticketId);
        DepenseTicket ticketDepense = new DepenseTicket();
        ticketDepense.setTicket(ticket);
        ticketDepense.setMontant(BigDecimal.valueOf(montant));
        ticketDepense.setDateAjout(LocalDateTime.now());
        depenseTicketService.save(ticketDepense);
        
        redirectAttributes.addFlashAttribute("message", "DepenseTicket inserted successfully !");
        
        BigDecimal budget = BigDecimal.valueOf(budgetService.calculate_remaining_amount(budgetCustomerService, depenseLeadService, depenseTicketService, ticket.getCustomer().getCustomerId()));
        // BigDecimal remaining_amount = budget.subtract(BigDecimal.valueOf(montant));
        Taux taux = tauxService.findLastTaux();
        double pourcentage = taux.getPourcentage().doubleValue() / 100;
        ticket = ticketService.findByTicketId(ticketId);
        double total_budget = (budgetCustomerService.getTotalMontant(ticket.getCustomer().getCustomerId()).doubleValue());
       
        double total = total_budget * pourcentage;
        if (total >= budget.doubleValue()) {
            redirectAttributes.addFlashAttribute("taux1", "The client " + ticket.getCustomer().getName() + " has reached " + taux.getPourcentage()
                + "% of their budget (Remaining: " + budget + ")");
        }
        
        return "redirect:/depenseTicket/create-depense-ticket";
    }
}

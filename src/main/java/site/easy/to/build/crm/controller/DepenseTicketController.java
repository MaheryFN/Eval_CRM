package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.DepenseTicket;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/depenseTicket")
public class DepenseTicketController {
    private final DepenseLeadService depenseLeadService;
    private final DepenseTicketService depenseTicketService;
    private final BudgetService budgetCustomerService;
    private final TicketService ticketService;
    
    @Autowired
    public DepenseTicketController(DepenseLeadService depenseLeadService,
                                   DepenseTicketService depenseTicketService,
                                   BudgetService budgetCustomerService,
                                   TicketService ticketService) {
        this.depenseLeadService = depenseLeadService;
        this.depenseTicketService = depenseTicketService;
        this.budgetCustomerService = budgetCustomerService;
        this.ticketService = ticketService;
    }
    
    @GetMapping("/list-depense-ticket")
    public String showAllDepenseTicket(Model model) {
        List<DepenseTicket> depenseTicketList = depenseTicketService.findAll();
        model.addAttribute("depenseTickets", depenseTicketList);
        return "depenseTicket/list-depense-ticket";
    }
    
    @GetMapping("/create-depense-ticket")
    public String showDepenseTicketForm(Model model) {
        model.addAttribute("tickets", Ticket.findTicketWithoutDepense(ticketService.findAll(), depenseTicketService.findAll()));
        return "depenseTicket/create-depense-ticket";
    }
    
    @PostMapping("/check-depense-ticket")
    public String checkDepenseTicket(Model model,
                                   @RequestParam("ticketId") int ticketId,
                                   @RequestParam("montant") BigDecimal montant) {
        
        if (montant.compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("tickets", ticketService.findAll());
            model.addAttribute("message", "Invalid amount !");
            return "/depenseTicket/create-depense-ticket";
        }
        
        Ticket ticket = ticketService.findByTicketId(ticketId);
        BigDecimal budget = BigDecimal.valueOf(Budget.calculate_remaining_amount(budgetCustomerService, depenseLeadService, depenseTicketService, ticket.getCustomer().getCustomerId()));
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
        ticketDepense.setDateAjout(LocalDate.now());
        depenseTicketService.save(ticketDepense);
        
        model.addAttribute("tickets", Ticket.findTicketWithoutDepense(ticketService.findAll(), depenseTicketService.findAll()));
        model.addAttribute("message", "DepenseTicket inserted successfully !");
        
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
        ticketDepense.setDateAjout(LocalDate.now());
        depenseTicketService.save(ticketDepense);
        
        redirectAttributes.addFlashAttribute("message", "DepenseTicket inserted successfully !");
        return "redirect:/depenseTicket/create-depense-ticket";
    }
}

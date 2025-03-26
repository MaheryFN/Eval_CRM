package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.taux.TauxService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.ConvertJsonUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class ApiRestController {
    private final CustomerService customerService;
    private final BudgetService budgetService;
    private final LeadService leadService;
    private final DepenseLeadService depenseLeadService;
    private final TicketService ticketService;
    private final DepenseTicketService depenseTicketService;
    private final UserService userService;
    private final TauxService tauxService;
    
    @Autowired
    public ApiRestController(CustomerService customerService, BudgetService budgetService,
                             LeadService leadService, DepenseLeadService depenseLeadService,
                             TicketService ticketService, DepenseTicketService depenseTicketService,
                             UserService userService, TauxService tauxService) {
        this.customerService = customerService;
        this.budgetService = budgetService;
        this.leadService = leadService;
        this.depenseLeadService = depenseLeadService;
        this.ticketService = ticketService;
        this.depenseTicketService = depenseTicketService;
        this.userService = userService;
        this.tauxService = tauxService;
    }
    
    @GetMapping("/list-budget")
    public ResponseEntity<List<Map<String, Object>>> getAllBudget() {
        List<Budget> budgetCustomers = budgetService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Budget budgetCustomer : budgetCustomers) {
            Map<String, Object> map1 = new HashMap<>(ConvertJsonUtil.convertBudgetToMap(budgetCustomer));
            map1.put("customer", ConvertJsonUtil.convertCustomerToMap(budgetCustomer.getCustomer()));
            response.add(map1);
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/list-depenseTicket")
    public ResponseEntity<List<Map<String, Object>>> getAllDepenseTicket() {
        List<DepenseTicket> ticketDepenses = depenseTicketService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (DepenseTicket ticketDepense : ticketDepenses) {
            Map<String, Object> map1 = new HashMap<>(ConvertJsonUtil.convertDepenseTicketToMap(ticketDepense));
            map1.put("ticket", ConvertJsonUtil.convertTicketToMap(ticketDepense.getTicket()));
            response.add(map1);
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/list-depenseLead")
    public ResponseEntity<List<Map<String, Object>>> getAllDepenseLead() {
        List<DepenseLead> leadDepenses = depenseLeadService.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        for (DepenseLead leadDepense : leadDepenses) {
            Map<String, Object> map1 = new HashMap<>(ConvertJsonUtil.convertDepenseLeadToMap(leadDepense));
            map1.put("lead", ConvertJsonUtil.convertLeadToMap(leadDepense.getLead()));
            response.add(map1);
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/delete/ticket/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") int id) {
        DepenseTicket ticketDepense = depenseTicketService.findById(id);
        depenseTicketService.delete(ticketDepense);
        ticketService.delete(ticketDepense.getTicket());
        
        return ResponseEntity.ok("ticket deleted ! ");
    }
    
    @GetMapping("/delete/lead/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable("id") int id) {
        DepenseLead leadDepense = depenseLeadService.findById(id);
        depenseLeadService.delete(leadDepense);
        leadService.delete(leadDepense.getLead());
        
        return ResponseEntity.ok("lead deleted");
    }
    
    @GetMapping("/ticket-depense/{id}")
    public ResponseEntity<Map<String, Object>> findDepenseTicketById(@PathVariable("id") int id) {
        DepenseTicket ticketDepense = depenseTicketService.findById(id);
        Map<String, Object> map = new HashMap<>(ConvertJsonUtil.convertDepenseTicketToMap(ticketDepense));
        map.put("ticket", ConvertJsonUtil.convertTicketToMap(ticketDepense.getTicket()));
        return ResponseEntity.ok(map);
    }
    @GetMapping("/lead-depense/{id}")
    public ResponseEntity<Map<String, Object>> findDepenseLeadById(@PathVariable("id") int id) {
        DepenseLead leadDepense = depenseLeadService.findById(id);
        Map<String, Object> map = new HashMap<>(ConvertJsonUtil.convertDepenseLeadToMap(leadDepense));
        map.put("lead", ConvertJsonUtil.convertLeadToMap(leadDepense.getLead()));
        return ResponseEntity.ok(map);
    }
    
    @GetMapping("/update/lead-depense/{id}/{montant}")
    public ResponseEntity<String> updateDepenseLead(@PathVariable("id") int id,@PathVariable("montant") double montant) {
        DepenseLead leadDepense = depenseLeadService.findById(id);
        leadDepense.setMontant(BigDecimal.valueOf(montant));
        depenseLeadService.save(leadDepense);
        System.out.println("ticket depense updated");
        return ResponseEntity.ok("lead depense updated");
    }
    
    @GetMapping("/update/ticket-depense/{id}/{montant}")
    public ResponseEntity<String> updateDepenseTicket(@PathVariable("id") int id,@PathVariable("montant") double montant) {
        DepenseTicket ticketDepense = depenseTicketService.findById(id);
        ticketDepense.setMontant(BigDecimal.valueOf(montant));
        depenseTicketService.save(ticketDepense);
        System.out.println("ticket depense updated");
        return ResponseEntity.ok("ticket depense updated");
    }
    
    @GetMapping("/insert/taux/{pourcentage}")
    public ResponseEntity<String> insertTax(@PathVariable("pourcentage") BigDecimal pourcentage) {
        Taux taux = new Taux();
        taux.setPourcentage(pourcentage);
        taux.setDateAjout(LocalDateTime.now());
        tauxService.save(taux);
        System.out.println("tax added");
        return ResponseEntity.ok("tax added");
    }
    
    @PostMapping("/user")
    public ResponseEntity<Object> login(@RequestParam("email") String email) {
        User users = userService.findByEmail(email);
        if (users == null) {
            return ResponseEntity.ok("null");
        }  else {
            for (Role role : users.getRoles()) {
                if (role.getName().equals("ROLE_MANAGER")) {
                    Map<String, Object> response = new HashMap<>(ConvertJsonUtil.convertUserToMap(users));
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.ok("null");
                }
            }
        }
        return ResponseEntity.ok("null");
    }
}

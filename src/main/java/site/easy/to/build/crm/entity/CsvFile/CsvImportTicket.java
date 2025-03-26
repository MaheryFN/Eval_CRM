package site.easy.to.build.crm.entity.CsvFile;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.DepenseTicket;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class CsvImportTicket {
    
    private static final List<String> STATUS_LIST = Arrays.asList(
        "open", "assigned", "on-hold", "in-progress", "resolved", "closed", "reopened",
        "pending-customer-response", "escalated", "archived");
    
    private static final List<String> PRIORITY_LIST = Arrays.asList(
        "low", "medium", "high", "closed", "urgent", "critical");
    
    private List<Object[]> tickets = new ArrayList<>();
    public List<String> getStatus() {       return STATUS_LIST;     }
    public List<String> getPriority() {     return PRIORITY_LIST;       }
    public List<Object[]> getTickets() {    return tickets;     }
    
    public void setTickets(List<Object[]> tickets) {
        this.tickets = tickets;
    }
    
    public Object[] addTicketInfo(Ticket ticket, DepenseTicket ticketDepense) {
        Object[] tab = { ticket, ticketDepense };
        tickets.add(tab);
        return tab;
    }
    
    public List<Object[]> readTicket(User manager, User employee, List<HashMap<String, Object>> data,
                                     CsvImportCustomer importCustomer) throws Exception {
        List<Object[]> result = new ArrayList<>();
        int count = 1;
        
        for (HashMap<String, Object> map : data) {
            if ("ticket".equals(map.get("type").toString())) {
                Ticket ticket = createTicketFromMap(map, manager, employee, count, importCustomer);
                DepenseTicket ticketDepense = createDepenseTicketFromMap(map, ticket, count);
                result.add(addTicketInfo(ticket, ticketDepense));
            }
            count++;
        }
        return result;
    }
    
    private Ticket createTicketFromMap(HashMap<String, Object> map, User manager, User employee, int count,
                                       CsvImportCustomer importCustomer) throws Exception {
        Ticket ticket = new Ticket();
        ticket.setSubject(map.get("subject_or_name").toString());
        ticket.setDescription("description");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setManager(manager);
        ticket.setEmployee(employee);
        ticket.setPriority(PRIORITY_LIST.get(0));
        
        String status = checkStatus(map.get("status").toString());
        if (status != null) {
            ticket.setStatus(status);
        } else {
            throw new Exception("ligne " + count + " : Status invalide et ne correspond pas");
        }
        
        Customer customer = importCustomer.isExistCustomer(map.get("customer_email").toString());
        if (customer != null) {
            ticket.setCustomer(customer);
        } else {
            throw new Exception("ligne " + count + " : Le customer n'existe même pas et il n'est pas enregistré");
        }
        
        return ticket;
    }
    
    private DepenseTicket createDepenseTicketFromMap(HashMap<String, Object> map, Ticket ticket, int count)
        throws Exception {
        DepenseTicket ticketDepense = new DepenseTicket();
        double montant = Double.parseDouble(map.get("expense").toString());
        if (montant >= 0) {
            ticketDepense.setMontant(BigDecimal.valueOf(montant));
            ticketDepense.setDateAjout(ticket.getCreatedAt());
        } else {
            throw new Exception("ligne " + count + " : Montant négatif ou invalide");
        }
        return ticketDepense;
    }
    
    public String checkStatus(String status) {
        return STATUS_LIST.contains(status) ? status : null;
    }
    
    public void save(TicketService ticketService, DepenseTicketService ticketDepenseService) {
        tickets.forEach(ticketInfo -> {
            Ticket ticket = (Ticket) ticketInfo[0];
            DepenseTicket ticketDepense = (DepenseTicket) ticketInfo[1];
            Ticket createdTicket = ticketService.save(ticket);
            ticketDepense.setTicket(createdTicket);
            ticketDepenseService.save(ticketDepense);
        });
    }
    
    public void updateCustomerForTicket(Customer customer) {
        tickets.forEach(ticketInfo -> {
            Ticket ticket = (Ticket) ticketInfo[0];
            if (ticket.getCustomer().getEmail().equals(customer.getEmail())) {
                ticket.setCustomer(customer);
            }
        });
        // for (Object[] ticketInfo : tickets) {
        // Ticket ticket = (Ticket) ticketInfo[0];
        // if (ticket.getCustomer().getEmail().equals(customer.getEmail())) {
        // ticket.setCustomer(customer);
        // }
        // }
    }
}


package site.easy.to.build.crm.util;

import site.easy.to.build.crm.entity.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConvertJsonUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static Map<String, Object> convertUserToMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        return map;
    }
    
    public static Map<String, Object> convertCustomerToMap(Customer customer) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", customer.getCustomerId());
        map.put("name", customer.getName());
        map.put("email", customer.getEmail());
        map.put("createdAt", formatDate(customer.getCreatedAt()));
        return map;
    }
    
    public static Map<String, Object> convertBudgetToMap(Budget budget) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", budget.getBudgetId());
        map.put("customerId", budget.getCustomer().getCustomerId());
        map.put("montant", budget.getMontant().doubleValue());
        map.put("date_ajout", formatDate(budget.getDateAjout()));
        return map;
    }
    
    public static Map<String, Object> convertLeadToMap(Lead lead) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", lead.getLeadId());
        map.put("name", lead.getName());
        map.put("createdAt", formatDate(lead.getCreatedAt()));
        map.put("customer", convertCustomerToMap(lead.getCustomer()));
        map.put("employee", convertUserToMap(lead.getEmployee()));
        return map;
    }
    
    public static Map<String, Object> convertTicketToMap(Ticket ticket) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", ticket.getTicketId());
        map.put("subject", ticket.getSubject());
        map.put("createdAt", formatDate(ticket.getCreatedAt()));
        map.put("customer", convertCustomerToMap(ticket.getCustomer()));
        map.put("employee", convertUserToMap(ticket.getEmployee()));
        return map;
    }
    
    public static Map<String, Object> convertDepenseTicketToMap(DepenseTicket ticketDepense) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", ticketDepense.getDepenseTicketId());
        map.put("ticket_id", ticketDepense.getTicket().getTicketId());
        map.put("montant", ticketDepense.getMontant().doubleValue());
        map.put("date_ajout", formatDate(ticketDepense.getDateAjout()));
        return map;
    }
    
    public static Map<String, Object> convertDepenseLeadToMap(DepenseLead leadDepense) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", leadDepense.getDepenseLeadId());
        map.put("lead_id", leadDepense.getLead().getLeadId());
        map.put("montant", leadDepense.getMontant().doubleValue());
        map.put("date_ajout", formatDate(leadDepense.getDateAjout()));
        return map;
    }
    
    private static String formatDate(Object date) {
        return Optional.ofNullable(date).map(Object::toString).orElse("N/A");
    }
}

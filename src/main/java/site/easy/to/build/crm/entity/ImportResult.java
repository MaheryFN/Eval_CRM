package site.easy.to.build.crm.entity;

import java.util.List;

public class ImportResult {
    private boolean success;
    private List<String> errors;
    private List<Customer> customers;
    private List<Lead> leads;
    private List<Ticket> tickets;
    private List<DepenseLead> depenseLeads;
    private List<DepenseTicket> depenseTickets;
    
    public ImportResult(boolean success, List<String> errors) {
        this.success = success;
        this.errors = errors;
    }
    
    public ImportResult(boolean success, List<Customer> customers,
                        List<Lead> leads, List<Ticket> tickets,
                        List<DepenseLead> depenseLeads,
                        List<DepenseTicket> depenseTickets) {
        this.success = success;
        this.customers = customers;
        this.leads = leads;
        this.tickets = tickets;
        this.depenseLeads = depenseLeads;
        this.depenseTickets = depenseTickets;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    public List<Lead> getLeads() {
        return leads;
    }
    
    public void setLeads(List<Lead> leads) {
        this.leads = leads;
    }
    
    public List<Ticket> getTickets() {
        return tickets;
    }
    
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
    
    public List<DepenseLead> getDepenseLeads() {
        return depenseLeads;
    }
    
    public void setDepenseLeads(List<DepenseLead> depenseLeads) {
        this.depenseLeads = depenseLeads;
    }
    
    public List<DepenseTicket> getDepenseTickets() {
        return depenseTickets;
    }
    
    public void setDepenseTickets(List<DepenseTicket> depenseTickets) {
        this.depenseTickets = depenseTickets;
    }
}

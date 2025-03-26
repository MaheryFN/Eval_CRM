package site.easy.to.build.crm.service.budget;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetService {
    public Budget findById(int budgetId);
    
    public List<Budget> findAll();
    
    public Budget save(Budget budget);
    
    public void delete(Budget budget);
    
    public List<Budget> findByCustomerCustomerId(int customerId);
    
    public BigDecimal getTotalMontant(int customerId);
    
    public double calculate_remaining_amount(BudgetService budgetService, DepenseLeadService depenseLeadService, DepenseTicketService depenseTicketService, int customerId);
}

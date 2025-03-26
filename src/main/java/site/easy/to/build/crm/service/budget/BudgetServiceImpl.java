package site.easy.to.build.crm.service.budget;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    
    public BudgetServiceImpl(BudgetRepository budgetRepository) {this.budgetRepository = budgetRepository;}
    
    public Budget findById(int budgetId) {
        return budgetRepository.findById(budgetId).get();
    }
    
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }
    
    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }
    
    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }
    
    public List<Budget> findByCustomerCustomerId(int customerId) {
        return budgetRepository.findByCustomerCustomerId(customerId);
    }
    
    public BigDecimal getTotalMontant(int customerId) {
        return budgetRepository.getTotalMontant(customerId);
    }
    
    public double calculate_remaining_amount(BudgetService budgetService, DepenseLeadService depenseLeadService, DepenseTicketService depenseTicketService, int customerId) {
        double depense = depenseLeadService.getTotalMontantCustomerId(customerId) + depenseTicketService.getTotalMontantCustomerId(customerId);
        double budget = budgetService.getTotalMontant(customerId).doubleValue() ;
        return budget - depense;
    }
}

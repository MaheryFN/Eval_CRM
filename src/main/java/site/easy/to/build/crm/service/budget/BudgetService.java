package site.easy.to.build.crm.service.budget;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.easy.to.build.crm.entity.Budget;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetService {
    public Budget findById(int budgetId);
    
    public List<Budget> findAll();
    
    public Budget save(Budget budget);
    
    public void delete(Budget budget);
    
    public List<Budget> findByCustomerCustomerId(int customerId);
    
    public BigDecimal getTotalMontant(int customerId);
    
}

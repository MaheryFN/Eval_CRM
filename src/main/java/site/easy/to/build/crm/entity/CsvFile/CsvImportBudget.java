package site.easy.to.build.crm.entity.CsvFile;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.budget.BudgetService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CsvImportBudget {
    private List<Budget> budgets = new ArrayList<>();
    
    public CsvImportBudget() {}
    
    public CsvImportBudget(List<Budget> budgets) {     this.budgets = budgets;     }
    public List<Budget> getBudgets() {      return budgets;     }
    public void setBudgets(List<Budget> budgets) {      this.budgets = budgets;     }
    
    public List<Budget> readBudget(List<HashMap<String, Object>> data, CsvImportCustomer importCustomer) throws Exception {
        List<Budget> result = new ArrayList<>();
        int count = 1;
        
        for (HashMap<String, Object> map : data) {
            Budget budget = createBudgetFromMap(map, importCustomer, count);
            budgets.add(budget);
            result.add(budget);
            count++;
        }
        
        return result;
    }
    
    private Budget createBudgetFromMap(HashMap<String, Object> map, CsvImportCustomer importCustomer, int count) throws Exception {
        Budget budget = new Budget();
        budget.setDateAjout(LocalDateTime.now());
        
        Customer customer = validateCustomer(map, importCustomer, count);
        budget.setCustomer(customer);
        
        BigDecimal montant = validateMontant(map, count);
        budget.setMontant(montant);
        
        return budget;
    }
    
    private Customer validateCustomer(HashMap<String, Object> map, CsvImportCustomer importCustomer, int count) throws Exception {
        Customer customer = importCustomer.isExistCustomer(map.get("customer_email").toString());
        if (customer == null) {
            throw new Exception("ligne " + count + " : Le customer n'existe même pas et il n'est pas enregistré");
        }
        return customer;
    }
    
    private BigDecimal validateMontant(HashMap<String, Object> map, int count) throws Exception {
        double montant = Double.parseDouble(map.get("Budget").toString());
        if (montant < 0) {
            throw new Exception("ligne " + count + " : Montant négatif ou invalide");
        }
        return BigDecimal.valueOf(montant);
    }
    
    public void save(BudgetService budgetService) throws Exception {
        budgets.forEach(budget -> budgetService.save(budget));
    }
    
    public void updateCustomerForBudget(Customer customer) {
        budgets.forEach(budget -> {
            if (budget.getCustomer().getEmail().equals(customer.getEmail())) {
                budget.setCustomer(customer);
            }
        });
    }
}

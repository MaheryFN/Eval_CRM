package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    public List<Budget> findByCustomerCustomerId(int customerId);
    
    @Query(value = "select sum(montant) from budget where customer_id = :customerId", nativeQuery = true)
    public BigDecimal getTotalMontant(@Param("customerId") int customerId);

}

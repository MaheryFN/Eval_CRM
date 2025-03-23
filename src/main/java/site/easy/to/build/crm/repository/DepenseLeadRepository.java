package site.easy.to.build.crm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.DepenseLead;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DepenseLeadRepository extends JpaRepository<DepenseLead, Integer> {
    public DepenseLead findByDepenseLeadId(int id);
    
    public DepenseLead findByLeadLeadId(int leadId);
    
    @Query(value = "select sum(montant) from depense_lead", nativeQuery = true)
    public Double getTotalMontant();
    
    @Query(value = "select sum(montant) from depense_lead where lead_id = :leadId", nativeQuery = true)
    public Double getTotalMontantLeadId(@Param("leadId") int leadId);
}

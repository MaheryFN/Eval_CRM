package site.easy.to.build.crm.service.depenseLead;

import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.DepenseLead;

import java.math.BigDecimal;
import java.util.List;

public interface DepenseLeadService {

    public DepenseLead findByDepenseLeadId(int id);

    public DepenseLead findByLeadLeadId(int leadId);
    
    public List<DepenseLead> findAll();
    
    public void delete(DepenseLead depenseLead);

    public double getTotalMontant();
    
    public double getTotalMontantLeadId(int leadId);
    
    public DepenseLead save(DepenseLead leadDepense);
    
    public List<DepenseLead> findByCustomerId(int customerId);
    
    public double getTotalMontantCustomerId(int customerId);
    
}

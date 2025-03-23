package site.easy.to.build.crm.service.depenseLead;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.DepenseLead;
import site.easy.to.build.crm.repository.DepenseLeadRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepenseLeadServiceImpl implements DepenseLeadService {
    private final DepenseLeadRepository depenseLeadRepository;
    
    public DepenseLeadServiceImpl(DepenseLeadRepository depenseLeadRepository) {
        this.depenseLeadRepository = depenseLeadRepository;
    }
    
    @Override
    public DepenseLead findByDepenseLeadId(int id) {
        return depenseLeadRepository.findByDepenseLeadId(id);
    }
    
    @Override
    public DepenseLead findByLeadLeadId(int leadId) {
        return depenseLeadRepository.findByLeadLeadId(leadId);
    }
    
    @Override
    public List<DepenseLead> findAll() {
        return depenseLeadRepository.findAll();
    }
    
    @Override
    public void delete(DepenseLead depenseLead) {
        depenseLeadRepository.delete(depenseLead);
    }
    
    @Override
    public double getTotalMontant() {
        Double total = depenseLeadRepository.getTotalMontant();
        if (total == null) {
            return 0;
        }
        return total;
    }
    
    @Override
    public double getTotalMontantLeadId(int leadId) {
        Double total = depenseLeadRepository.getTotalMontantLeadId(leadId);
        if (total == null) {
            return 0;
        }
        return total;
    }
    
    @Override
    public DepenseLead save(DepenseLead leadDepense) {
        return depenseLeadRepository.save(leadDepense);
    }
    
    
    @Override
    public List<DepenseLead> findByCustomerId(int customerId) {
        List<DepenseLead> depenseLeads = depenseLeadRepository.findAll();
        List<DepenseLead> filteredDepenseLeads = new ArrayList<>();
        for (DepenseLead leadDepense : depenseLeads) {
            if (leadDepense.getLead().getCustomer().getCustomerId() == customerId) {
                filteredDepenseLeads.add(leadDepense);
            }
        }
        return filteredDepenseLeads;
    }
    
    @Override
    public double getTotalMontantCustomerId(int customerId) {
        double total = 0;
        List<DepenseLead> depenseLeads = findByCustomerId(customerId);
        for (DepenseLead leadDepense : depenseLeads) {
            total += leadDepense.getMontant().doubleValue();
        }
        return total;
    }
}

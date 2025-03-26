package site.easy.to.build.crm.entity.CsvFile;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.DepenseLead;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.lead.LeadService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CsvImportLead {
    
    private static final List<String> STATUS_LIST = Arrays.asList(
        "meeting-to-schedule", "scheduled", "archived", "success", "assign-to-sales"
    );
    
    private List<Object[]> leads = new ArrayList<>();
    public List<String> getStatus() {   return STATUS_LIST;     }
    
    public List<Object[]> getLeads() {  return leads;   }
    
    public Object[] addLeadInfo(Lead lead, DepenseLead leadDepense) {
        Object[] tab = { lead, leadDepense };
        leads.add(tab);
        return tab;
    }
    
    public List<Object[]> readLead(User manager, User employee, List<HashMap<String, Object>> data, CsvImportCustomer importCustomer) throws Exception {
        List<Object[]> result = new ArrayList<>();
        int count = 1;
        
        for (HashMap<String, Object> map : data) {
            if ("lead".equals(map.get("type").toString())) {
                Lead lead = createLeadFromMap(map, manager, employee, count, importCustomer);
                DepenseLead leadDepense = createLeadDepenseFromMap(map, lead, count);
                result.add(addLeadInfo(lead, leadDepense));
            }
            count++;
        }
        return result;
    }
    
    private Lead createLeadFromMap(HashMap<String, Object> map, User manager, User employee, int count, CsvImportCustomer importCustomer) throws Exception {
        Lead lead = new Lead();
        lead.setName(map.get("subject_or_name").toString());
        lead.setPhone("0327464502");
        lead.setGoogleDrive(false);
        lead.setGoogleDriveFolderId("");
        lead.setCreatedAt(LocalDateTime.now());
        lead.setManager(manager);
        lead.setEmployee(employee);
        
        String status = checkStatus(map.get("status").toString());
        if (status != null) {
            lead.setStatus(status);
        } else {
            throw new Exception("ligne " + count + " : Status invalide et ne correspond pas!");
        }
        
        Customer customer = importCustomer.isExistCustomer(map.get("customer_email").toString());
        if (customer != null) {
            lead.setCustomer(customer);
        } else {
            throw new Exception("ligne " + count + " : Le customer n'existe même pas et il n'est pas enregistré");
        }
        
        return lead;
    }
    
    private DepenseLead createLeadDepenseFromMap(HashMap<String, Object> map, Lead lead, int count) throws Exception {
        DepenseLead leadDepense = new DepenseLead();
        double montant = Double.parseDouble(map.get("expense").toString());
        if (montant >= 0) {
            leadDepense.setMontant(BigDecimal.valueOf(montant));
            leadDepense.setDateAjout(lead.getCreatedAt());
        } else {
            throw new Exception("ligne " + count + " : Montant négatif ou invalide");
        }
        return leadDepense;
    }
    
    public String checkStatus(String status) {
        return STATUS_LIST.contains(status) ? status : null;
    }
    
    public void save(LeadService leadService, DepenseLeadService leadDepenseService) {
        for (Object[] leadInfo : leads) {
            Lead lead = (Lead) leadInfo[0];
            DepenseLead leadDepense = (DepenseLead) leadInfo[1];
            Lead createdLead = leadService.save(lead);
            leadDepense.setLead(createdLead);
            leadDepenseService.save(leadDepense);
        }
    }
    
    public void updateCustomerForLead(Customer customer) {
        leads.forEach(leadInfo -> {
            Lead lead = (Lead) leadInfo[0];
            if (lead.getCustomer().getEmail().equals(customer.getEmail())) {
                lead.setCustomer(customer);
            }
        });
    }
}

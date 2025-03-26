package site.easy.to.build.crm.service.depenseTicket;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.DepenseTicket;
import site.easy.to.build.crm.repository.DepenseTicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DepenseTicketServiceImpl implements DepenseTicketService {
    DepenseTicketRepository depenseTicketRepository;
    
    public DepenseTicketServiceImpl(DepenseTicketRepository depenseTicketRepository) {
        this.depenseTicketRepository = depenseTicketRepository;
    }
    
    @Override
    public DepenseTicket findByDepenseTicketId(int id) {
        return depenseTicketRepository.findByDepenseTicketId(id);
    }
    
    @Override
    public DepenseTicket findByTicketTicketId(int leadId) {
        return depenseTicketRepository.findByTicketTicketId(leadId);
    }
    
    @Override
    public List<DepenseTicket> findAll() {
        return depenseTicketRepository.findAll();
    }
    
    @Override
    public void delete(DepenseTicket depenseLead) {
        depenseTicketRepository.delete(depenseLead);
    }
    
    @Override
    public double getTotalMontant() {
        Double total = depenseTicketRepository.getTotalMontant();
        if (total == null) {
            return 0;
        }
        return total;
    }
    
    @Override
    public double getTotalMontantTicketId(int ticketId) {
        Double total = depenseTicketRepository.getTotalMontantTicketId(ticketId);
        if (total == null) {
            return 0;
        }
        return total;
    }
    
    @Override
    public DepenseTicket save(DepenseTicket leadDepense) {
        return depenseTicketRepository.save(leadDepense);
    }
    
    @Override
    public List<DepenseTicket> findByCustomerId(int customerId) {
        List<DepenseTicket> depenseTickets = depenseTicketRepository.findAll();
        List<DepenseTicket> filteredDepenseTickets = new ArrayList<>();
        for (DepenseTicket leadDepense : depenseTickets) {
            if (leadDepense.getTicket().getCustomer().getCustomerId() == customerId) {
                filteredDepenseTickets.add(leadDepense);
            }
        }
        return filteredDepenseTickets;
    }
    
    @Override
    public double getTotalMontantCustomerId(int customerId) {
        double total = 0;
        List<DepenseTicket> depenseTickets = findByCustomerId(customerId);
        for (DepenseTicket leadDepense : depenseTickets) {
            total += leadDepense.getMontant().doubleValue();
        }
        return total;
    }
    
    @Override
    public DepenseTicket findById(int id) {
        return depenseTicketRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<DepenseTicket> saveAll(List<DepenseTicket> leadDepenseTickets) {
        return depenseTicketRepository.saveAll(leadDepenseTickets);
    }
    
}

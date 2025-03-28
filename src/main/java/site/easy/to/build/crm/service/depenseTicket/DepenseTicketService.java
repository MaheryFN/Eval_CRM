package site.easy.to.build.crm.service.depenseTicket;

import site.easy.to.build.crm.entity.DepenseTicket;

import java.util.List;
import java.util.Optional;

public interface DepenseTicketService {
    public DepenseTicket findByDepenseTicketId(int id);
    
    public DepenseTicket findByTicketTicketId(int ticketId);
    
    public List<DepenseTicket> findAll();
    
    public void delete(DepenseTicket depenseticket);
    
    public double getTotalMontant();
    
    public double getTotalMontantTicketId(int ticketId);
    
    public DepenseTicket save(DepenseTicket leadDepense);
    
    public List<DepenseTicket> findByCustomerId(int customerId);
    
    public double getTotalMontantCustomerId(int customerId);
    
    public DepenseTicket findById(int id);
    
    public List<DepenseTicket> saveAll(List<DepenseTicket> leadDepenseTickets);
}

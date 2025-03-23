package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.DepenseTicket;

@Repository
public interface DepenseTicketRepository extends JpaRepository<DepenseTicket, Integer> {
    public DepenseTicket findByDepenseTicketId(int id);
    
    public DepenseTicket findByTicketTicketId(int ticketId);
    
    @Query(value = "select sum(montant) from depense_ticket", nativeQuery = true)
    public Double getTotalMontant();
    
    @Query(value = "select sum(montant) from depense_ticket where ticket_id = :ticketId", nativeQuery = true)
    public Double getTotalMontantTicketId(@Param("ticketId") int ticketId);
}

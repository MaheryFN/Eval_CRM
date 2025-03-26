package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "depense_ticket")
public class DepenseTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer depenseTicketId;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;
    
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
    
    public DepenseTicket() {}
    
    public DepenseTicket(BigDecimal montant, LocalDateTime dateAjout, Ticket ticket) {
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.ticket = ticket;
    }
    
    public DepenseTicket(Integer depenseTicketId, BigDecimal montant, LocalDateTime dateAjout, Ticket ticket) {
        this.depenseTicketId = depenseTicketId;
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.ticket = ticket;
    }
    
    public Integer getDepenseTicketId() {
        return depenseTicketId;
    }
    
    public void setDepenseTicketId(Integer depenseTicketId) {
        this.depenseTicketId = depenseTicketId;
    }
    
    public BigDecimal getMontant() {
        return montant;
    }
    
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
    
    public LocalDateTime getDateAjout() {
        return dateAjout;
    }
    
    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}

package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate dateAjout;
    
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
    
    public DepenseTicket() {}
    
    public DepenseTicket(BigDecimal montant, LocalDate dateAjout, Ticket ticket) {
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.ticket = ticket;
    }
    
    public DepenseTicket(Integer depenseTicketId, BigDecimal montant, LocalDate dateAjout, Ticket ticket) {
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
    
    public LocalDate getDateAjout() {
        return dateAjout;
    }
    
    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }
    
    public Ticket getTicket() {
        return ticket;
    }
    
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}

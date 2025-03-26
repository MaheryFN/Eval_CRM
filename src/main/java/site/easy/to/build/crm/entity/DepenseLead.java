package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "depense_lead")
public class DepenseLead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer depenseLeadId;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;
    
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;
    
    public DepenseLead() {}
    
    public DepenseLead(BigDecimal montant, LocalDateTime dateAjout, Lead lead) {
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.lead = lead;
    }
    
    public DepenseLead(Integer depenseLeadId, BigDecimal montant, LocalDateTime dateAjout, Lead lead) {
        this.depenseLeadId = depenseLeadId;
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.lead = lead;
    }
    
    public Integer getDepenseLeadId() {
        return depenseLeadId;
    }
    
    public void setDepenseLeadId(Integer depenseLeadId) {
        this.depenseLeadId = depenseLeadId;
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
    
    public Lead getLead() {
        return lead;
    }
    
    public void setLead(Lead lead) {
        this.lead = lead;
    }
}

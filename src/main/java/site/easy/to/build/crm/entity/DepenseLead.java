package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate dateAjout;
    
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;
    
    public DepenseLead() {}
    
    public DepenseLead(BigDecimal montant, LocalDate dateAjout, Lead lead) {
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.lead = lead;
    }
    
    public DepenseLead(Integer depenseLeadId, BigDecimal montant, LocalDate dateAjout, Lead lead) {
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
    
    public LocalDate getDateAjout() {
        return dateAjout;
    }
    
    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }
    
    public Lead getLead() {
        return lead;
    }
    
    public void setLead(Lead lead) {
        this.lead = lead;
    }
}

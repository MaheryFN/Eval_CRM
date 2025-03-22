package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budget")
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer budgetId;
    
    @Column(name = "montant")
    private BigDecimal montant;
    
    @Column(name = "date_ajout")
    private LocalDate dateAjout;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    public Budget() {}
    
    public Budget(BigDecimal montant, LocalDate dateAjout, Customer customer) {
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.customer = customer;
    }
    
    public Budget(Integer budgetId, BigDecimal montant, LocalDate dateAjout, Customer customer) {
        this.budgetId = budgetId;
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.customer = customer;
    }
    
    public Integer getBudgetId() {
        return budgetId;
    }
    
    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
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
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
}

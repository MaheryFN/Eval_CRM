package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime dateAjout;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
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
    
    public LocalDateTime getDateAjout() {
        return dateAjout;
    }
    
    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Budget() {}
    
    public Budget(BigDecimal montant, LocalDateTime dateAjout, Customer customer) {
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.customer = customer;
    }
    
    public Budget(Integer budgetId, BigDecimal montant, LocalDateTime dateAjout, Customer customer) {
        this.budgetId = budgetId;
        this.montant = montant;
        this.dateAjout = dateAjout;
        this.customer = customer;
    }
    
}

package site.easy.to.build.crm.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="taux")
public class Taux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "pourcentage")
    private BigDecimal pourcentage;
    
    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;
    
    public Integer getId() {
        return id;
    }
    
    public BigDecimal getPourcentage() {
        return pourcentage;
    }
    
    public LocalDateTime getDateAjout() {
        return dateAjout;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setPourcentage(BigDecimal pourcentage) {
        this.pourcentage = pourcentage;
    }
    
    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
    }
}

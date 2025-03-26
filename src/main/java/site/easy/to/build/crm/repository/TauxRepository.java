package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.entity.Taux;

public interface TauxRepository extends JpaRepository<Taux,Integer> {
    Taux findById(int id);
    
    @Query(value = "SELECT * FROM Taux ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Taux findLastTaux();
    
    @Query(value = "SELECT * FROM Taux ORDER BY date_ajout DESC LIMIT 1", nativeQuery = true)
    Taux findLastTauxByCreateAt();
}

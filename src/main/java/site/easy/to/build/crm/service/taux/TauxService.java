package site.easy.to.build.crm.service.taux;

import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.entity.Taux;

public interface TauxService {
    Taux findById(int id);
    
    public Taux findLastTaux();
    
    public Taux findLastTauxByCreateAt();
    
    public void save(Taux taux);
}

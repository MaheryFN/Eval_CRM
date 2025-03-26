package site.easy.to.build.crm.service.taux;

import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Taux;
import site.easy.to.build.crm.repository.TauxRepository;

@Service
public class TauxServiceImpl implements TauxService {
    TauxRepository repo;
    
    public TauxServiceImpl(TauxRepository repo) {
        this.repo = repo;
    }
    
    @Override
    public Taux findById(int id) {
        return repo.findById(id);
    }
    
    @Override
    public Taux findLastTaux() {
        return repo.findLastTaux();
    }
    
    @Override
    public Taux findLastTauxByCreateAt() {
        return repo.findLastTauxByCreateAt();
    }
    
    @Override
    public void save(Taux taux) {
        repo.save(taux);
    }
}

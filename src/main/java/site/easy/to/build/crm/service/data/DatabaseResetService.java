package site.easy.to.build.crm.service.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseResetService {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public void resetDatabase(List<String> tablesToReset) {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        try {
            List<String> tableNames = getTableNames(tablesToReset);
            for (String tableName : tableNames) {
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            }
        } finally {
            entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        }
    }
    
    @SuppressWarnings("unchecked")
    private List<String> getTableNames(List<String> tablesToReset) {
        List<String> allTables = entityManager.createNativeQuery(
                "SELECT TABLE_NAME FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_TYPE = 'BASE TABLE'")
            .getResultList();
        
        return allTables.stream()
            .filter(tablesToReset::contains)
            .collect(Collectors.toList());
    }
    
}


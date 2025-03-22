package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.service.data.DatabaseResetService;

import java.util.Arrays;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequestMapping("/data")
public class DatabaseResetController {
    @Autowired
    private DatabaseResetService databaseResetService;
    
    // Liste des tables à réinitialiser
    private final List<String> defaultTablesToReset = Arrays.asList(
        "file"
    );
    
    // AJAX
    @PostMapping("/reset-database-ajax")
    @ResponseBody
    public ResponseEntity<String> resetDatabaseRest() {
        databaseResetService.resetDatabase(defaultTablesToReset);
        return ResponseEntity.ok("Base de données réinitialisée avec succès");
    }
//
//    // 2. Endpoint MVC pour les soumissions de formulaire
//    @PostMapping("/reset-database")
//    public String resetDatabase(RedirectAttributes redirectAttributes) {
//        databaseResetService.resetDatabase(defaultTablesToReset);
//        redirectAttributes.addFlashAttribute("message", "Base de données réinitialisée avec succès");
//        return "redirect:/manager/database/reset";
//    }
    
    @GetMapping("/manager/database/reset")
    public String showResetPage(Model model) {
        model.addAttribute("allTables", defaultTablesToReset);
        return "manager/database/reset";
    }
}

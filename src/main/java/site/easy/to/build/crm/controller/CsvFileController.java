package site.easy.to.build.crm.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.entity.CsvFile.CsvFile;
import site.easy.to.build.crm.entity.CsvFile.CsvImport;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@PreAuthorize("hasRole('ROLE_MANAGER')")
@RequestMapping("/importCsv")
public class CsvFileController {
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final TicketService ticketService;
    private final DepenseTicketService ticketDepenseService;
    private final LeadService leadService;
    private final DepenseLeadService leadDepenseService;
    private final AuthenticationUtils authenticationUtils;
    private final UserService userService;
    private final BudgetService budgetCustomerService;
    
    public CsvFileController(PasswordEncoder passwordEncoder, CustomerService customerService, CustomerLoginInfoService customerLoginInfoService, TicketService ticketService, DepenseTicketService ticketDepenseService, LeadService leadService, DepenseLeadService leadDepenseService, AuthenticationUtils authenticationUtils, UserService userService, BudgetService budgetCustomerService) {
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
        this.customerLoginInfoService = customerLoginInfoService;
        this.ticketService = ticketService;
        this.ticketDepenseService = ticketDepenseService;
        this.leadService = leadService;
        this.leadDepenseService = leadDepenseService;
        this.authenticationUtils = authenticationUtils;
        this.userService = userService;
        this.budgetCustomerService = budgetCustomerService;
    }
    
    @GetMapping("/manager/database/import-csv")
    public String showImportCsvPage() {
        return "manager/database/import-csv";
    }
    
    public String uploadCsv(MultipartFile file) {
        String uploadDir = "E:\\ITU\\S6\\Evaluation\\Evaluation_20-03-25\\outils";
        String originalFilename = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, originalFilename);
        try {
            Files.createDirectories(filePath.getParent());
            return filePath.toString();
            
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier: " + e.getMessage(), e);
        }
    }
    
    @PostMapping("/create/import-csv")
    public String importCsvFile(Authentication authentication, Model model,
                                @RequestParam("csvFile1") MultipartFile file,
                                @RequestParam("csvFile2") MultipartFile file2,
                                @RequestParam("csvFile3") MultipartFile file3,
                                @RequestParam("separator") String separateur,
                                RedirectAttributes redirectAttributes) {
        
        CsvFile csvFile1 = new CsvFile();
        csvFile1.getTypes().put("customer_email", String.class);
        csvFile1.getTypes().put("customer_name", String.class);
        csvFile1.setName(file.getOriginalFilename());
        csvFile1.setPath(uploadCsv(file));
        
        CsvFile csvFile2 = new CsvFile();
        csvFile2.getTypes().put("customer_email", String.class);
        csvFile2.getTypes().put("subject_or_name", String.class);
        csvFile2.getTypes().put("type", String.class);
        csvFile2.getTypes().put("status", String.class);
        csvFile2.getTypes().put("expense", double.class);
        csvFile2.setName(file2.getOriginalFilename());
        csvFile2.setPath(uploadCsv(file2));
        
        CsvFile csvFile3 = new CsvFile();
        csvFile3.getTypes().put("customer_email", String.class);
        csvFile3.getTypes().put("Budget", double.class);
        csvFile3.setName(file3.getOriginalFilename());
        csvFile3.setPath(uploadCsv(file3));
        
        CsvImport importCSV = new CsvImport(passwordEncoder, customerService,
            customerLoginInfoService, ticketService, ticketDepenseService,
            leadService, leadDepenseService, budgetCustomerService);
        importCSV.addListFile(csvFile1);
        importCSV.addListFile(csvFile2);
        importCSV.addListFile(csvFile3);
        
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User manager = userService.findById(userId);
        
        try {
            importCSV.importData(manager, manager, separateur);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "manager/database/import-csv";
        }

        redirectAttributes.addFlashAttribute("alert", "Import des données CSV réussi !");
        return "redirect:/importCsv/manager/database/import-csv";
        
        
    }
}

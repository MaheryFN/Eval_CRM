package site.easy.to.build.crm.entity.CsvFile;

import org.springframework.security.crypto.password.PasswordEncoder;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.depenseLead.DepenseLeadService;
import site.easy.to.build.crm.service.depenseTicket.DepenseTicketService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CsvImport {
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final CustomerLoginInfoService customerLoginInfoService;
    private final TicketService ticketService;
    private final DepenseTicketService ticketDepenseService;
    private final LeadService leadService;
    private final DepenseLeadService leadDepenseService;
    private final BudgetService budgetCustomerService;
    List<CsvFile> listFiles = new ArrayList<CsvFile>();
    
    public CsvImport(PasswordEncoder passwordEncoder, CustomerService customerService, CustomerLoginInfoService customerLoginInfoService, TicketService ticketService, DepenseTicketService ticketDepenseService, LeadService leadService, DepenseLeadService leadDepenseService, BudgetService budgetCustomerService) {
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
        this.customerLoginInfoService = customerLoginInfoService;
        this.ticketService = ticketService;
        this.ticketDepenseService = ticketDepenseService;
        this.leadService = leadService;
        this.leadDepenseService = leadDepenseService;
        this.budgetCustomerService = budgetCustomerService;
    }
    
    public void addListFile(CsvFile file) {
        this.listFiles.add(file);
    }
    
    public void importData (User manager, User employee, String separateur) throws Exception {
        CsvProcessor csvProcessor = new CsvProcessor();
        CsvImportCustomer importCustomer = new CsvImportCustomer(passwordEncoder);
        importCustomer.setCustomers(customerService.findAll());
        CsvImportTicket importTicket = new CsvImportTicket();
        CsvImportLead importLead = new CsvImportLead();
        CsvImportBudget importBudget = new CsvImportBudget();
        for (int i = 0; i < listFiles.size(); i++) {
            CsvFile file = listFiles.get(i);
            try {
                List<HashMap<String,Object>> data = csvProcessor.extractDataFromCSV(file.getPath(), separateur, file.getTypes());
                if(i==0){
                    importCustomer.readCustomers(manager,data);
                }
                if (i==1){
                    importLead.readLead(manager,employee,data,importCustomer);
                    importTicket.readTicket(manager,employee,data,importCustomer);
                }
                if (i==2){
                    importBudget.readBudget(data,importCustomer);
                }
            } catch (Exception e) {
                throw new Exception("Une erreur est survenue dans le "+ file.getName()+",  "+e.getMessage());
            }
        }
        importCustomer.save(customerService,customerLoginInfoService,importLead,importTicket,importBudget);
        importLead.save(leadService,leadDepenseService);
        importTicket.save(ticketService,ticketDepenseService);
        importBudget.save(budgetCustomerService);
    }
    
}


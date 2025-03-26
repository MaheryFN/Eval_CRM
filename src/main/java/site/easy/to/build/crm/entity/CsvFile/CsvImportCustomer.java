package site.easy.to.build.crm.entity.CsvFile;

import org.springframework.security.crypto.password.PasswordEncoder;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.service.customer.CustomerLoginInfoService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.util.EmailTokenUtils;

import java.time.LocalDateTime;
import java.util.*;

public class CsvImportCustomer {
    private final PasswordEncoder passwordEncoder;
    private List<Customer> customers = new ArrayList<>();
    
    public CsvImportCustomer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    public List<Customer> readCustomers(User manager, List<HashMap<String, Object>> data) throws Exception {
        List<Customer> result = new ArrayList<>();
        int count = 1;
        
        for (HashMap<String, Object> map : data) {
            Customer customer = createCustomerFromMap(map, manager);
            if (isExistCustomer(customer.getEmail()) == null) {
                result.add(customer);
                customers.add(customer);
            } else {
                throw new Exception("ligne " + count + " : Le client existe déjà");
            }
            count++;
        }
        return result;
    }
    
    private Customer createCustomerFromMap(HashMap<String, Object> map, User manager) {
        Customer customer = new Customer();
        customer.setName((String) map.get("customer_name"));
        customer.setEmail((String) map.get("customer_email"));
        customer.setAddress("Manjakaray-Dakar");
        customer.setCity("Antananarivo");
        customer.setState("Madagascar");
        customer.setCountry("Afrique-Atsimo");
        customer.setDescription("");
        customer.setPhone("0327464502");
        customer.setFacebook("");
        customer.setTwitter("");
        customer.setYoutube("");
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUser(manager);
        
        // Auth-Customer-info
        CustomerLoginInfo customerLoginInfo = createCustomerLoginInfo(customer);
        customer.setCustomerLoginInfo(customerLoginInfo);
        
        return customer;
    }
    
    private CustomerLoginInfo createCustomerLoginInfo(Customer customer) {
        CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo();
        String hashPassword = passwordEncoder.encode("mdp");
        customerLoginInfo.setPassword(hashPassword);
        customerLoginInfo.setPasswordSet(true);
        customerLoginInfo.setEmail(customer.getEmail());
        customerLoginInfo.setToken(EmailTokenUtils.generateToken());
        return customerLoginInfo;
    }
    
    public Customer isExistCustomer(String email) {
        return customers.stream()
            .filter(customer -> customer.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }
    
    public void save(CustomerService customerService, CustomerLoginInfoService customerLoginInfoService,
                     CsvImportLead importLead, CsvImportTicket importTicket, CsvImportBudget importBudget) throws Exception {
        customers.forEach(customer -> {
            if (customer.getCustomerId() == null) {
                Customer createdCustomer = saveNewCustomer(customerService, customer);
                updateCustomerLoginInfo(customerLoginInfoService, customer, createdCustomer);
                updateRelatedEntities(createdCustomer, importLead, importTicket, importBudget);
            }
        });
    }
    
    private Customer saveNewCustomer(CustomerService customerService, Customer customer) {
        Customer newCustomer = new Customer();
        newCustomer.setName(customer.getName());
        newCustomer.setEmail(customer.getEmail());
        newCustomer.setAddress(customer.getAddress());
        newCustomer.setCity(customer.getCity());
        newCustomer.setState(customer.getState());
        newCustomer.setCountry(customer.getCountry());
        newCustomer.setDescription(customer.getDescription());
        newCustomer.setPhone(customer.getPhone());
        newCustomer.setFacebook(customer.getFacebook());
        newCustomer.setTwitter(customer.getTwitter());
        newCustomer.setYoutube(customer.getYoutube());
        newCustomer.setCreatedAt(customer.getCreatedAt());
        newCustomer.setUser(customer.getUser());
        
        return customerService.save(newCustomer);
    }
    
    private void updateCustomerLoginInfo(CustomerLoginInfoService customerLoginInfoService, Customer customer, Customer createdCustomer) {
        customer.getCustomerLoginInfo().setCustomer(createdCustomer);
        CustomerLoginInfo createdCustomerLoginInfo = customerLoginInfoService.save(customer.getCustomerLoginInfo());
        createdCustomer.setCustomerLoginInfo(createdCustomerLoginInfo);
    }
    
    private void updateRelatedEntities(Customer createdCustomer, CsvImportLead importLead, CsvImportTicket importTicket, CsvImportBudget importBudget) {
        importLead.updateCustomerForLead(createdCustomer);
        importTicket.updateCustomerForTicket(createdCustomer);
        importBudget.updateCustomerForBudget(createdCustomer);
    }
}

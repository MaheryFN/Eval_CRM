//package site.easy.to.build.crm;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import site.easy.to.build.crm.service.data.DatabaseResetService;
//
//import java.util.Arrays;
//import java.util.List;
//
//@SpringBootApplication
//public class Main implements CommandLineRunner {
//    @Autowired
//    DatabaseResetService databaseResetService = new DatabaseResetService();
//
//    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("Delete data...");
//        List<String> tablesToReset = Arrays.asList("file" );
//        databaseResetService.resetDatabase(tablesToReset);
//    }
//}

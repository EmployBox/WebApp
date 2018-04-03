package isel.ps.employbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"isel.ps.employbox.api.services", "isel.ps.employbox.api.controllers"})
public class EmployBoxAPIApp {

    public static void main(String[] args) {
        SpringApplication.run(EmployBoxAPIApp.class, args);
    }
}

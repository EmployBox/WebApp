package isel.ps.EmployBox.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"isel.ps.EmployBox.server", "isel.ps.EmployBox.controllers"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

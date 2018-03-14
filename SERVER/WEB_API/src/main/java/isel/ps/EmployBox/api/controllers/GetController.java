package isel.ps.EmployBox.api.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GetController {

    @GetMapping("/ola")
    public String ola(){
        return "hello";
    }
    
}

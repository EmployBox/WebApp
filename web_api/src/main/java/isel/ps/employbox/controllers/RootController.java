package isel.ps.employbox.controllers;

import isel.ps.employbox.model.output.RootPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {

    @GetMapping
    public RootPage getRoot(){
        return new RootPage();
    }

}

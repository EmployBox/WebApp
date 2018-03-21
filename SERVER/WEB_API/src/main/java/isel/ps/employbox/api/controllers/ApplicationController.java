package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.input.InApplication;
import isel.ps.employbox.api.model.output.OutApplication;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.api.services.UserService;
import isel.ps.employbox.dal.model.DomainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ApplicationController implements ModelBinder {

    @Autowired
    private UserService userService;

    @Autowired
    private APIService apiService;

    @Override
    public List bindOutput(List list) {
        return null;
    }

    @Override
    public List bindInput(List list) {
        return null;
    }

    @Override
    public Object bindOutput(DomainObject object) {
        return null;
    }

    @Override
    public DomainObject bindInput(Object object) {
        return null;
    }

    @GetMapping("/account/user/{id}/apply")
    public List<OutApplication> getAllApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return bindOutput(
                userService.getAllApplications(id, queryString)
        );
    }

    @PutMapping("/account/user/{id}/apply/{jid}")
    public void updateApplication(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody InApplication inApplication
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateApplication(id, jid, inApplication);
    }
}

package isel.ps.employbox.api.controllers;

import isel.ps.employbox.api.model.binder.ApplicationBinder;
import isel.ps.employbox.api.model.binder.UserBinder;
import isel.ps.employbox.api.model.input.InApplication;
import isel.ps.employbox.api.model.input.InCurriculum;
import isel.ps.employbox.api.model.input.InUser;
import isel.ps.employbox.api.model.output.OutApplication;
import isel.ps.employbox.api.model.output.OutUser;
import isel.ps.employbox.api.services.APIService;
import isel.ps.employbox.api.services.ModelBinder;
import isel.ps.employbox.api.services.UserService;
import isel.ps.employbox.dal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private  UserService userService;
    @Autowired
    private  APIService apiService;
    @Autowired
    private UserBinder userBinder;
    @Autowired
    private ApplicationBinder applicationBinder;

    @GetMapping("/account/user")
    public List<OutUser> getAllUsers(@RequestParam Map<String,String> queryString){
        return userBinder.bindOutput(userService.getAllUsers(queryString));
    }

    @GetMapping("/account/user/{id}")
    public Optional<OutUser> getUser(@PathVariable long id){
        return userBinder.bindOutput(Collections.singletonList(userService.getUser(id)))
                .stream()
                .findFirst();
    }

    @GetMapping("/account/user/{id}/apply")
    public List<OutApplication> getAllApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return applicationBinder.bindOutput(
                userService.getAllApplications(id, queryString)
        );
    }

    @PutMapping("/account/user/{id}")
    public void updateUser(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @RequestBody InUser inUser
    ){
        apiService.validateAPIKey(apiKey);
        userService.updateUser(id, inUser);
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

    @PostMapping("/account/user/")
    public void createUser(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InUser inUser
    ){
        apiService.validateAPIKey(apiKey);
        userService.createUser(inUser);
    }

    @PostMapping("/account/user/{id}/apply/{jid}")
    public void createApplication(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InApplication inApplication
    ){
        apiService.validateAPIKey(apiKey);
        userService.createApplication(inApplication);
    }

    @DeleteMapping("/account/user/{id}")
    public void deleteUser(@RequestHeader("apiKey") String apiKey, @PathVariable long id){
        apiService.validateAPIKey(apiKey);
        userService.deleteUser(id);
    }

    @DeleteMapping("/account/user/{id}/application/{jid}")
    public void deleteApplication(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long jid){
        apiService.validateAPIKey(apiKey);
        userService.deleteApplication(id, jid);
    }
}
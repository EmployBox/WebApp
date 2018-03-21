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
import isel.ps.employbox.dal.model.Application;
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

    private final UserService userService;
    private final APIService apiService;
    private final UserBinder userBinder;
    private final ApplicationBinder applicationBinder;

    public UserController(UserService userService, APIService apiService, UserBinder userBinder, ApplicationBinder applicationBinder) {
        this.userService = userService;
        this.apiService = apiService;
        this.userBinder = userBinder;
        this.applicationBinder = applicationBinder;
    }

    @GetMapping("/account/user")
    public List<OutUser> getAllUsers(@RequestParam Map<String,String> queryString){
        return userBinder.bindOutput(userService.getAllUsers(queryString));
    }

    @GetMapping("/account/user/{id}")
    public Optional<OutUser> getUser(@PathVariable long id){
        return userService.getUser(id).map(userBinder::bindOutput);
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
        User user = userBinder.bindInput(inUser);
        userService.updateUser(id, user);
    }

    @PutMapping("/account/user/{id}/apply/{jid}")
    public void updateApplication(
            @RequestHeader("apiKey") String apiKey,
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody InApplication inApplication
    ){
        apiService.validateAPIKey(apiKey);

        Application application = applicationBinder.bindInput(inApplication);

        userService.updateApplication(id, jid, application);
    }

    @PostMapping("/account/user/")
    public void createUser(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InUser inUser
    ){
        apiService.validateAPIKey(apiKey);
        User user = userBinder.bindInput(inUser);
        userService.createUser(user);
    }

    @PostMapping("/account/user/{id}/apply/{jid}")
    public void createApplication(
            @RequestHeader("apiKey") String apiKey,
            @RequestBody InApplication inApplication
    ){
        apiService.validateAPIKey(apiKey);
        Application application = applicationBinder.bindInput(inApplication);
        userService.createApplication(application);
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
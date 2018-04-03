package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.ApplicationBinder;
import isel.ps.employbox.model.binder.UserBinder;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.model.output.OutApplication;
import isel.ps.employbox.model.output.OutUser;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.UserService;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users")
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

    @GetMapping
    public List<OutUser> getAllUsers(@RequestParam Map<String,String> queryString){
        return userBinder.bindOutput(userService.getAllUsers(queryString));
    }

    @GetMapping("/{id}")
    public Optional<OutUser> getUser(@PathVariable long id){
        return userService.getUser(id).map(userBinder::bindOutput);
    }

    @GetMapping("/{id}/applications")
    public List<OutApplication> getAllApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return applicationBinder.bindOutput(
                userService.getAllApplications(id, queryString)
        );
    }

    @PutMapping("/{id}")
    public void updateUser(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @RequestBody InUser inUser){
        apiService.validateAPIKey(apiKey);
        if(inUser.getId() != id) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        User user = userBinder.bindInput(inUser);
        userService.updateUser(user);
    }

    @PutMapping("/{id}/applications/{jid}")
    public void updateApplication(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long jid, @RequestBody InApplication inApplication){
        apiService.validateAPIKey(apiKey);
        if(inApplication.getUserId() != id || inApplication.getJobId() != jid) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Application application = applicationBinder.bindInput(inApplication);
        userService.updateApplication(application);
    }

    @PostMapping
    public void createUser(@RequestHeader("apiKey") String apiKey, @RequestBody InUser inUser){
        apiService.validateAPIKey(apiKey);
        User user = userBinder.bindInput(inUser);
        userService.createUser(user);
    }

    @PostMapping("/{id}/applications/{jid}")
    public void createApplication(@PathVariable long id, @PathVariable long jid, @RequestHeader("apiKey") String apiKey, @RequestBody InApplication inApplication){
        if(id != inApplication.getUserId() || jid != inApplication.getJobId()) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        apiService.validateAPIKey(apiKey);
        Application application = applicationBinder.bindInput(inApplication);
        userService.createApplication(application);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader("apiKey") String apiKey, @PathVariable long id){
        apiService.validateAPIKey(apiKey);
        userService.deleteUser(id);
    }

    @DeleteMapping("/{id}/applications/{jid}")
    public void deleteApplication(@RequestHeader("apiKey") String apiKey, @PathVariable long id, @PathVariable long jid){
        apiService.validateAPIKey(apiKey);
        userService.deleteApplication(id, jid);
    }
}
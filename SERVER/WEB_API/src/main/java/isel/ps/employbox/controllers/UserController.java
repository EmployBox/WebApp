package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.ApplicationBinder;
import isel.ps.employbox.model.binder.UserBinder;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.User;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.input.InUser;
import isel.ps.employbox.model.output.HalCollection;
import isel.ps.employbox.model.output.OutApplication;
import isel.ps.employbox.model.output.OutUser;
import isel.ps.employbox.services.APIService;
import isel.ps.employbox.services.UserService;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

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
    public HalCollection getAllUsers(@RequestParam Map<String,String> queryString){
        return userBinder.bindOutput(
                userService.getAllUsers(queryString),
                this.getClass()
        );
    }

    @GetMapping("/{id}")
    public OutUser getUser(@PathVariable long id){
        return userBinder.bindOutput( userService.getUser(id));
    }

    @GetMapping("/{id}/applications")
    public HalCollection getAllApplications(@PathVariable long id, @RequestParam Map<String,String> queryString){
        return applicationBinder.bindOutput(
                userService.getAllApplications(id),//, queryString),
                this.getClass(),
                id
        );
    }

    @GetMapping("/{id}/applications/{jid}")
    public OutApplication getApplication(@PathVariable long id, @PathVariable long jid, @RequestParam Map<String,String> queryString){
        return applicationBinder.bindOutput(
                userService.getApplication(id, jid)
        );
    }

    @PutMapping("/{id}")
    public void updateUser( @PathVariable long id, @RequestBody InUser inUser){
        if(inUser.getId() != id) throw new BadRequestException(badRequest_IdsMismatch);
        User user = userBinder.bindInput(inUser);
        userService.updateUser(user);
    }

    @PutMapping("/{id}/applications/{jid}")
    public void updateApplication( @PathVariable long id, @PathVariable long jid, @RequestBody InApplication inApplication){
        if(inApplication.getUserId() != id || inApplication.getJobId() != jid) throw new BadRequestException(badRequest_IdsMismatch);
        Application application = applicationBinder.bindInput(inApplication);
        userService.updateApplication(application);
    }

    @PostMapping
    public void createUser( @RequestBody InUser inUser){
        User user = userBinder.bindInput(inUser);
        userService.createUser(user);
    }

    @PostMapping("/{id}/applications/{jid}")
    public void createApplication(@PathVariable long id, @PathVariable long jid,  @RequestBody InApplication inApplication){
        if(id != inApplication.getUserId() || jid != inApplication.getJobId()) throw new BadRequestException(badRequest_IdsMismatch);
        Application application = applicationBinder.bindInput(inApplication);
        userService.createApplication(id, application);
    }

    @DeleteMapping("/{id}")
    public void deleteUser( @PathVariable long id){
        userService.deleteUser(id);
    }

    @DeleteMapping("/{id}/applications/{jid}")
    public void deleteApplication( @PathVariable long id, @PathVariable long jid){
        userService.deleteApplication(id, jid);
    }
}
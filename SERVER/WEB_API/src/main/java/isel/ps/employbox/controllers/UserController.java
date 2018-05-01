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
import isel.ps.employbox.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.badRequest_IdsMismatch;

@RestController
@RequestMapping("/accounts/users")
public class UserController {

    private final UserService userService;
    private final UserBinder userBinder;
    private final ApplicationBinder applicationBinder;

    public UserController(UserService userService, UserBinder userBinder, ApplicationBinder applicationBinder) {
        this.userService = userService;
        this.userBinder = userBinder;
        this.applicationBinder = applicationBinder;
    }

    @GetMapping
    public Mono<HalCollection> getAllUsers(){
        return userBinder.bindOutput(
                userService.getAllUsers(),
                this.getClass()
        );
    }

    @GetMapping("/{id}")
    public Mono<OutUser> getUser(@PathVariable long id){
        return userBinder.bindOutput( userService.getUser(id));
    }

    @GetMapping("/{id}/applications")
    public Mono<HalCollection> getAllApplications(@PathVariable long id){
        return applicationBinder.bindOutput(
                userService.getAllApplications(id),
                this.getClass(),
                id
        );
    }

    @GetMapping("/{id}/applications/{jid}")
    public Mono<OutApplication> getApplication(@PathVariable long id, @PathVariable long jid){
        return applicationBinder.bindOutput( userService.getApplication(id, jid) );
    }

    @PutMapping("/{id}")
    public Mono<Void> updateUser(
            @PathVariable long id,
            @RequestBody InUser inUser,
            Authentication authentication)
    {
        if(inUser.getId() != id) throw new BadRequestException(badRequest_IdsMismatch);
        User user = userBinder.bindInput(inUser);
        return userService.updateUser(user, authentication.getName());
    }

    @PutMapping("/{id}/applications/{jid}")
    public Mono<Void> updateApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody InApplication inApplication,
            Authentication authentication)
    {
        if(inApplication.getAccountId() != id || inApplication.getJobId() != jid)
            throw new BadRequestException(badRequest_IdsMismatch);
        Application application = applicationBinder.bindInput(inApplication);
        return userService.updateApplication(application, authentication.getName());
    }

    @PostMapping
    public Mono<OutUser> createUser( @RequestBody InUser inUser ){
        User user = userBinder.bindInput(inUser);
        return userBinder.bindOutput( userService.createUser(user) );
    }

    @PostMapping("/{id}/applications/{jid}")
    public Mono<OutApplication> createApplication(@PathVariable long id, @PathVariable long jid,  @RequestBody InApplication inApplication, Authentication authentication){
        if(id != inApplication.getAccountId() || jid != inApplication.getJobId())
            throw new BadRequestException(badRequest_IdsMismatch);
        Application application = applicationBinder.bindInput(inApplication);
        return applicationBinder.bindOutput( userService.createApplication(id, application, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser( @PathVariable long id, Authentication authentication){
        return userService.deleteUser(id, authentication.getName());
    }

    @DeleteMapping("/{id}/applications/{jid}")
    public Mono<Void> deleteApplication( @PathVariable long id, @PathVariable long jid, Authentication authentication){
        return userService.deleteApplication(id, jid, authentication.getName());
    }
}
package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binder.ApplicationBinder;
import isel.ps.employbox.model.binder.UserBinder;
import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.input.InUserAccount;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutApplication;
import isel.ps.employbox.model.output.OutUser;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users")
public class UserAccountController {

    private final UserAccountService userAccountService;
    private final UserBinder userBinder;
    private final ApplicationBinder applicationBinder;

    public UserAccountController(UserAccountService userAccountService, UserBinder userBinder, ApplicationBinder applicationBinder) {
        this.userAccountService = userAccountService;
        this.userBinder = userBinder;
        this.applicationBinder = applicationBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage> getAllUsers(){
        return userBinder.bindOutput(
                userAccountService.getAllUsers(),
                this.getClass()
        );
    }

    @GetMapping("/{id}")
    public Mono<OutUser> getUser(@PathVariable long id){
        return userBinder.bindOutput( userAccountService.getUser(id));
    }

    @GetMapping("/{id}/applications")
    public Mono<HalCollectionPage> getAllApplications(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page

    ){
        return applicationBinder.bindOutput(
                userAccountService.getAllApplications(id, page),
                this.getClass(),
                id
        );
    }

    @GetMapping("/{id}/applications/{jid}")
    public Mono<OutApplication> getApplication(@PathVariable long id, @PathVariable long jid){
        return applicationBinder.bindOutput( userAccountService.getApplication(id, jid) );
    }

    @PostMapping
    public Mono<OutUser> createUser( @RequestBody InUserAccount inUserAccount){
        UserAccount userAccount = userBinder.bindInput(inUserAccount);
        return userBinder.bindOutput( userAccountService.createUser(userAccount) );
    }

    @PostMapping("/{id}/applications/{jid}")
    public Mono<OutApplication> createApplication(@PathVariable long id, @PathVariable long jid,  @RequestBody InApplication inApplication, Authentication authentication){
        if(id != inApplication.getAccountId() || jid != inApplication.getJobId())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Application application = applicationBinder.bindInput(inApplication);
        return applicationBinder.bindOutput( userAccountService.createApplication(id, application, authentication.getName()));
    }

    @PutMapping("/{id}")
    public Mono<Void> updateUser(
            @PathVariable long id,
            @RequestBody InUserAccount inUserAccount,
            Authentication authentication
    ) {
        if(inUserAccount.getId() != id) throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        UserAccount userAccount = userBinder.bindInput(inUserAccount);
        return userAccountService.updateUser(userAccount, authentication.getName());
    }

    @PutMapping("/{id}/applications/{jid}")
    public Mono<Void> updateApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @RequestBody InApplication inApplication,
            Authentication authentication
    ) {
        if(inApplication.getAccountId() != id || inApplication.getJobId() != jid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Application application = applicationBinder.bindInput(inApplication);
        return userAccountService.updateApplication(application, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser( @PathVariable long id, Authentication authentication){
        return userAccountService.deleteUser(id, authentication.getName());
    }

    @DeleteMapping("/{id}/applications/{jid}")
    public Mono<Void> deleteApplication( @PathVariable long id, @PathVariable long jid, Authentication authentication){
        return userAccountService.deleteApplication(id, jid, authentication.getName());
    }
}
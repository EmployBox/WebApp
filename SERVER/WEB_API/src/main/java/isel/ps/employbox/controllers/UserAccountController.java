package isel.ps.employbox.controllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.ApplicationBinder;
import isel.ps.employbox.model.binders.UserBinder;
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

import java.util.concurrent.CompletableFuture;

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
    public Mono<HalCollectionPage<UserAccount>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer ratingLow,
            @RequestParam(required = false) Integer ratingHigh
    ){
        CompletableFuture<HalCollectionPage<UserAccount>> future = userAccountService.getAllUsers(page, pageSize, name, ratingLow, ratingHigh)
                .thenCompose(userAccountCollectionPage -> userBinder.bindOutput(userAccountCollectionPage, this.getClass()));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{id}")
    public Mono<OutUser> getUser(@PathVariable long id){
        CompletableFuture<OutUser> future = userAccountService.getUser(id)
                .thenCompose(userBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @GetMapping("/{id}/applications")
    public Mono<HalCollectionPage<Application>> getAllApplications(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        CompletableFuture<HalCollectionPage<Application>> future = userAccountService.getAllApplications(id, page, pageSize)
                .thenCompose(applicationCollectionPage -> applicationBinder.bindOutput(applicationCollectionPage, this.getClass(), id));
        return Mono.fromFuture(future);
    }

    @GetMapping("/{id}/jobs/{jid}/applications/{apId}")
    public Mono<OutApplication> getApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @PathVariable long apId
    ){
        CompletableFuture<OutApplication> future = userAccountService.getApplication(id, jid, apId)
                .thenCompose(applicationBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<OutUser> createUser( @RequestBody InUserAccount inUserAccount){
        UserAccount userAccount = userBinder.bindInput(inUserAccount);
        CompletableFuture<OutUser> future = userAccountService.createUser(userAccount)
                .thenCompose(userBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PostMapping("/{id}/jobs/{jid}/applications")
    public Mono<OutApplication> createApplication(@PathVariable long id, @PathVariable long jid,  @RequestBody InApplication inApplication, Authentication authentication){
        if(id != inApplication.getAccountId() || jid != inApplication.getJobId())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Application application = applicationBinder.bindInput(inApplication);

        CompletableFuture<OutApplication> future = userAccountService.createApplication(id, application, authentication.getName())
                .thenCompose(applicationBinder::bindOutput);
        return Mono.fromFuture(future);
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

    @PutMapping("/{id}/jobs/{jid}/applications/{apId}")
    public Mono<Void> updateApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @PathVariable long apId,
            @RequestBody InApplication inApplication,
            Authentication authentication
    ) {
        if(inApplication.getAccountId() != id || inApplication.getJobId() != jid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Application application = applicationBinder.bindInput(inApplication);
        return userAccountService.updateApplication(application, authentication.getName(), apId);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser( @PathVariable long id, Authentication authentication){
        return userAccountService.deleteUser(id, authentication.getName());
    }

    @DeleteMapping("/{id}/jobs/{jid}/applications/{apId}")
    public Mono<Void> deleteApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @PathVariable long apId,
            Authentication authentication
    ){
        return userAccountService.deleteApplication(id, jid, apId, authentication.getName());
    }
}
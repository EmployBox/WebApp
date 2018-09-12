package isel.ps.employbox.controllers.UserAccountControllers;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.jobs.ApplicationBinder;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.output.OutApplication;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/jobs/{jid}/applications")
public class UserJobApplicationController {
    private final UserAccountService userAccountService;
    private final ApplicationBinder applicationBinder;

    public UserJobApplicationController(UserAccountService userAccountService, ApplicationBinder applicationBinder) {
        this.userAccountService = userAccountService;
        this.applicationBinder = applicationBinder;
    }

    @GetMapping("/{apId}")
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
    public Mono<OutApplication> createApplication(@PathVariable long id, @PathVariable long jid,  @RequestBody InApplication inApplication, Authentication authentication){
        if(jid != inApplication.getJobId())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        Application application = applicationBinder.bindInput(inApplication);

        CompletableFuture<OutApplication> future = userAccountService.createApplication(id, inApplication.getAccountId(), application, authentication.getName())
                .thenCompose(applicationBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @PutMapping("/{apId}")
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

    @DeleteMapping("/{apId}")
    public Mono<Void> deleteApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @PathVariable long apId,
            Authentication authentication
    ){
        return userAccountService.deleteApplication(id, jid, apId, authentication.getName());
    }
}

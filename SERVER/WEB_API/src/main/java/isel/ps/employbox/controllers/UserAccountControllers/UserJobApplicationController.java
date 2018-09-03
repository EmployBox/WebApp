package isel.ps.employbox.controllers.UserAccountControllers;

import isel.ps.employbox.model.binders.jobs.ApplicationBinder;
import isel.ps.employbox.model.output.OutApplication;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/users/{id}/jobs/{jid}/applications/{apId}")
public class UserJobApplicationController {
    private final UserAccountService userAccountService;
    private final ApplicationBinder applicationBinder;

    public UserJobApplicationController(UserAccountService userAccountService, ApplicationBinder applicationBinder) {
        this.userAccountService = userAccountService;
        this.applicationBinder = applicationBinder;
    }

    @GetMapping
    public Mono<OutApplication> getApplication(
            @PathVariable long id,
            @PathVariable long jid,
            @PathVariable long apId
    ){
        CompletableFuture<OutApplication> future = userAccountService.getApplication(id, jid, apId)
                .thenCompose(applicationBinder::bindOutput);
        return Mono.fromFuture(future);
    }
}

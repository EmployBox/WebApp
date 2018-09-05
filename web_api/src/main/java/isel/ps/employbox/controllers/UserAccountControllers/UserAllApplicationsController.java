package isel.ps.employbox.controllers.UserAccountControllers;

import isel.ps.employbox.model.binders.jobs.ApplicationBinder;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.services.UserAccountService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/accounts/users/{id}/applications")
public class UserAllApplicationsController {
    private final UserAccountService userAccountService;
    private final ApplicationBinder applicationBinder;

    public UserAllApplicationsController(UserAccountService userAccountService, ApplicationBinder applicationBinder) {
        this.userAccountService = userAccountService;
        this.applicationBinder = applicationBinder;
    }

    @GetMapping
    public Mono<HalCollectionPage<Application>> getAllApplications(
            @PathVariable long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        CompletableFuture<HalCollectionPage<Application>> future = userAccountService.getAllApplications(id, page, pageSize)
                .thenCompose(applicationCollectionPage -> applicationBinder.bindOutput(applicationCollectionPage, this.getClass(), id));
        return Mono.fromFuture(future);
    }


}

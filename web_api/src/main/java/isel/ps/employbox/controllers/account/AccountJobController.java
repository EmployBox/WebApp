package isel.ps.employbox.controllers.account;

import isel.ps.employbox.model.binders.jobs.JobBinder;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.services.AccountService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/accounts/{accountId}/jobs/offered")
public class AccountJobController {
    private final JobBinder jobBinder;
    private final AccountService accountService;

    public AccountJobController(JobBinder jobBinder, AccountService accountService) {
        this.jobBinder = jobBinder;
        this.accountService = accountService;
    }

    @GetMapping
    public Mono<HalCollectionPage<Job>> getOfferedJobs(
            @PathVariable long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ){

        return Mono.fromFuture(
                accountService.getOfferedJob(accountId, page, pageSize, orderColumn, orderClause)
                        .thenCompose( jobCollectionPage -> jobBinder.bindOutput(jobCollectionPage , this.getClass(), accountId))
        );
    }

}

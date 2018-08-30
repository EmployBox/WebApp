package isel.ps.employbox.controllers.jobs;


import isel.ps.employbox.model.binders.Jobs.ScheduleBinder;
import isel.ps.employbox.model.entities.jobs.Schedule;
import isel.ps.employbox.model.input.InSchedule;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutSchedule;
import isel.ps.employbox.services.ScheduleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/jobs/{jobId}/schedules")
public class ScheduleController {

    ScheduleService scheduleService = new ScheduleService();
    ScheduleBinder scheduleBinder = new ScheduleBinder();

    @GetMapping
    public Mono<HalCollectionPage<Schedule>> getAllSchedules(
            @PathVariable long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderColumn,
            @RequestParam(required = false, defaultValue = "ASC") String orderClause
    ) {
        CompletableFuture<HalCollectionPage<Schedule>> future = scheduleService.getAllSchedules(jobId, page, pageSize, orderColumn, orderClause)
                .thenCompose( collectionPage -> scheduleBinder.bindOutput( collectionPage, this.getClass(), jobId));

        return Mono.fromFuture(future);
    }

    @PutMapping("/{scheduleId}")
    public Mono<Void> updateSchedule(
            @PathVariable long jobId,
            @PathVariable long scheduleId,
            @RequestBody InSchedule inSchedule,
            Authentication authentication
    ) {
        return Mono.fromFuture(scheduleService.updateSchedule(jobId, scheduleId,  authentication.getName(), inSchedule));
    }

    @PostMapping
    public Mono<Schedule> createSchedule(
            @PathVariable long jobId,
            @RequestBody InSchedule inSchedule,
            Authentication authentication
    ) {
        Schedule schedule = scheduleBinder.bindInput(inSchedule);

        return Mono.fromFuture(scheduleService.createSchedule(jobId, authentication.getName(), schedule));
    }

    @DeleteMapping("/{scheduleId}")
    public Mono<Void> deleteSchedule(
            @PathVariable long jobId,
            @PathVariable long scheduleId,
            Authentication authentication
    ){
        return Mono.fromFuture( scheduleService.deleteSchedule(jobId, scheduleId, authentication.getName()));
    }

    @GetMapping("/{scheduleId}")
    public Mono<OutSchedule> getSchedule(
            @PathVariable long scheduleId
    ) {
        return null;
    }
}

package isel.ps.employbox.model.binder.CurriclumBinders;

import isel.ps.employbox.model.binder.ModelBinder;
import isel.ps.employbox.model.entities.CurriculumChilds.PreviousJob;
import isel.ps.employbox.model.output.OutPreviousJobs;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class PreviousJobsBinder implements ModelBinder<PreviousJob, OutPreviousJobs, Void> {
    @Override
    public Mono<OutPreviousJobs> bindOutput(CompletableFuture<PreviousJob> outPreviousJobsCompletableFuture) {
        return Mono.fromFuture(
                outPreviousJobsCompletableFuture.thenApply(
                        previousJob -> new OutPreviousJobs(
                                previousJob.getIdentityKey(),
                                previousJob.getAccountId(),
                                previousJob.getCurriculumId(),
                                previousJob.getCompanyName(),
                                previousJob.getBeginDate().toString(),
                                previousJob.getEndDate().toString(),
                                previousJob.getWorkLoad(),
                                previousJob.getRole())
                )
        );
    }

    @Override
    public PreviousJob bindInput(Void object) {
        throw new NotImplementedException();
    }
}

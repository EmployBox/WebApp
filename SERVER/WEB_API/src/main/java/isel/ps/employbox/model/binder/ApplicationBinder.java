package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.Application;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.output.OutApplication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class ApplicationBinder implements ModelBinder<Application,OutApplication,InApplication> {


    public Mono<OutApplication> bindOutput(CompletableFuture<Application> applicationCompletableFuture) {
        return Mono.fromFuture(
                applicationCompletableFuture.thenApply(
                        application ->
                                new OutApplication(
                                        application.getAccountId(),
                                        application.getJobId(),
                                        application.getCurriculumId(),
                                        application.getDate())
                )
        );
    }

    @Override
    public Application bindInput(InApplication obj) {
        return new Application (obj.getUserId(), obj.getJobId(), obj.getCurriculumId(), obj.getDate());
    }
}

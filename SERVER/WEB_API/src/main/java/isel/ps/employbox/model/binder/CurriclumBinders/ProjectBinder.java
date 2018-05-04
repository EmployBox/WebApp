package isel.ps.employbox.model.binder.CurriclumBinders;

import isel.ps.employbox.model.binder.ModelBinder;
import isel.ps.employbox.model.entities.CurriculumChilds.Project;
import isel.ps.employbox.model.output.OutProject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class ProjectBinder implements ModelBinder<Project, OutProject, Void> {

    @Override
    public Mono<OutProject> bindOutput(CompletableFuture<Project> object) {
        return Mono.fromFuture(
                object.thenApply(
                        curr -> new OutProject(
                                curr.getIdentityKey(),
                                curr.getAccountId(),
                                curr.getCurriculumId(),
                                curr.getName(),
                                curr.getDescription()
                                )
                )
        );
    }

    @Override
    public Project bindInput(Void object) {
        throw new NotImplementedException();
    }
}

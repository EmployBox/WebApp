package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.input.curricula.childs.InProject;
import isel.ps.employbox.model.output.OutProject;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class ProjectBinder implements ModelBinder<Project, OutProject, InProject> {

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
    public Project bindInput(InProject inProject) {
        return new Project(
                inProject.getAccountId(),
                inProject.getCurriculumId(),
                inProject.getName(),
                inProject.getDescription(),
                inProject.getVersion(),
                inProject.getProjectId()
        );
    }
}

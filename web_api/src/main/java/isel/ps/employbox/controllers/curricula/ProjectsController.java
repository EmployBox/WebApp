package isel.ps.employbox.controllers.curricula;

import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.ProjectBinder;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.input.curricula.childs.InProject;
import isel.ps.employbox.model.output.Collections.HalCollectionPage;
import isel.ps.employbox.model.output.OutProject;
import isel.ps.employbox.services.curricula.CurriculumService;
import isel.ps.employbox.services.curricula.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/projects")
public class ProjectsController {

    private final ProjectBinder projectBinder;
    private final CurriculumService curriculumService;
    private final ProjectService projectService;

    public ProjectsController(ProjectBinder projectBinder, CurriculumService curriculumService, ProjectService projectService) {
        this.projectBinder = projectBinder;
        this.curriculumService = curriculumService;
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    public Mono<OutProject> getProjectFromCurriculum(
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long projectId
    ){
        CompletableFuture<OutProject> future = curriculumService.getCurriculumChild(Project.class, id, cid, projectId)
                .thenCompose(projectBinder::bindOutput);
        return Mono.fromFuture(future);
    }

    @GetMapping
    public Mono<HalCollectionPage<Project>> getProjects(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        CompletableFuture<HalCollectionPage<Project>> future = projectService.getCurriculumProjects(cid, page, pageSize)
                .thenCompose(projectCollectionPage -> projectBinder.bindOutput(projectCollectionPage, this.getClass(), id, cid));
        return Mono.fromFuture(future);
    }

    @PostMapping
    public Mono<Project> addProject(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InProject inProject,
            Authentication authentication)
    {
        Project project = projectBinder.bindInput(inProject);
        return Mono.fromFuture(projectService.addProjectToCurriculum(id,cid, project,authentication.getName()));
    }

    @PutMapping("/{pjId}")
    public Mono<Void> updateProject(
            @PathVariable long pjId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody InProject inProject,
            Authentication authentication
    ){
        Project project = projectBinder.bindInput(inProject);
        if(project.getCurriculumId() != cid || project.getIdentityKey() != pjId)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return projectService.updateProject(pjId, id, cid, project, authentication.getName() );
    }

    @DeleteMapping("/{pjId}")
    public Mono<Void> deleteProject(
            @PathVariable long pjId,
            @PathVariable long id,
            @PathVariable long cid,
            Authentication authentication)
    {
        return projectService.deleteProject(pjId, id, cid, authentication.getName());
    }
}

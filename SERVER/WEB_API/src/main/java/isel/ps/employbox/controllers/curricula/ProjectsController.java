package isel.ps.employbox.controllers.curricula;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.model.binders.curricula.ProjectBinder;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.output.HalCollectionPage;
import isel.ps.employbox.model.output.OutProject;
import isel.ps.employbox.services.curricula.CurriculumService;
import isel.ps.employbox.services.curricula.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static isel.ps.employbox.ErrorMessages.BAD_REQUEST_IDS_MISMATCH;

@RestController
@RequestMapping("/accounts/users/{id}/curricula/{cid}/projects")
public class ProjectsController {

    private final ProjectBinder projectBinder;
    private final CurriculumService curriculumService;
    private final DataRepository<Project, Long> projectRepo;
    private final ProjectService projectService;

    public ProjectsController(ProjectBinder projectBinder, CurriculumService curriculumService, DataRepository<Project, Long> projectRepo, ProjectService projectService) {
        this.projectBinder = projectBinder;
        this.curriculumService = curriculumService;
        this.projectRepo = projectRepo;
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    public Mono<OutProject> getProjects (
            @PathVariable long id,
            @PathVariable long cid,
            @PathVariable long projectId){
        return projectBinder.bindOutput(curriculumService.getCurriculumChild(projectRepo, id, cid, projectId));
    }

    @GetMapping
    public Mono<HalCollectionPage> getProjects(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int numberOfItems
    ) {
        return projectBinder.bindOutput(
                projectService.getCurriculumProjects(cid, page, numberOfItems),
                this.getClass(),
                id,
                cid
        );
    }

    @PostMapping
    public Mono<Project> addProject(
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody Project project,
            Authentication authentication)
    {
        return Mono.fromFuture( projectService.addProjectToCurriculum(id,cid, project,authentication.getName()));
    }

    @PutMapping("/{ceId}")
    public Mono<Void> updateProject(
            @PathVariable long ceId,
            @PathVariable long id,
            @PathVariable long cid,
            @RequestBody Project project,
            Authentication authentication
    ){
        if(project.getAccountId() != id || project.getCurriculumId() != cid)
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
        return projectService.updateProject(ceId, id, cid, project,authentication.getName() );
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

package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.input.curricula.childs.InProject;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class ProjectService {
    private final DataRepository<Project, Long> projectRepo;
    private final CurriculumService curriculumService;
    private final DataRepository<Curriculum, Long> curriculumRepo;

    public ProjectService(DataRepository<Project, Long> projectRepo, CurriculumService curriculumService, DataRepository<Curriculum, Long> curriculumRepo) {
        this.projectRepo = projectRepo;
        this.curriculumService = curriculumService;
        this.curriculumRepo = curriculumRepo;
    }

    public CompletableFuture<CollectionPage<Project>> getCurriculumProjects(long curriculumId, int page, int pageSize) {
        return curriculumRepo.findById(curriculumId)
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture(projectRepo, page, pageSize, new Pair<>("curriculumId", curriculumId)));
    }

    public CompletableFuture<Project> addProjectToCurriculum (
            long accountId,
            long curriculumId,
            Project project,
            String email
    ) {
        if(project.getAccountId() != accountId || project.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        return curriculumService.getCurriculum(accountId, curriculumId,email)
                .thenCompose(curriculum -> projectRepo.create( project))
                .thenApply(res -> project);
    }

    public Mono<Void> updateProject(
            long projectId,
            long accountId,
            long curriculumId,
            Project project,
            String email
    ) {
        if(project.getIdentityKey() != projectId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        return Mono.fromFuture(curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> projectRepo.update(project))
        );
    }

    public Mono<Void> deleteProject(
            long projectId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(
                curriculumService.getCurriculum(accountId, curriculumId, email)
                        .thenCompose(curriculum -> projectRepo.deleteById(projectId))
        );
    }
}

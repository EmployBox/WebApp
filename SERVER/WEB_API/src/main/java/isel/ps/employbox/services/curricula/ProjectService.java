package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.model.entities.CurriculumChilds.Project;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class ProjectService {
    private final DataRepository<Project, Long> projectRepo;
    private final CurriculumService curriculumService;

    public ProjectService(DataRepository<Project, Long> projectRepo, CurriculumService curriculumService) {
        this.projectRepo = projectRepo;
        this.curriculumService = curriculumService;
    }


    public CompletableFuture<Stream<Project>> getCurriculumProjects(long curriculumId){
        return projectRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply(Collection::stream);
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

package isel.ps.employbox.services.curriculumServices;

import com.github.jayield.rapper.DataRepository;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binder.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.Project;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class ProjectService {
    private final DataRepository<Project, Long> projectRepo;
    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final CurriculumService curriculumService;

    public ProjectService(DataRepository<Project, Long> projectRepo, DataRepository<Curriculum, Long> curriculumRepo, CurriculumService curriculumService) {
        this.projectRepo = projectRepo;
        this.curriculumRepo = curriculumRepo;
        this.curriculumService = curriculumService;
    }


    public CompletableFuture<CollectionPage<Project>> getCurriculumProjects(long curriculumId, int page){
        return curriculumRepo.findById(curriculumId)
                .thenApply( ocurriculum -> ocurriculum.orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(Curriculum::getProjects)
                .thenApply( list -> new CollectionPage<>(
                        list.size(),
                        page,
                        list.stream()
                                .skip(CollectionPage.PAGE_SIZE * page)
                                .limit(CollectionPage.PAGE_SIZE))
                );
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
                .thenCompose( __ -> projectRepo.create( project))
                .thenApply( res -> {
                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                    return project;
                });
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
                .thenCompose(__ -> projectRepo.update(project)
                        .thenAccept(
                                res -> {
                                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                }
                        ))
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
                        .thenCompose(__ -> projectRepo.deleteById(projectId))
                        .thenAccept(
                                res -> {
                                    if (res.isPresent()) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                                }
                        )
        );
    }
}

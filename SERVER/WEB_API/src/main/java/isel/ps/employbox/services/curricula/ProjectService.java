package isel.ps.employbox.services.curricula;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.MapperRegistry;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import com.github.jayield.rapper.utils.Pair;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.services.ServiceUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.services.ServiceUtils.handleExceptions;

@Service
public class ProjectService {
    private final CurriculumService curriculumService;

    public ProjectService( CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    public CompletableFuture<CollectionPage<Project>> getCurriculumProjects(long curriculumId, int page, int pageSize) {
        UnitOfWork unitOfWork = new UnitOfWork();

        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unitOfWork);
        CompletableFuture<CollectionPage<Project>> future = curriculumMapper.findById(curriculumId)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenApply(ocurriculum -> ocurriculum.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM)))
                .thenCompose(__ -> ServiceUtils.getCollectionPageFuture(Project.class, page, pageSize, new Pair<>("curriculumId", curriculumId)));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Project> addProjectToCurriculum (
            long accountId,
            long curriculumId,
            Project project,
            String email
    ) {
        if(project.getAccountId() != accountId || project.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
        UnitOfWork unitOfWork = new UnitOfWork();

        DataMapper<Project, Long> projectMapper = getMapper(Project.class, unitOfWork);
        CompletableFuture<Project> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> projectMapper.create(project))
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> project));
        return handleExceptions(future, unitOfWork);
    }

    public CompletableFuture<Project> getProject(long projectId){
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Project, Long> projectMapper = MapperRegistry.getMapper(Project.class, unitOfWork);

        return projectMapper.findById(projectId)
                .thenCompose(res -> unitOfWork.commit().thenApply(__ -> res))
                .thenApply(oproject -> oproject.orElseThrow( ()-> new ResourceNotFoundException("Project not found")));
    }

    public Mono<Void> updateProject(
            long projectId,
            long accountId,
            long curriculumId,
            Project project,
            String email
    ) {
        if(project.getAccountId() != accountId)
            throw new BadRequestException(ErrorMessages.UN_AUTHORIZED);
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Project, Long> projectMapper = getMapper(Project.class, unitOfWork);
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose( __ -> getProject(projectId))
                .thenCompose( __ -> projectMapper.update(project))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(handleExceptions(future, unitOfWork));
    }

    public Mono<Void> deleteProject(
            long projectId,
            long accountId,
            long curriculumId,
            String email
    ) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Project, Long> projectMapper = getMapper(Project.class, unitOfWork);
        CompletableFuture<Void> future = curriculumService.getCurriculum(accountId, curriculumId, email)
                .thenCompose(curriculum -> projectMapper.deleteById(projectId))
                .thenCompose(res -> unitOfWork.commit());
        return Mono.fromFuture(
                handleExceptions(future, unitOfWork)
        );
    }
}

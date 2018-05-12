package isel.ps.employbox.services.curriculumServices;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.entities.CurriculumChilds.*;
import isel.ps.employbox.model.entities.UserAccount;
import isel.ps.employbox.services.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Service
public class CurriculumService {
    private final UserService userService;

    private final DataRepository<Curriculum, Long> curriculumRepo;
    private final DataRepository<PreviousJobs, Long> previousJobsRepo;
    private final DataRepository<AcademicBackground, Long> academicBackgroundRepo;
    private final DataRepository<Project, Long> projectRepo;
    private final DataRepository<CurriculumExperience, Long> curriculumExperienceRepo;

    public CurriculumService(UserService userService,
                             DataRepository<Curriculum, Long> curriculumRepo,
                             DataRepository<PreviousJobs, Long> previousJobsRepo,
                             DataRepository<AcademicBackground, Long> academicBackgroundRepo,
                             DataRepository<Project, Long> projectRepo,
                             DataRepository<CurriculumExperience, Long> curriculumExperienceRepo)
    {
        this.userService = userService;
        this.curriculumRepo = curriculumRepo;
        this.previousJobsRepo = previousJobsRepo;
        this.academicBackgroundRepo = academicBackgroundRepo;
        this.projectRepo = projectRepo;
        this.curriculumExperienceRepo = curriculumExperienceRepo;
    }

    public CompletableFuture<Curriculum> createCurriculum(long userId, Curriculum curriculum, String email) {
        if (curriculum.getAccountId() != userId)
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        return userService.getUser(userId, email)
                .thenCompose(__ -> curriculumRepo.create(curriculum))
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                    return curriculum;
                }).thenCompose(
                        (__) -> {
                            List<CompletableFuture<Boolean>> list = new ArrayList<>();
                            populateChildList(list, curriculum, userId);
                            return list
                                    .stream()
                                    .reduce(CompletableFuture.completedFuture(true), (a, b) -> a.thenCombine(b, (a2, b2) -> a2 && b2));
                        }
                ).thenApply(
                        res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.CHILDS_CREATION);
                            return curriculum;
                        }
                );
    }

    private void populateChildList(List<CompletableFuture<Boolean>> creationList, Curriculum curriculum, long userId){
        creationList.add(curriculum.getPreviousJobs()
                .thenApply(previousJobsList -> {
                    previousJobsList.forEach(curr -> {
                        curr.setAccountId(userId);
                        curr.setCurriculumId(curriculum.getIdentityKey());
                    });
                    return previousJobsList;
                })
                .thenCompose( list -> {
                    if(list.size() == 0)
                        return CompletableFuture.completedFuture(true);
                    return previousJobsRepo.createAll(list);
                })
        );

        creationList.add(curriculum.getAcademicBackground()
                .thenApply(academicBackgroundList -> {
                    academicBackgroundList.forEach(curr -> {
                                curr.setAccountId(userId);
                                curr.setCurriculumId(curriculum.getIdentityKey());
                            }
                    );
                    return academicBackgroundList;
                }).thenCompose(
                        list -> {
                            if(list.size() == 0)
                                return CompletableFuture.completedFuture(true);
                            return academicBackgroundRepo.createAll(list);
                        })
        );

        creationList.add(curriculum.getExperiences().thenApply(experienceList -> {
            experienceList.forEach(curr -> {
                curr.setAccountId(userId);
                curr.setCurriculumId(curriculum.getIdentityKey());
            });
            return experienceList;
        }).thenCompose(
                list -> {
                    if(list.size() == 0)
                        return CompletableFuture.completedFuture(true);
                    return curriculumExperienceRepo.createAll(list);
                }
        ));

        creationList.add(curriculum.getProjects()
                .thenApply(projectList -> {
                    projectList.forEach(curr -> {
                        curr.setAccountId(userId);
                        curr.setCurriculumId(curriculum.getIdentityKey());
                    });
                    return projectList;
                })
                .thenCompose(
                        list -> {
                            if(list.size() == 0)
                                return  CompletableFuture.completedFuture(true);
                            return projectRepo.createAll(list);
                        })
        );
    }

    public CompletableFuture<Stream<Curriculum>> getCurricula(long userId, String email)
    {
        return userService.getUser(userId, email)
                .thenCompose( user -> user.getCurricula())
                .thenApply( curricula ->
                        curricula
                                .stream()
                                .filter(curr -> curr.getAccountId() == userId)
        );
    }

    public CompletableFuture<Curriculum> getCurriculum(long userId, long cid, String email) {
        return userService.getUser(userId, email)
                .thenCompose(UserAccount::getCurricula)
                .thenApply(curricula -> {
                    Optional<Curriculum> oret;
                    if (curricula.isEmpty() || !(oret = curricula.stream().filter(curr -> curr.getIdentityKey() == cid).findFirst()).isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_CURRICULUM);
                    return oret.get();
                });
    }

    public Mono<Void> updateCurriculum(Curriculum curriculum, String email) {
        return Mono.fromFuture(
                userService.getUser(curriculum.getAccountId(), email)
                        .thenCompose(__ -> getCurriculum(curriculum.getAccountId(), curriculum.getIdentityKey(), email))
                        .thenCompose(___ -> curriculumRepo.update(curriculum))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_CREATION);
                        })
        );
    }

    public Mono<Void> deleteCurriculum(long userId, long cid, String name) {
        return Mono.fromFuture(
                getCurriculum(userId, cid, name)
                        .thenCompose(curriculumRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.BAD_REQUEST_ITEM_DELETION);
                        })
        );
    }

    public <T extends CurriculumChild & DomainObject<Long>> CompletableFuture<T> getCurriculumChild(
            DataRepository<T,Long> repo,
            long accountId,
            long curriculumId,
            long jexpId)
    {
        return repo.findById(jexpId)
                .thenApply( oChild -> oChild.orElseThrow( ()-> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND)))
                .thenApply( child -> {
                    if(child.getCurriculumId() != curriculumId || child.getAccountId() != accountId)
                        throw new ConflictException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);
                    return child;
                });
    }
}

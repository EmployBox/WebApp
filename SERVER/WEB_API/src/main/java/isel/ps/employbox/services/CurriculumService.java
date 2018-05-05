package isel.ps.employbox.services;

import com.github.jayield.rapper.DataRepository;
import com.github.jayield.rapper.DomainObject;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ConflictException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.CurriculumChilds.*;
import javafx.util.Pair;
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
            throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);

        return userService.getUser(userId, email)
                .thenCompose(__ -> curriculumRepo.create(curriculum))
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
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
                            if (!res) throw new BadRequestException(ErrorMessages.childsCreation);
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
                .thenCompose(User::getCurricula)
                .thenApply(curricula -> {
                    Optional<Curriculum> oret;
                    if (curricula.isEmpty() || !(oret = curricula.stream().filter(curr -> curr.getIdentityKey() == cid).findFirst()).isPresent())
                        throw new ResourceNotFoundException(ErrorMessages.resourceNotfound_curriculum);
                    return oret.get();
                });
    }

    public Mono<Void> updateCurriculum(Curriculum curriculum, String email) {
        return Mono.fromFuture(
                userService.getUser(curriculum.getAccountId(), email)
                        .thenCompose(__ -> getCurriculum(curriculum.getAccountId(), curriculum.getIdentityKey(), email))
                        .thenCompose(___ -> curriculumRepo.update(curriculum))
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemCreation);
                        })
        );
    }

    public Mono<Void> deleteCurriculum(long userId, long cid, String name) {
        return Mono.fromFuture(
                getCurriculum(userId, cid, name)
                        .thenCompose(curriculumRepo::delete)
                        .thenAccept(res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
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
                .thenApply( oChild -> oChild.orElseThrow( ()-> new ResourceNotFoundException(ErrorMessages.resourceNotfound)))
                .thenApply( child -> {
                    if(child.getCurriculumId() != curriculumId || child.getAccountId() != accountId)
                        throw new ConflictException(ErrorMessages.badRequest_IdsMismatch);
                    return child;
                });
    }

    public CompletableFuture<Stream<PreviousJobs>> getCurriculumPreviousJobs(long curriculumId){
        return previousJobsRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply( opreviousJobs -> opreviousJobs.stream());
    }

    public CompletableFuture<Stream<Project>> getCurriculumProjects(long curriculumId){
        return projectRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply( projectList -> projectList.stream());
    }

    public CompletableFuture<Stream<AcademicBackground>> getCurriculumAcademicBackgrounds(long curriculumId){
        return academicBackgroundRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply( academicBackgrounds -> academicBackgrounds.stream());
    }

    public CompletableFuture<Stream<CurriculumExperience>> getCurriculumExperiences(long curriculumId){
        return curriculumExperienceRepo.findWhere(new Pair<>("curriculumId",curriculumId))
                .thenApply( curriculumExperiences -> curriculumExperiences.stream());
    }


    public CompletableFuture<PreviousJobs> addPreviousJobToCurriculum (
            long accountId,
            long curriculumId,
            PreviousJobs previousJobs,
            String email
    ) {
        if(previousJobs.getAccountId() != accountId || previousJobs.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.badRequest_IdsMismatch);
        return getCurriculum(accountId, curriculumId,email)
                .thenCompose( __ ->previousJobsRepo.create( previousJobs))
                .thenApply( res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                    return previousJobs;
                });
    }

    public CompletableFuture<AcademicBackground> addAcademicBackgroundToCurriculum (
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        if(academicBackground.getAccountId() != accountId || academicBackground.getCurriculumId() != curriculumId || academicBackgroundId != academicBackground.getAccountId())
            throw new ConflictException(ErrorMessages.badRequest_IdsMismatch);

        return getCurriculum(accountId, curriculumId, email)
                .thenCompose( __ -> academicBackgroundRepo.create( academicBackground))
                .thenApply( res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                    return academicBackground;
                });
    }

    public CompletableFuture<Project> addProjectToCurriculum (
            long accountId,
            long curriculumId,
            Project project,
            String email
    ) {
        if(project.getAccountId() != accountId || project.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.badRequest_IdsMismatch);
        return getCurriculum(accountId, curriculumId,email)
                .thenCompose( __ -> projectRepo.create( project))
                .thenApply( res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                    return project;
                });
    }

    public CompletableFuture<CurriculumExperience> addCurriculumExperienceToCurriculum(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        if (curriculumExperience.getAccountId() != accountId || curriculumExperience.getCurriculumId() != curriculumId)
            throw new ConflictException(ErrorMessages.badRequest_IdsMismatch);
        return getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> curriculumExperienceRepo.create(curriculumExperience))
                .thenApply(res -> {
                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                    return curriculumExperience;
                });
    }

    public Mono<Void> updatePreviousJob(
            long pvjId,
            long accountId,
            long curriculumId,
            PreviousJobs previousJobs,
            String email
    ) {
        if(previousJobs.getAccountId() != pvjId)
            throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);
        return Mono.fromFuture(getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> previousJobsRepo.update(previousJobs))
                .thenAccept(
                        res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        }
                )
        );
    }

    public Mono<Void> updateAcademicBackground(
            long accountId,
            long curriculumId,
            AcademicBackground academicBackground,
            String email
    ) {
        return Mono.fromFuture(getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> academicBackgroundRepo.update(academicBackground)
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        ))
        );
    }

    public Mono<Void> updateProject(
            long projectId,
            long accountId,
            long curriculumId,
            Project project,
            String email
    ) {
        if(project.getIdentityKey() != projectId)
            throw new BadRequestException(ErrorMessages.badRequest_IdsMismatch);
        return Mono.fromFuture(getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> projectRepo.update(project)
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        ))
        );
    }

    public Mono<Void> updateCurriculumExperience(
            long accountId,
            long curriculumId,
            CurriculumExperience curriculumExperience,
            String email
    ) {
        return Mono.fromFuture(getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> curriculumExperienceRepo.update(curriculumExperience)
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        ))
        );
    }


    public Mono<Void> deletePreviousJob(
            long previousJobId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(
                getCurriculum(accountId, curriculumId, email)
                        .thenCompose(__ -> previousJobsRepo.deleteById(previousJobId))
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        )
        );
    }

    public Mono<Void> deleteAcademicBackground(
            long academicBackgroundId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(getCurriculum(accountId, curriculumId, email)
                .thenCompose(__ -> academicBackgroundRepo.deleteById(academicBackgroundId))
                .thenAccept(
                        res -> {
                            if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                        }
                )
        );
    }

    public Mono<Void> deleteProject(
            long projectId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(
                getCurriculum(accountId, curriculumId, email)
                        .thenCompose(__ -> projectRepo.deleteById(projectId))
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        )
        );
    }

    public Mono<Void> deleteCurriculumExperience(
            long curriculumExperienceId,
            long accountId,
            long curriculumId,
            String email
    ) {
        return Mono.fromFuture(
                getCurriculum(accountId, curriculumId, email)
                        .thenCompose(__ -> curriculumExperienceRepo.deleteById(curriculumExperienceId))
                        .thenAccept(
                                res -> {
                                    if (!res) throw new BadRequestException(ErrorMessages.badRequest_ItemDeletion);
                                }
                        )
        );
    }
}

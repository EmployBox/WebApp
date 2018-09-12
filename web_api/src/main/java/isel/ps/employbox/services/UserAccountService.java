package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.mapper.conditions.EqualOrCondition;
import com.github.jayield.rapper.mapper.conditions.LikeCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.*;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.entities.jobs.Job;
import isel.ps.employbox.services.curricula.CurriculumService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.github.jayield.rapper.mapper.MapperRegistry.getMapper;
import static isel.ps.employbox.ErrorMessages.*;
import static isel.ps.employbox.services.ServiceUtils.*;

@Service
public class UserAccountService {

    public CompletableFuture<CollectionPage<UserAccount>> getAllUsers(
            int page,
            int pageSize,
            String name,
            String summary,
            Integer ratingLow,
            Integer ratingHigh,
            String orderColumn,
            String orderClause) {
        List<Condition> conditions = new ArrayList<>();

        evaluateRatingConditions(ratingLow, ratingHigh, conditions);
        conditions.add(new LikeCondition("name", name));
        conditions.add(new LikeCondition("accountType", "USR"));

        conditions = conditions.stream()
                .filter(stringPair -> stringPair.getValue() != null)
                .collect(Collectors.toList());

        conditions.add(new LikeCondition("summary", summary));

        conditions = conditions.stream()
                .filter(stringPair -> stringPair.getValue() != null)
                .collect(Collectors.toList());

        evaluateOrderClauseConditions(orderColumn, orderClause, conditions);


        return getCollectionPageFuture(UserAccount.class, page, pageSize, conditions.toArray(new Condition[conditions.size()]));

    }


    public CompletableFuture<UserAccount> getUser(long id, String... email) {
        UnitOfWork unit = new UnitOfWork();
        return getUser(id, unit, email);
    }

    public CompletableFuture<UserAccount> getUser(long id, UnitOfWork unit, String... email) {
        if (email.length > 1) {
            throw new InvalidParameterException("Only 1 or 2 parameters are allowed for this method");
        }

        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);

        CompletableFuture<UserAccount> future = userMapper.findById(id)
                .thenApply(res -> {
                    if (!res.isPresent()) throw new ResourceNotFoundException(RESOURCE_NOTFOUND_USER);
                    if (email.length == 1 && !res.get().getEmail().equals(email[0]))
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return res.get();
                })
                .thenCompose(userAccount -> unit.commit().thenApply(aVoid -> userAccount));

        return handleExceptions(future, unit);
    }

    public CompletableFuture<Application> getApplication(long userId, long jobId, long apId) {
        UnitOfWork unit = new UnitOfWork();
        return getApplication(userId, jobId, apId, unit);
    }

    private CompletableFuture<Application> getApplication(long userId, long jobId, long apId, UnitOfWork unit) {
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<Application> future = getUser(userId)
                .thenCompose(ignored -> applicationMapper.findById(apId))
                .thenApply(oapplication -> oapplication.orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NOTFOUND_APPLICATION)))
                .thenCompose(application ->
                    application.getJob().getForeignObject(unit)
                            .thenApply( job -> {
                                if (job.getIdentityKey() != jobId)
                                    throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);
                                return application;
                            })
                )
                .thenCompose(application1 -> unit.commit().thenApply(aVoid -> application1));
        return handleExceptions(future, unit);
    }

    public CompletableFuture<CollectionPage<Application>> getAllApplications(long accountId, int page, int pageSize) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        CompletableFuture<CollectionPage<Application>> future = userMapper.findById( accountId)
                .thenApply(ouser -> ouser.orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND_USER)))
                .thenCompose(ignored -> applicationMapper.find(page, pageSize, new EqualAndCondition<Long>("accountId", accountId))
                        .thenCompose(listRes -> applicationMapper.getNumberOfEntries( new EqualAndCondition<Long>("accountId",accountId))
                                .thenCompose(aLong -> unit.commit().thenApply(aVoid -> aLong))
                                .thenApply(collectionSize -> new CollectionPage<Application>(
                                        collectionSize,
                                        pageSize,
                                        page,
                                        listRes
                                ))));
        return handleExceptions(future, unit);
    }

    public CompletableFuture<UserAccount> createUser(UserAccount userAccount) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);

        CompletableFuture<UserAccount> future = userMapper.create(userAccount)
                .thenCompose(aVoid -> unit.commit())
                .thenApply(res -> userAccount)
                .exceptionally(throwable -> {
                    throw new ConflictException(ErrorMessages.CONFLIT_USERNAME_TAKEN);
                });

        return handleExceptions(future, unit);
    }

    public CompletableFuture<Application> createApplication( long userId, Application application, String email) {

        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        AccountService accountService = new AccountService();
        CompletableFuture<Application> future  = accountService.getAccount(userId, email)
                .thenCompose( userAccount -> applicationMapper.find(
                        new EqualAndCondition<>("accountId", userAccount.getIdentityKey()),
                        new EqualAndCondition<>("jobId", application.getJob().getForeignKey())
                        )
                        .thenAccept(list -> {
                            if(list.size() != 0)
                                throw new ForbiddenException(ErrorMessages.ALREADY_EXISTS);
                        })
                )
                .thenCompose(aVoid -> applicationMapper.create( application))
                .thenCompose(aVoid -> unit.commit())
                .thenApply(res -> application);
        return handleExceptions(future, unit);
    }

    public Mono<Void> updateUser(UserAccount userAccount, String email) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        CompletableFuture<Void> future = getUser(userAccount.getIdentityKey(), email)
                .thenCompose(userAccount1 -> userMapper.update( userAccount)
                        .thenCompose(aVoid -> unit.commit()));
        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }

    public Mono<Void> updateApplication(Application application, String email, long apId) {
        if(apId != application.getIdentityKey())
            throw new BadRequestException(BAD_REQUEST_IDS_MISMATCH);

        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unitOfWork);

        CompletableFuture<Void> future = application.getAccount().getForeignObject(unitOfWork)
                .thenCompose(userAccount -> {
                    if (userAccount.getEmail().compareTo(email) != 0)
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED);
                    return applicationMapper.update(application);
                }).thenCompose(aVoid -> unitOfWork.commit());

        return Mono.fromFuture(handleExceptions(future, unitOfWork));
    }

    public Mono<Void> deleteUser(long id, String email) {
        UnitOfWork unit = new UnitOfWork();

        CurriculumService curriculumService = new CurriculumService(this);
        JobService jobService = new JobService();

        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);
        DataMapper<UserAccount, Long> userMapper = getMapper(UserAccount.class, unit);
        DataMapper<Comment, Long> commentMapper = getMapper(Comment.class, unit);
        DataMapper<Curriculum, Long> curriculumMapper = getMapper(Curriculum.class, unit);
        DataMapper<Rating, Rating.RatingKey> ratingMapper = getMapper(Rating.class, unit);
        DataMapper<Follows, Follows.FollowKey> followsMapper = getMapper(Follows.class, unit);
        DataMapper<Job, Long> jobMapper = getMapper(Job.class, unit);


        CompletableFuture<List<Comment>> [] comments = new CompletableFuture[2];
        CompletableFuture<List<Rating>> [] ratings  = new CompletableFuture[2];
        CompletableFuture<List<Follows>> [] follows  = new CompletableFuture[2];

        CompletableFuture<Void> future = getUser(id, email)
                .thenCompose(userAccount -> userAccount.getApplications().apply(unit)
                        .thenCompose(applications -> {
                            List<Long> applicationIds = applications.stream().map(Application::getIdentityKey).collect(Collectors.toList());
                            return applicationMapper.deleteAll(applicationIds);
                        })
                        .thenCompose(__ -> {
                                    comments[0] = commentMapper.find(new EqualAndCondition<Long>("accountIdFrom", userAccount.getIdentityKey()));
                                    comments[1] = commentMapper.find(new EqualAndCondition<Long>("accountIdDest", userAccount.getIdentityKey()));

                                    return CompletableFuture.allOf(comments);
                                }
                        )
                        .thenCompose(
                                aVoid -> {
                                    List<Long> list = new ArrayList<>();
                                    list.addAll( comments[0].join().stream().map(Comment::getIdentityKey).collect(Collectors.toList()));
                                    list.addAll( comments[1].join().stream().map(Comment::getIdentityKey).collect(Collectors.toList()));
                                    return commentMapper.deleteAll(list);
                                }
                        )
                        .thenCompose(__ -> curriculumMapper.find(new EqualAndCondition<>("accountId", id)))
                        .thenCompose(list -> {
                            List<CompletableFuture<Void>> cflist = new ArrayList<>();
                            list.forEach(curr -> cflist.add(curriculumService.deleteCurriculum(userAccount.getIdentityKey(), curr.getIdentityKey(), unit).toFuture()));
                            return CompletableFuture.allOf(cflist.toArray(new CompletableFuture[cflist.size()]));
                        })
                        .thenCompose( aVoid -> {
                                    follows[0] = followsMapper.find(new EqualAndCondition<Long>("accountIdFollower", userAccount.getIdentityKey()));
                                    follows[1] = followsMapper.find(new EqualAndCondition<Long>("accountIdFollowed", userAccount.getIdentityKey()));

                                    return CompletableFuture.allOf(follows);
                                }
                        )
                        .thenCompose(
                                aVoid -> {
                                    Set<Follows.FollowKey> list = new HashSet<>();
                                    list.addAll( follows[0].join().stream().map(curr -> curr.getIdentityKey()).collect(Collectors.toList()));
                                    list.addAll( follows[1].join().stream().map(curr -> curr.getIdentityKey()).collect(Collectors.toList()));
                                    return followsMapper.deleteAll(list);
                                }
                        )
                        .thenCompose( aVoid -> {
                                    ratings[0] = ratingMapper.find(new EqualAndCondition<Long>("accountIdFrom", userAccount.getIdentityKey()));
                                    ratings[1] = ratingMapper.find(new EqualAndCondition<Long>("accountIdTo", userAccount.getIdentityKey()));

                                    return CompletableFuture.allOf(ratings);
                                }
                        )
                        .thenCompose(
                                aVoid -> {
                                    Set<Rating.RatingKey> list = new HashSet<>();
                                    list.addAll( ratings[0].join().stream().map(curr -> curr.getIdentityKey()).collect(Collectors.toList()));
                                    list.addAll( ratings[1].join().stream().map(curr -> curr.getIdentityKey()).collect(Collectors.toList()));
                                    return ratingMapper.deleteAll(list);
                                }
                        )
                        .thenCompose(__ -> jobMapper.find(new EqualAndCondition<>("accountId", id)))
                        .thenCompose(list -> {
                            List<CompletableFuture<Void>> cflist = new ArrayList<>();
                            list.forEach(curr -> cflist.add( jobService.deleteJob(curr.getIdentityKey(), email, unit).toFuture()) );
                            return CompletableFuture.allOf(cflist.toArray(new CompletableFuture[cflist.size()]));
                        })
                        .thenCompose(aVoid -> userMapper.delete(userAccount))
                        .thenCompose(aVoid -> unit.commit()));
        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }

    public Mono<Void> deleteApplication(long userId, long jobId,long apId, String email) {
        UnitOfWork unit = new UnitOfWork();
        DataMapper<Application, Long> applicationMapper = getMapper(Application.class, unit);

        CompletableFuture<Void> future = getUser(userId, unit, email)
                .thenCompose(userAccount -> getApplication(userId, jobId, apId, unit))
                .thenCompose(applicationMapper::delete)
                .thenCompose(aVoid -> unit.commit());

        return Mono.fromFuture(
                handleExceptions(future, unit)
        );
    }
}
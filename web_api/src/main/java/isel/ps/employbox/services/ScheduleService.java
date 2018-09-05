package isel.ps.employbox.services;

import com.github.jayield.rapper.mapper.DataMapper;
import com.github.jayield.rapper.mapper.MapperRegistry;
import com.github.jayield.rapper.mapper.conditions.Condition;
import com.github.jayield.rapper.mapper.conditions.EqualAndCondition;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.ErrorMessages;
import isel.ps.employbox.exceptions.BadRequestException;
import isel.ps.employbox.exceptions.ResourceNotFoundException;
import isel.ps.employbox.exceptions.UnauthorizedException;
import isel.ps.employbox.model.binders.CollectionPage;
import isel.ps.employbox.model.binders.jobs.ScheduleBinder;
import isel.ps.employbox.model.entities.Account;
import isel.ps.employbox.model.entities.jobs.Schedule;
import isel.ps.employbox.model.input.InSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ScheduleService {
    AccountService accountService = new AccountService();
    JobService jobService = new JobService();

    public CompletableFuture<CollectionPage<Schedule>> getAllSchedules(
            long jobId,
            int page,
            int pageSize,
            String orderColumn,
            String orderClause)
    {
        UnitOfWork unitOfWork = new UnitOfWork();
        JobService jobService = new JobService();

        List<Condition> conds = new ArrayList<>();
        conds.add( new EqualAndCondition<>("jobId", jobId));
        ServiceUtils.evaluateOrderClause(orderColumn, orderClause, conds);

        return jobService
                .getJob(jobId, unitOfWork)
                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res))
                .thenCompose( __ -> ServiceUtils.getCollectionPageFuture(Schedule.class, page, pageSize, conds.toArray(new Condition[conds.size()])));
    }

    public CompletableFuture<Void> updateSchedule(long jid, long scheduleId, String email, InSchedule inSchedule) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Schedule, Long> scheduleDataMapper = MapperRegistry.getMapper(Schedule.class, unitOfWork);
        ScheduleBinder scheduleBinder = new ScheduleBinder();

        if (scheduleId != inSchedule.getScheduleId())
            throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

        return accountService.getAccount(email, unitOfWork)
                .thenCompose(account ->
                        scheduleDataMapper.find(
                                new EqualAndCondition<>("accountId", account.getIdentityKey()),
                                new EqualAndCondition<>("scheduleId", scheduleId),
                                new EqualAndCondition<>("jobId", jid)
                        )).thenCompose( schedules -> {
                            if(schedules.size() == 0)
                                throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND);
                            return scheduleDataMapper.update(scheduleBinder.bindInput(inSchedule));
                        }
                ).thenCompose( res ->unitOfWork.commit().thenApply(__ -> res));
    }

    public CompletableFuture<Schedule> createSchedule(long jobId, String email, Schedule newSchedule) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Schedule, Long> scheduleDataMapper = MapperRegistry.getMapper(Schedule.class, unitOfWork);
        Account[] accounts = new Account[2];

        return jobService.getJob(jobId, unitOfWork)
                .thenCompose(job -> {
                    if (job.getIdentityKey() != jobId)
                        throw new BadRequestException(ErrorMessages.BAD_REQUEST_IDS_MISMATCH);

                    return CompletableFuture.allOf(
                            accountService.getAccount(email, unitOfWork).thenAccept(acc -> accounts[0] = acc),
                            job.getAccount().getForeignObject(unitOfWork).thenAccept(acc -> accounts[1] = acc)
                    );
                }).thenCompose(account -> {
                    if (accounts[0].getIdentityKey() != accounts[1].getIdentityKey())
                        throw new UnauthorizedException(ErrorMessages.UN_AUTHORIZED_ID_AND_EMAIL_MISMATCH);
                    return scheduleDataMapper.create(newSchedule);
                }).thenCompose( aVoid ->unitOfWork.commit().thenApply(__ -> newSchedule));
    }

    public CompletableFuture<Void> deleteSchedule(long jobId, long scheduleId, String email) {
        UnitOfWork unitOfWork = new UnitOfWork();
        DataMapper<Schedule, Long> scheduleDataMapper = MapperRegistry.getMapper(Schedule.class, unitOfWork);

        return accountService.getAccount(email, unitOfWork)
                .thenCompose(account ->
                        scheduleDataMapper.find(
                                new EqualAndCondition<>("accountId", account.getIdentityKey()),
                                new EqualAndCondition<>("scheduleId", scheduleId),
                                new EqualAndCondition<>("jobId", jobId)
                        )
                                .thenCompose(schedule -> {
                                            if (schedule.size() == 0)
                                                throw new ResourceNotFoundException(ErrorMessages.RESOURCE_NOTFOUND);

                                            return scheduleDataMapper.deleteById(scheduleId);
                                        }
                                ))
                .thenCompose( res ->unitOfWork.commit().thenApply(__ -> res));
    }
}

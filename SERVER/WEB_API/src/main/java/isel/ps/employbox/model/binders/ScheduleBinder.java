package isel.ps.employbox.model.binders;

import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.entities.Schedule;
import isel.ps.employbox.model.input.InSchedule;
import isel.ps.employbox.model.output.OutSchedule;

import java.util.concurrent.CompletableFuture;

public class ScheduleBinder implements ModelBinder<Schedule,OutSchedule,InSchedule> {

    @Override
    public CompletableFuture<OutSchedule> bindOutput(Schedule schedule) {
        UnitOfWork unitOfWork = new UnitOfWork();
        AccountBinder accountBinder = new AccountBinder();
        JobBinder jobBinder = new JobBinder();

        return schedule.getAccount()
                .getForeignObject(unitOfWork)
                .thenCompose(accountBinder::bindOutput)
                .thenCompose( outAccount ->
                        schedule.getJob()
                                .getForeignObject(unitOfWork)
                                .thenCompose(jobBinder::bindOutput)
                                .thenCompose(outJob -> unitOfWork.commit().thenApply(aVoid -> outJob))
                                .thenApply( outJob ->
                                    new OutSchedule(
                                            schedule.getIdentityKey(),
                                            outJob,
                                            outAccount,
                                            schedule.getStartDate(),
                                            schedule.getEndDate(),
                                            schedule.getStartHour(),
                                            schedule.getEndHour(),
                                            schedule.getType(),
                                            schedule.getVersion()
                                    )
                                )
                );
    }

    @Override
    public Schedule bindInput(InSchedule inSchedule) {
        return new Schedule(
                inSchedule.getScheduleId(),
                inSchedule.getAccountId(),
                inSchedule.getJobId(),
                inSchedule.getStartDate(),
                inSchedule.getEndDate(),
                inSchedule.getStartHour(),
                inSchedule.getEndHour(),
                inSchedule.getScheduleType(),
                inSchedule.getVersion()
        );
    }
}

package isel.ps.employbox.model.binders.jobs;

import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.jobs.Schedule;
import isel.ps.employbox.model.input.InSchedule;
import isel.ps.employbox.model.output.OutSchedule;

import java.util.concurrent.CompletableFuture;

public class ScheduleBinder implements ModelBinder<Schedule,OutSchedule,InSchedule> {

    @Override
    public CompletableFuture<OutSchedule> bindOutput(Schedule schedule) {
        UnitOfWork unitOfWork = new UnitOfWork();
        JobBinder jobBinder = new JobBinder();

        return schedule.getJob()
                .getForeignObject(unitOfWork)
                .thenCompose(jobBinder::bindOutput)
                .thenCompose(outJob -> unitOfWork.commit().thenApply(aVoid -> outJob))
                .thenApply( outJob ->
                        new OutSchedule(
                                schedule.getIdentityKey(),
                                outJob,
                                schedule.getDate(),
                                schedule.getStartHour(),
                                schedule.getEndHour(),
                                schedule.getRepeats(),
                                schedule.getVersion()
                        )
                );
    }

    @Override
    public Schedule bindInput(InSchedule inSchedule) {
        return new Schedule(
                inSchedule.getScheduleId(),
                inSchedule.getJobId(),
                inSchedule.getDate(),
                inSchedule.getStartHour(),
                inSchedule.getEndHour(),
                inSchedule.getRepeats(),
                inSchedule.getVersion()
        );
    }
}

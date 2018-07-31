package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.curricula.childs.PreviousJobs;
import isel.ps.employbox.model.entities.curricula.childs.Project;
import isel.ps.employbox.model.input.curricula.childs.InPreviousJobs;
import isel.ps.employbox.model.input.curricula.childs.InProject;
import isel.ps.employbox.model.output.OutPreviousJobs;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class PreviousJobsBinder implements ModelBinder<PreviousJobs, OutPreviousJobs, InPreviousJobs> {
    @Override
    public OutPreviousJobs bindOutput(PreviousJobs previousJob) {
        return new OutPreviousJobs(
                previousJob.getIdentityKey(),
                previousJob.getAccountId(),
                previousJob.getCurriculumId(),
                previousJob.getCompanyName(),
                previousJob.getBeginDate().toString(),
                previousJob.getEndDate().toString(),
                previousJob.getWorkLoad(),
                previousJob.getRole());
    }

    @Override
    public PreviousJobs bindInput(InPreviousJobs inPreviousJobs) {
        return new PreviousJobs(
                inPreviousJobs.getPreviousJobId(),
                inPreviousJobs.getAccountId(),
                inPreviousJobs.getCurriculumId(),
                inPreviousJobs.getBeginDate(),
                inPreviousJobs.getEndDate(),
                inPreviousJobs.getCompanyName(),
                inPreviousJobs.getWorkLoad(),
                inPreviousJobs.getRole(),
                inPreviousJobs.getVersion()
        );
    }
}

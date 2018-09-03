package isel.ps.employbox.model.binders.jobs;

import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.jobs.Application;
import isel.ps.employbox.model.input.InApplication;
import isel.ps.employbox.model.output.OutApplication;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ApplicationBinder implements ModelBinder<Application,OutApplication,InApplication> {

    @Override
    public CompletableFuture<OutApplication> bindOutput(Application application) {
        UnitOfWork unitOfWork = new UnitOfWork();
        AccountBinder accountBinder = new AccountBinder();
        JobBinder jobBinder = new JobBinder();


        return application.getAccount()
                .getForeignObject(unitOfWork)
                .thenCompose(accountBinder::bindOutput)
                .thenCompose(outAccount ->
                        application.getJob()
                                .getForeignObject(unitOfWork)
                                .thenCompose(jobBinder::bindOutput)
                                .thenCompose(outJob -> unitOfWork.commit().thenApply(aVoid -> outJob))
                                .thenApply(outJob ->

                                        new OutApplication(
                                                outJob, outAccount,
                                                application.getIdentityKey(),
                                                application.getCurriculumId(),
                                                application.getDate())
                                ));
    }

    @Override
    public Application bindInput(InApplication inApplication) {
        return new Application (inApplication.getApplicationId(),
                inApplication.getAccountId(),
                inApplication.getJobId(),
                inApplication.getCurriculumId(),
                inApplication.getDate(),
                inApplication.getVersion());
    }
}

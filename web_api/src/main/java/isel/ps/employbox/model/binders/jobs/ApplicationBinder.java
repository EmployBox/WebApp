package isel.ps.employbox.model.binders.jobs;

import com.github.jayield.rapper.mapper.externals.Foreign;
import com.github.jayield.rapper.unitofwork.UnitOfWork;
import isel.ps.employbox.model.binders.AccountBinder;
import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.binders.curricula.CurriculumBinder;
import isel.ps.employbox.model.entities.Curriculum;
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
        CurriculumBinder curriculumBinder = new CurriculumBinder();


        return application.getAccount()
                .getForeignObject(unitOfWork)
                .thenCompose(accountBinder::bindOutput)
                .thenCompose(outAccount ->
                        application.getJob()
                                .getForeignObject(unitOfWork)
                                .thenCompose(jobBinder::bindOutput)
                                .thenCompose(outJob ->
                                {
                                    Foreign<Curriculum, Long> curriculum = application.getCurriculum();
                                    return curriculum != null
                                            ? curriculum
                                            .getForeignObject(unitOfWork)
                                            .thenCompose(curriculumBinder::bindOutput)
                                            .thenApply(outCurriculum ->
                                                    new OutApplication(
                                                            outCurriculum,
                                                            outJob,
                                                            outAccount,
                                                            application.getIdentityKey(),
                                                            application.getDatetime())
                                            )
                                            : CompletableFuture.completedFuture(new OutApplication(
                                            null,
                                            outJob,
                                            outAccount,
                                            application.getIdentityKey(),
                                            application.getDatetime())
                                    );
                                })
                                .thenCompose(res -> unitOfWork.commit().thenApply(aVoid -> res)));
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

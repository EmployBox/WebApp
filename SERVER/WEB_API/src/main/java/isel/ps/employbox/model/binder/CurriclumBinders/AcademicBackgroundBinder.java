package isel.ps.employbox.model.binder.CurriclumBinders;

import isel.ps.employbox.model.binder.ModelBinder;
import isel.ps.employbox.model.entities.CurriculumChilds.AcademicBackground;
import isel.ps.employbox.model.output.OutAcademicBackground;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class AcademicBackgroundBinder implements ModelBinder<AcademicBackground, OutAcademicBackground, Void> {
    @Override
    public Mono<OutAcademicBackground> bindOutput(CompletableFuture<AcademicBackground> academicBackgroundCompletableFuture) {
        return Mono.fromFuture(
                academicBackgroundCompletableFuture.thenApply(
                        academicBackground -> new OutAcademicBackground(
                                academicBackground.getIdentityKey(),
                                academicBackground.getAccountId(),
                                academicBackground.getCurriculumId(),
                                academicBackground.getInstitution(),
                                academicBackground.getDegreeObtained(),
                                academicBackground.getStudyArea(),
                                academicBackground.getBeginDate().toString(),
                                academicBackground.getEndDate().toString())
                ));
    }

    @Override
    public AcademicBackground bindInput(Void object) {
        throw new NotImplementedException();
    }
}

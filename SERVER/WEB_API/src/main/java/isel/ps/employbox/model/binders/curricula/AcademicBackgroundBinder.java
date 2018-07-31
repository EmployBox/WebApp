package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.curricula.childs.AcademicBackground;
import isel.ps.employbox.model.input.curricula.childs.InAcademicBackground;
import isel.ps.employbox.model.output.OutAcademicBackground;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class AcademicBackgroundBinder implements ModelBinder<AcademicBackground, OutAcademicBackground, InAcademicBackground> {
    @Override
    public CompletableFuture<OutAcademicBackground> bindOutput(AcademicBackground academicBackground) {
        return CompletableFuture.completedFuture(new OutAcademicBackground(
                academicBackground.getIdentityKey(),
                academicBackground.getAccountId(),
                academicBackground.getCurriculumId(),
                academicBackground.getInstitution(),
                academicBackground.getDegreeObtained(),
                academicBackground.getStudyArea(),
                academicBackground.getBeginDate(),
                academicBackground.getEndDate()
        ));
    }

    @Override
    public AcademicBackground bindInput(InAcademicBackground inAcademicBackground) {
        return new AcademicBackground(
                inAcademicBackground.getAccountId(),
                inAcademicBackground.getCurriculumId(),
                inAcademicBackground.getBeginDate(),
                inAcademicBackground.getEndDate(),
                inAcademicBackground.getStudyArea(),
                inAcademicBackground.getInstitution(),
                inAcademicBackground.getDegreeObtained(),
                inAcademicBackground.getVersion(),
                inAcademicBackground.getId()
        );
    }
}

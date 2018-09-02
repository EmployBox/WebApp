package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.input.curricula.childs.InCurriculum;
import isel.ps.employbox.model.output.OutCurriculum;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class CurriculumBinder implements ModelBinder<Curriculum,OutCurriculum,InCurriculum> {
    private final PreviousJobsBinder previousJobsBinder;
    private final CurriculumExperienceBinder curriculumExperienceBinder;

    public CurriculumBinder(PreviousJobsBinder previousJobsBinder, CurriculumExperienceBinder curriculumExperienceBinder) {
        this.previousJobsBinder = previousJobsBinder;
        this.curriculumExperienceBinder = curriculumExperienceBinder;
    }

    @Override
    public CompletableFuture<OutCurriculum> bindOutput(Curriculum curriculum) {
        return CompletableFuture.completedFuture(new OutCurriculum(
                curriculum.getAccountId(),
                curriculum.getIdentityKey(),
                curriculum.getTitle()
        ));
    }

    @Override
    public Curriculum bindInput(InCurriculum obj) {
        return new Curriculum(
                obj.getAccountId(),
                obj.getCurriculumId(),
                obj.getTitle(),
                obj.getVersion(),
                obj.getPreviousJobs().stream().map(previousJobsBinder::bindInput).collect(Collectors.toList()),
                obj.getAcademicBackground(),
                obj.getExperiences().stream().map(curriculumExperienceBinder::bindInput).collect(Collectors.toList()),
                obj.getProjects());
    }
}

package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.input.curricula.childs.InCurriculumExperience;
import isel.ps.employbox.model.output.OutCurriculumExperience;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CurriculumExperienceBinder implements ModelBinder<CurriculumExperience, OutCurriculumExperience, InCurriculumExperience> {

    @Override
    public CompletableFuture<OutCurriculumExperience> bindOutput(CurriculumExperience curriculumExperience) {
        return CompletableFuture.completedFuture(new OutCurriculumExperience(
                curriculumExperience.getIdentityKey(),
                curriculumExperience.getAccountId(),
                curriculumExperience.getCurriculumId(),
                curriculumExperience.getCompetence(),
                curriculumExperience.getYears()
        ));
    }

    @Override
    public CurriculumExperience bindInput(InCurriculumExperience inCurriculumExperience) {
        return new CurriculumExperience(
                inCurriculumExperience.getAccountId(),
                inCurriculumExperience.getCurriculumId(),
                inCurriculumExperience.getCompetence(),
                inCurriculumExperience.getYears(),
                inCurriculumExperience.getVersion(),
                inCurriculumExperience.getCurriculumExperienceId()
        );
    }
}

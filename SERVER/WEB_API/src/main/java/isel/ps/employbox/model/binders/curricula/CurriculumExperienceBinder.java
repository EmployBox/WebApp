package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.curricula.childs.CurriculumExperience;
import isel.ps.employbox.model.input.curricula.childs.InCurriculum;
import isel.ps.employbox.model.input.curricula.childs.InCurriculumExperience;
import isel.ps.employbox.model.output.OutCurriculumExperience;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class CurriculumExperienceBinder implements ModelBinder<CurriculumExperience, OutCurriculumExperience, InCurriculumExperience> {

    @Override
    public Mono<OutCurriculumExperience> bindOutput(CompletableFuture<CurriculumExperience> curriculumExperienceCompletableFuture) {
        return Mono.fromFuture(
                curriculumExperienceCompletableFuture.thenApply(
                        curriculumExperience -> new OutCurriculumExperience(
                                curriculumExperience.getIdentityKey(),
                                curriculumExperience.getAccountId(),
                                curriculumExperience.getCurriculumId(),
                                curriculumExperience.getCompetences(),
                                curriculumExperience.getYears())
                )
        );
    }

    @Override
    public CurriculumExperience bindInput(InCurriculumExperience inCurriculumExperience) {
        return new CurriculumExperience(
                inCurriculumExperience.getAccountId(),
                inCurriculumExperience.getCurriculumId(),
                inCurriculumExperience.getCompetences(),
                inCurriculumExperience.getYears(),
                inCurriculumExperience.getVersion(),
                inCurriculumExperience.getCurriculumExperienceId()
        );
    }
}

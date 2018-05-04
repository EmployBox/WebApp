package isel.ps.employbox.model.binder.CurriclumBinders;

import isel.ps.employbox.model.binder.ModelBinder;
import isel.ps.employbox.model.entities.CurriculumChilds.CurriculumExperience;
import isel.ps.employbox.model.output.OutCurriculumExperience;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.CompletableFuture;

@Component
public class CurriculumExperienceBinder implements ModelBinder<CurriculumExperience, OutCurriculumExperience, Void> {

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
    public CurriculumExperience bindInput(Void object) {
        throw new NotImplementedException();
    }
}

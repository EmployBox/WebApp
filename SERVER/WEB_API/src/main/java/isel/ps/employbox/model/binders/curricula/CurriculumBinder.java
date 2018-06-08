package isel.ps.employbox.model.binders.curricula;

import isel.ps.employbox.model.binders.ModelBinder;
import isel.ps.employbox.model.entities.Curriculum;
import isel.ps.employbox.model.input.curricula.childs.InCurriculum;
import isel.ps.employbox.model.output.OutCurriculum;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class CurriculumBinder implements ModelBinder<Curriculum,OutCurriculum,InCurriculum> {

    @Override
    public Mono<OutCurriculum> bindOutput(CompletableFuture<Curriculum> curriculumCompletableFuture) {
        return Mono.fromFuture(
                curriculumCompletableFuture.thenApply(
                        curriculum ->
                                new OutCurriculum(
                                        curriculum.getAccountId(),
                                        curriculum.getIdentityKey(),
                                        curriculum.getTitle()
                                )));
    }

    @Override
    public Curriculum bindInput(InCurriculum obj) {
        return new Curriculum(
                obj.getAccountId(),
                obj.getCurriculumId(),
                obj.getTitle(),
                obj.getVersion(),
                obj.getPreviousJobs(),
                obj.getAcademicBackground(),
                obj.getExperiences(),
                obj.getProjects());
    }

    /*


    public List<OutCurriculum.OutPreviousJobs> bindOutPreviousJobs(List<PreviousJobs> list){
        return list.stream()
                .map(curr-> new OutCurriculum.OutPreviousJobs(
                    curr.getCompanyName(),
                    curr.getBeginDate().toString(),
                    curr.getEndDate().toString(),
                    curr.getWorkLoad(),
                    curr.getRole())
                ).collect(Collectors.toList());
}

    public List<OutCurriculum.OutProject> bindOutProject(List<Project> list){
        return StreamSupport.stream(list.spliterator(),false)
                .map(curr -> new OutCurriculum.OutProject(curr.getName(),curr.getDescription()))
                .collect(Collectors.toList());
    }*/
}

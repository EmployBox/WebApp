package isel.ps.employbox.model.binder;

import isel.ps.employbox.model.entities.*;
import isel.ps.employbox.model.input.InCurriculum;
import isel.ps.employbox.model.output.OutCurriculum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class CurriculumBinder extends ModelBinder<Curriculum, OutCurriculum, InCurriculum > {

    @Override
    public OutCurriculum bindOutput(Curriculum object) {
        return new OutCurriculum(
                object.getUserId(),
                object.getCurriculumId(),
                object.getTitle(),
                bindExperience(object.getExperiences().get()),
                bindPreviousJobs(object.getPreviousJobs().get()),
                bindOutProject(object.getProjects().get()),
                bindAcademicBackground(object.getAcademicBackground().get())
        );
    }

    @Override
    public Curriculum bindInput(InCurriculum obj) {
        return new Curriculum(obj.getId(), obj.getTitle());
    }

    private List<OutCurriculum.OutExperience> bindExperience(List<CurriculumExperience> list){
        return StreamSupport.stream(list.spliterator(),false)
                .map(curr-> new OutCurriculum.OutExperience(curr.getCompetences(), curr.getYears()))
                .collect(Collectors.toList());
    }

    private List<OutCurriculum.OutAcademicBackground> bindAcademicBackground(List<AcademicBackground> list){
        return StreamSupport.stream(list.spliterator(),false)
                .map(curr-> new OutCurriculum.OutAcademicBackground(
                        curr.getInstitution(),
                        curr.getDegreeObtained(),
                        curr.getStudyArea(),
                        curr.getBeginDate().toString(),
                        curr.getEndDate().toString())
                ).collect(Collectors.toList());
    }

    private List<OutCurriculum.OutPreviousJobs> bindPreviousJobs(List<PreviousJobs> list){
        return StreamSupport.stream(list.spliterator(),false)
                .map(curr-> new OutCurriculum.OutPreviousJobs(
                    curr.getCompanyName(),
                    curr.getBeginDate().toString(),
                    curr.getEndDate().toString(),
                    curr.getWorkLoad(),
                    curr.getRole())
                ).collect(Collectors.toList());
}

    private List<OutCurriculum.OutProject> bindOutProject(List<Project> list){
        return StreamSupport.stream(list.spliterator(),false)
                .map(curr -> new OutCurriculum.OutProject(curr.getName(),curr.getDescription()))
                .collect(Collectors.toList());
    }
}

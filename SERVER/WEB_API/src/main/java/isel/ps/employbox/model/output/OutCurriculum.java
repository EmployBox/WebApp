package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutCurriculum extends ResourceSupport {

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String title;


    public OutCurriculum(
            long userId,
            long curriculumId,
            String title
    ){
        this.curriculumId = curriculumId;
        this.title = title;
        this.add( linkTo (CurriculumController.class, userId).slash(curriculumId).withSelfRel());
        this.add( linkTo ( methodOn(CurriculumController.class).getProjects(userId,curriculumId)).withRel("projects"));
        this.add( linkTo ( methodOn(CurriculumController.class).getAcademicBackground(userId,curriculumId)).withRel("academicBackground"));
        this.add( linkTo ( methodOn(CurriculumController.class).getPreviousJobs(userId,curriculumId)).withRel("previousJobs"));
        this.add( linkTo ( methodOn(CurriculumController.class).getCurriculumExperiences(userId,curriculumId)).withRel("curriculumExperience"));
    }
}

package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutCurriculumExperience extends ResourceSupport {

    @JsonProperty
    private final long experienceId;

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String competence;

    @JsonProperty
    private final int year;

    public OutCurriculumExperience(long experienceId, long accountId, long curriculumId, String competence, int year){
        this.experienceId = experienceId;
        this.competence = competence;
        this.year = year;
        this.accountId = accountId;
        this.curriculumId = curriculumId;
        this.add( linkTo( methodOn(CurriculumController.class).getCurriculumExperience(this.accountId, this.curriculumId, 0L)).slash(experienceId).withSelfRel());
    }
}
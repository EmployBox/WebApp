package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.JobController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutJobExperience extends ResourceSupport {

    @JsonProperty
    private final long experienceId;

    @JsonProperty
    private final long jobId;

    @JsonProperty
    private final long jobExperienceId;

    @JsonProperty
    private final String competence;

    @JsonProperty
    private final int years;

    public OutJobExperience(long experienceId, long jobId, long jobExperienceId, String competence, int years) {
        this.experienceId = experienceId;
        this.jobId = jobId;
        this.jobExperienceId = jobExperienceId;
        this.competence = competence;
        this.years = years;
        this.add( linkTo( methodOn(JobController.class).getJobExperiences(this.jobId)).slash(experienceId).withSelfRel());
    }
}
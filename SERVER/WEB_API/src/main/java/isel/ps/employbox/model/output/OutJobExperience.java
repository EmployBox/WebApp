package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.JobController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutJobExperience extends OutputDto {
    @JsonProperty
    private final long jobId;

    @JsonProperty
    private final long jobExperienceId;

    @JsonProperty
    private final String competence;

    @JsonProperty
    private final int years;

    @JsonProperty
    private final _Links _links;

    public OutJobExperience(long jobExperienceId, long jobId, String competence, int years) {
        this.jobId = jobId;
        this.jobExperienceId = jobExperienceId;
        this.competence = competence;
        this.years = years;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new JobExperienceItemOutput();
    }

    class JobExperienceItemOutput {
        @JsonProperty
        private final _Links _links = new _Links();

        class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( methodOn(JobController.class).getJobExperiences(jobId, 0,0)).slash(jobExperienceId).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(JobController.class).getJobExperiences(jobId, 0,0)).slash(jobExperienceId).withSelfRel().getHref();
        }
    }
}
package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.jobs.JobExperienceController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutJobExperience implements OutputDto {

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
        private final long jobId;

        @JsonProperty
        private final long jobExperienceId;

        @JsonProperty
        private final String competence;

        @JsonProperty
        private final int years;

        @JsonProperty
        private _Links _links;

        JobExperienceItemOutput() {
            this.jobId = OutJobExperience.this.jobId;
            jobExperienceId = OutJobExperience.this.jobExperienceId;
            competence = OutJobExperience.this.competence;
            years = OutJobExperience.this.years;
            _links = new _Links();
        }

        private class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( methodOn(JobExperienceController.class).getJobExperiences(jobId, 0,5)).slash(jobExperienceId).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(JobExperienceController.class).getJobExperiences(jobId, 0,5)).slash(jobExperienceId).withSelfRel().getHref();
        }
    }

    @Override
    public String toString() {
        return "OutJobExperience{" +
                "jobId=" + jobId +
                ", jobExperienceId=" + jobExperienceId +
                ", competence='" + competence + '\'' +
                ", years=" + years +
                ", _links=" + _links +
                '}';
    }
}
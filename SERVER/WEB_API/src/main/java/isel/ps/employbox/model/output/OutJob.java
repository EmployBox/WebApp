package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.JobController;
import isel.ps.employbox.controllers.UserAccountController;

import java.sql.Timestamp;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutJob extends OutputDto {

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final long jobId;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String address;

    @JsonProperty
    private final double wage;

    @JsonProperty
    private final String description;

    @JsonProperty
    private final String schedule;

    @JsonProperty
    private final Timestamp offerBeginDate;

    @JsonProperty
    private final Timestamp offerEndDate;

    @JsonProperty
    private final String offerType;

    @JsonProperty
    private final _Links _links;

    public OutJob(
            long accountId,
            long jobId,
            String title,
            String address,
            double wage,
            String description,
            String schedule,
            Timestamp offerBeginDate,
            Timestamp offerEndDate,
            String offerType)
    {
        this.accountId = accountId;
        this.jobId = jobId;
        this.title = title;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.schedule = schedule;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this._links = new _Links();
    }

    @Override
    public Object getCollectionItemOutput() {
        return null;
    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        @JsonProperty
        private Experiences experiences = new Experiences();

        @JsonProperty
        private Applications application = new Applications();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( JobController.class).slash(jobId).withSelfRel().getHref();
        }

        private class Experiences {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(JobController.class).getJobExperiences(jobId, 0)).withRel("experiences").getHref();
        }

        private class Applications {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(UserAccountController.class).getAllApplications(accountId, 0)).withRel("applications").getHref();
        }
    }
}

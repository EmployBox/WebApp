package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.JobController;
import isel.ps.employbox.controllers.UserAccountController;

import java.sql.Timestamp;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

//todo embedded
public class OutJob implements OutputDto {

    @JsonProperty
    private final OutAccount account;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Timestamp offerBeginDate;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Timestamp offerEndDate;

    @JsonProperty
    private final String offerType;

    @JsonProperty
    private final _Links _links;

    public OutJob(
            OutAccount account,
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
        this.account = account;
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

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new JobItemOutput(title, description);
    }

    class JobItemOutput {
        @JsonProperty
        private String title;

        @JsonProperty
        private final String description;

        @JsonProperty
        private final _Links _links = new _Links();

        private JobItemOutput(String title, String description){
            this.title = title;
            this.description = description;
        }

        class _Links {
            @JsonProperty
            private Self self = new Self();

            private class Self{
                @JsonProperty
                final String href = HOSTNAME + linkTo(JobController.class).slash(jobId).withSelfRel().getHref();
            }
        }
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
            final String href = HOSTNAME + linkTo ( methodOn(JobController.class).getJobExperiences(jobId, 0,0)).withRel("experiences").getHref();
        }

        private class Applications {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(UserAccountController.class).getAllApplications(account.getAccountId(), 0,0)).withRel("applications").getHref();
        }
    }
}

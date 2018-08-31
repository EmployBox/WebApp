package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.jobs.JobController;
import isel.ps.employbox.controllers.UserAccountController;
import isel.ps.employbox.controllers.jobs.JobExperienceController;
import isel.ps.employbox.model.output.OutAccount.AccountItemOutput;

import java.sql.Timestamp;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

//todo embedded
public class OutJob implements OutputDto<OutJob.JobItemOutput> {

    private final OutAccount _account;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Timestamp offerBeginDate;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Timestamp offerEndDate;

    @JsonProperty
    private final String offerType;

    @JsonProperty
    private final String type;

    @JsonProperty
    private final _Links _links;

    @JsonProperty
    private final _Embedded _embedded;


    public OutJob(
            OutAccount account,
            long jobId,
            String title,
            String address,
            double wage,
            String description,
            Timestamp offerBeginDate,
            Timestamp offerEndDate,
            String offerType, String type)
    {
        this._account = account;
        this.jobId = jobId;
        this.title = title;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.type = type;
        this._links = new _Links();
        _embedded = new _Embedded();
    }

    @JsonIgnore
    @Override
    public JobItemOutput getCollectionItemOutput() {
        return new JobItemOutput(jobId, (AccountItemOutput) _account.getCollectionItemOutput(), title, offerBeginDate, address, offerType, type);
    }

    public long getJobId() {
        return this.jobId;
    }

    class JobItemOutput {
        @JsonProperty
        private final long jobId;
        @JsonProperty
        private final AccountItemOutput account;
        @JsonProperty
        private final String title;
        @JsonProperty
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        private final Timestamp offerBeginDate;
        @JsonProperty
        private final String address;
        @JsonProperty
        private final String offerType;
        @JsonProperty
        private final String type;
        @JsonProperty
        private final _Links _links;

        private JobItemOutput(long jobId, AccountItemOutput account, String title, Timestamp offerBeginDate, String address, String offerType, String type){
            this.jobId = jobId;
            this.account = account;
            this.title = title;
            this.offerBeginDate = offerBeginDate;
            this.address = address;
            this.offerType = offerType;
            this.type = type;
            this._links = new _Links();
        }

        class _Links {
            @JsonProperty
            private Self self = new Self();
            @JsonProperty
            private Application applications = new Application();
            @JsonProperty
            private Apply apply = new Apply();

            @JsonProperty
            private Application application = new Application();

            private class Self{
                @JsonProperty
                final String href = HOSTNAME + linkTo(JobController.class).slash(jobId).withSelfRel().getHref();
            }

            private class Application{
                @JsonProperty
                final String href = HOSTNAME + linkTo(methodOn(JobController.class).getApplication(jobId, 0, 5)).withSelfRel().getHref();
            }

            private class Apply {
                @JsonProperty
                final String href = HOSTNAME + linkTo(methodOn(UserAccountController.class).createApplication(_account.getAccountId(), jobId, null, null)).withRel("apply").getHref();
            }
        }
    }

    private class _Embedded{
        @JsonProperty
        private final OutAccount account = _account;

    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        @JsonProperty
        private Experiences experiences = new Experiences();

        @JsonProperty
        private Applications applications = new Applications();

        @JsonProperty
        private Apply apply = new Apply();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( JobController.class).slash(jobId).withSelfRel().getHref();
        }

        private class Experiences {
            @JsonProperty
            final String href = HOSTNAME + linkTo ( methodOn(JobExperienceController.class).getJobExperiences(jobId, 0,5)).withRel("experiences").getHref();
        }

        private class Applications {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(JobController.class).getApplication(jobId, 0, 5)).withSelfRel().getHref();
        }

        private class Apply {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(UserAccountController.class).createApplication(_account.getAccountId(), jobId, null, null)).withRel("apply").getHref();
        }
    }
}

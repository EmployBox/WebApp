package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.UserAccountController;

import java.sql.Timestamp;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutApplication implements OutputDto {

    private final OutJob _outJob;

    private final OutAccount _outAccount;

    @JsonProperty
    private final long applicationId;

    @JsonProperty
    private final long accountId;

    @JsonProperty
    private final long jobId;

    @JsonProperty
    private final Long curriculumId;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Timestamp date;

    @JsonProperty
    private final _Links _links;

    @JsonProperty
    private final _Embedded _embedded;

    public OutApplication(OutJob outJob, OutAccount outAccount, long applicationId, Long curriculumId, Timestamp date) {
        _outJob = outJob;
        _outAccount = outAccount;
        this.applicationId = applicationId;
        this.accountId = outAccount.getAccountId();
        this.jobId = outJob.getJobId();
        this.curriculumId = curriculumId;
        this.date = date;
        this._links = new _Links();
        this._embedded = new _Embedded();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new ApplicationItemOutput();
    }

    class ApplicationItemOutput {
        @JsonProperty
        private final _Links _links = new _Links();

        private class _Links {
            @JsonProperty
            private Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo(methodOn(UserAccountController.class).getApplication(accountId, jobId, applicationId)).withSelfRel().getHref();
            }
        }
    }

    private class _Embedded {
        @JsonProperty
        private final OutAccount account = _outAccount;

        @JsonProperty
        private final OutJob outJob = _outJob;

    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(UserAccountController.class).getApplication(accountId, jobId, applicationId)).withSelfRel().getHref();
        }
    }

}

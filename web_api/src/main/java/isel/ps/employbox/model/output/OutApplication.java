package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.UserAccountControllers.UserJobApplicationController;
import isel.ps.employbox.controllers.jobs.JobController;

import java.time.Instant;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutApplication implements OutputDto {

    private final OutJob _outJob;

    private final OutAccount _outAccount;

    private final OutCurriculum _outCurriculum;

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
    private final Instant date;

    @JsonProperty
    private final _Links _links;

    @JsonProperty
    private final _Embedded _embedded;

    public OutApplication(
            OutCurriculum outCurriculum,
            OutJob outJob,
            OutAccount outAccount,
            long applicationId,
            Instant date)
    {
        this._outCurriculum = outCurriculum;
        this._outJob = outJob;
        this._outAccount = outAccount;
        this.applicationId = applicationId;
        this.accountId = outAccount.getAccountId();
        this.curriculumId = outCurriculum != null ? outCurriculum.getCurriculumId() : null;
        this.jobId = outJob.getJobId();
        this.date = date;
        this._links = new _Links();
        this._embedded = new _Embedded();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new ApplicationItemOutput(_embedded);
    }

    class ApplicationItemOutput {

        @JsonProperty
        private final _Links _links = new _Links();

        @JsonProperty
        private final _Embedded _embedded;

        ApplicationItemOutput(_Embedded embedded) {
            this._embedded = embedded;
        }

        private class _Links {
            @JsonProperty
            private Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo(methodOn(JobController.class).getApplication(jobId, 0, 5)).withSelfRel().getHref();
            }
        }
    }

    private class _Embedded {
        @JsonProperty
        private final OutAccount account = _outAccount;

        @JsonProperty
        private final OutJob job = _outJob;

        @JsonProperty
        private final OutCurriculum curriculum = _outCurriculum;
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo(methodOn(UserJobApplicationController.class).getApplication(accountId, jobId, applicationId)).withSelfRel().getHref();
        }
    }

}

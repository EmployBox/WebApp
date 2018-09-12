package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.curricula.PreviousJobsController;

import java.time.Instant;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutPreviousJobs implements OutputDto {

    @JsonProperty
    private final long previousJobId;

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String companyName;

    @JsonProperty
    private final Instant beginDate;

    @JsonProperty
    private final Instant endDate;

    @JsonProperty
    private final String workload;

    @JsonProperty
    private final String role;

    @JsonIgnore
    private final long accountId;

    @JsonProperty
    private final _Links _links;

    public OutPreviousJobs(
            long previousJobId,
            long accountId,
            long jobId,
            String companyName,
            Instant beginDate,
            Instant endDate,
            String workload,
            String role
    )
    {
        this.previousJobId = previousJobId;
        this.curriculumId = jobId;
        this.companyName = companyName;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.workload = workload;
        this.role = role;
        this.accountId = accountId;
        this._links = new _Links();
    }

    @JsonIgnore
    @Override
    public Object getCollectionItemOutput() {
        return new PreviousItemOutput(companyName, workload, role);
    }

    class PreviousItemOutput {

        @JsonProperty
        private final String companyName;

        @JsonProperty
        private final String workload;

        @JsonProperty
        private final String role;

        public PreviousItemOutput(String companyName, String workload, String role) {
            this.companyName = companyName;
            this.workload = workload;
            this.role = role;
        }

        @JsonProperty
        private final _Links _links = new _Links();

    }

    private class _Links {
        @JsonProperty
        private _Links.Self self = new _Links.Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME +   linkTo( methodOn(PreviousJobsController.class).getPreviousJobs(accountId, curriculumId, 0,5)).slash(previousJobId).withSelfRel().getHref();
        }
    }
}

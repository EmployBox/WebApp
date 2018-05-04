package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CurriculumController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutPreviousJobs extends ResourceSupport {

    @JsonProperty
    private final long previousJobId;

    @JsonProperty
    private final long curriculumId;

    @JsonProperty
    private final String companyName;

    @JsonProperty
    private final String beginDate;

    @JsonProperty
    private final String endDate;

    @JsonProperty
    private final String workload;

    @JsonProperty
    private final String role;

    @JsonIgnore
    private final long accountId;

    public OutPreviousJobs(
            long previousJobId,
            long accountId,
            long jobId,
            String companyName,
            String beginDate,
            String endDate,
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
        this.add( linkTo( methodOn(CurriculumController.class).getPreviousJobs(this.accountId, curriculumId)).slash(previousJobId).withSelfRel());
    }
}

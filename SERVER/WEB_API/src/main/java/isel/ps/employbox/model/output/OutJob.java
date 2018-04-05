package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.JobController;
import isel.ps.employbox.controllers.UserController;
import org.springframework.hateoas.ResourceSupport;

import java.sql.Date;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutJob extends ResourceSupport {

    @JsonProperty
    private final long accountID;

    @JsonProperty
    private final long jobId;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final List<OutExperience> experiences;

    @JsonProperty
    private final String address;

    @JsonProperty
    private final double wage;

    @JsonProperty
    private final String description;

    @JsonProperty
    private final String schedule;

    @JsonProperty
    private final Date offerBeginDate;

    @JsonProperty
    private final Date offerEndDate;

    @JsonProperty
    private final String offerType;


    public OutJob(
            long accountID,
            long jobId,
            String title,
            List<OutExperience> experiences,
            String address,
            double wage,
            String description,
            String schedule,
            Date offerBeginDate,
            Date offerEndDate,
            String offerType)
    {
        this.accountID = accountID;
        this.jobId = jobId;
        this.title = title;
        this.experiences = experiences;
        this.address = address;
        this.wage = wage;
        this.description = description;
        this.schedule = schedule;
        this.offerBeginDate = offerBeginDate;
        this.offerEndDate = offerEndDate;
        this.offerType = offerType;
        this.add( linkTo (JobController.class).slash(jobId).withSelfRel());
        this.add( linkTo ( methodOn(UserController.class).getAllApplications(accountID,null)).withRel("applications"));
    }

    public class OutExperience extends ResourceSupport{
        @JsonProperty
        private final String competence;

        @JsonProperty
        private final int years;

        public OutExperience(String competence, int years) {
            this.competence = competence;
            this.years = years;
        }
    }
}

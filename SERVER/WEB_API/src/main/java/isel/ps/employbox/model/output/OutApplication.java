package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.UserController;
import org.springframework.hateoas.ResourceSupport;

import java.sql.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutApplication extends ResourceSupport {

    @JsonProperty
    private long accountId;

    @JsonProperty
    private long jobId;

    @JsonProperty
    private long curriculumId;

    @JsonProperty
    private Date date;

    public OutApplication(long userId, long jobId, long curriculumId, Date date){
        this.accountId = userId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this.add( linkTo( methodOn(UserController.class,userId)).slash(userId).withSelfRel());
    }
}

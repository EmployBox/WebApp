package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.UserAccountController;

import java.sql.Timestamp;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutApplication implements OutputDto {

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

    public OutApplication(long applicationId, long accountId, long jobId, Long curriculumId, Timestamp date){
        this.applicationId = applicationId;
        this.accountId = accountId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this._links = new _Links();
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
                final String href = HOSTNAME + linkTo( methodOn(UserAccountController.class).getApplication(accountId, jobId, applicationId)).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(UserAccountController.class).getApplication(accountId, jobId, applicationId)).withSelfRel().getHref();
        }
    }

}

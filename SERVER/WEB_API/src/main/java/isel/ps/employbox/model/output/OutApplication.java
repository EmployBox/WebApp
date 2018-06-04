package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.UserAccountController;

import java.sql.Timestamp;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class OutApplication extends OutputDto {

    @JsonProperty
    private long accountId;

    @JsonProperty
    private long jobId;

    @JsonProperty
    private Long curriculumId;

    @JsonProperty
    private Timestamp date;

    @JsonProperty
    private final _Links _links;

    public OutApplication(long accountId, long jobId, Long curriculumId, Timestamp date){
        this.accountId = accountId;
        this.jobId = jobId;
        this.curriculumId = curriculumId;
        this.date = date;
        this._links = new _Links();
    }

    @Override
    public Object getCollectionItemOutput() {
        return new ApplicationItemOutput();
    }

    class ApplicationItemOutput {
        private class _Links {
            @JsonProperty
            private Self self = new _Links.Self();

            private class Self {
                @JsonProperty
                final String href = HOSTNAME + linkTo( methodOn(UserAccountController.class).getApplication(accountId, jobId)).withSelfRel().getHref();
            }
        }
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo( methodOn(UserAccountController.class).getApplication(accountId, jobId)).withSelfRel().getHref();
        }
    }

}

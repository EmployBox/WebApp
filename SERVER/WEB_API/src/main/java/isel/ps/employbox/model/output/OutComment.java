package isel.ps.employbox.model.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import isel.ps.employbox.controllers.CommentController;
import org.springframework.hateoas.ResourceSupport;

import static isel.ps.employbox.model.output.OutputDto.HOSTNAME;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class OutComment extends ResourceSupport {

    @JsonProperty
    private final long accountIdFrom;

    @JsonProperty
    private final long accountIdTo;

    @JsonProperty
    private final long commmentId;

    @JsonProperty
    private final long mainCommentId;

    @JsonProperty
    private final String datetime;

    @JsonProperty
    private final String text;

    @JsonProperty
    private final _Links _links;

    public OutComment(long accountIdFrom, long accountIdTo, long commmentId, long mainCommentId, String datetime, String text) {
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.commmentId = commmentId;
        this.mainCommentId = mainCommentId;
        this.datetime = datetime;
        this.text = text;
        this._links = new _Links();
    }

    private class _Links {
        @JsonProperty
        private Self self = new Self();

        private class Self {
            @JsonProperty
            final String href = HOSTNAME + linkTo (CommentController.class, accountIdFrom).slash(commmentId).withSelfRel().getHref();
        }
    }
}
